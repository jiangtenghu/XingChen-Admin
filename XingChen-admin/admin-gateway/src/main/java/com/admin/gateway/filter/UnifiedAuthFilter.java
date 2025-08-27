package com.admin.gateway.filter;

import com.admin.common.core.domain.Result;
import com.admin.common.util.JwtUtil;
import com.admin.common.constant.CommonConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 网关统一认证过滤器
 * 职责：在网关层完成所有认证工作，内部服务直接信任网关传递的用户信息
 *
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@Component
public class UnifiedAuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 白名单路径 - 不需要认证的接口
     */
    private static final String[] SKIP_PATHS = {
        "/api/auth/login",
        "/api/auth/register",
        "/api/auth/refresh",
        "/api/auth/logout", 
        "/api/identity/register",
        "/api/identity/login",
        "/api/identity/check-**",
        "/api/identity/health",
        "/actuator/**",
        "/doc.html",
        "/webjars/**",
        "/swagger-resources/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        String method = request.getMethod().name();
        String clientIp = getClientIp(request);

        long startTime = System.currentTimeMillis();
        log.debug("网关统一认证处理: {} {} from {}", method, path, clientIp);

        try {
            // 1. 检查白名单路径
            if (isSkipPath(path)) {
                log.debug("白名单路径，跳过认证: {}", path);
                return addTraceHeaders(exchange, chain, startTime);
            }

            // 2. 提取JWT令牌
            String token = extractBearerToken(request);
            if (!StringUtils.hasText(token)) {
                log.debug("缺少访问令牌: {}", path);
                return handleUnauthorized(exchange, "缺少访问令牌", startTime);
            }

            // 3. 验证令牌（先查缓存，再验证JWT）
            return validateTokenAndForward(exchange, chain, token, clientIp, startTime);

        } catch (Exception e) {
            log.error("网关认证异常: {} - 路径: {}", e.getMessage(), path, e);
            return handleInternalError(exchange, "认证服务异常", startTime);
        }
    }

    /**
     * 验证Token并转发请求
     */
    private Mono<Void> validateTokenAndForward(ServerWebExchange exchange, GatewayFilterChain chain, 
                                              String token, String clientIp, long startTime) {
        try {
            // 1. 先查Redis缓存
            String cacheKey = "gateway:auth:" + getTokenHash(token);
            Object cachedUser = redisTemplate.opsForValue().get(cacheKey);
            
            if (cachedUser != null) {
                log.debug("从缓存获取用户信息: {}", cacheKey);
                return forwardWithUserContext(exchange, chain, cachedUser.toString(), startTime);
            }

            // 2. 缓存未命中，验证JWT
            if (!JwtUtil.validateToken(token)) {
                log.warn("Token验证失败: {}", token.substring(0, Math.min(10, token.length())));
                return handleUnauthorized(exchange, "Token无效或已过期", startTime);
            }

            // 3. 提取用户信息
            String userId = JwtUtil.getUserIdFromToken(token);
            String username = JwtUtil.getUsernameFromToken(token);

            if (!StringUtils.hasText(userId) || !StringUtils.hasText(username)) {
                return handleUnauthorized(exchange, "Token中缺少用户信息", startTime);
            }

            // 4. 构建用户上下文并缓存
            String userContext = buildUserContext(userId, username, clientIp);
            cacheUserContext(cacheKey, userContext);

            log.debug("Token验证成功: 用户={}, ID={}", username, userId);
            return forwardWithUserContext(exchange, chain, userContext, startTime);

        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage(), e);
            return handleUnauthorized(exchange, "Token验证失败", startTime);
        }
    }

    /**
     * 转发请求并添加用户上下文信息
     */
    private Mono<Void> forwardWithUserContext(ServerWebExchange exchange, GatewayFilterChain chain, 
                                            String userContext, long startTime) {
        try {
            // 解析用户上下文
            String[] parts = userContext.split("\\|");
            String userId = parts.length > 0 ? parts[0] : "";
            String username = parts.length > 1 ? parts[1] : "";
            String clientIp = parts.length > 2 ? parts[2] : "";

            // 添加用户信息到请求头，供下游服务使用
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header(CommonConstants.USER_ID_HEADER, userId)
                    .header(CommonConstants.USERNAME_HEADER, username)
                    .header(CommonConstants.CLIENT_IP_HEADER, clientIp)
                    .header(CommonConstants.AUTH_SOURCE_HEADER, CommonConstants.AUTH_SOURCE_GATEWAY)
                    .header(CommonConstants.AUTH_TIME_HEADER, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            log.debug("用户上下文已添加: userId={}, username={}", userId, username);

            return addTraceHeaders(
                exchange.mutate().request(modifiedRequest).build(), 
                chain, 
                startTime
            );

        } catch (Exception e) {
            log.error("添加用户上下文失败: {}", e.getMessage(), e);
            return handleInternalError(exchange, "内部错误", startTime);
        }
    }

    /**
     * 构建用户上下文字符串
     */
    private String buildUserContext(String userId, String username, String clientIp) {
        return String.join("|", userId, username, clientIp, String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 缓存用户上下文
     */
    private void cacheUserContext(String cacheKey, String userContext) {
        try {
            redisTemplate.opsForValue().set(cacheKey, userContext, 300, TimeUnit.SECONDS); // 5分钟缓存
            log.debug("用户上下文已缓存: {}", cacheKey);
        } catch (Exception e) {
            log.warn("缓存用户上下文失败: {}", e.getMessage());
        }
    }

    /**
     * 检查是否为跳过路径
     */
    private boolean isSkipPath(String path) {
        return Arrays.stream(SKIP_PATHS)
                .anyMatch(pattern -> pathMatches(path, pattern));
    }

    /**
     * 路径匹配
     */
    private boolean pathMatches(String path, String pattern) {
        if (pattern.endsWith("/**")) {
            return path.startsWith(pattern.substring(0, pattern.length() - 3));
        } else if (pattern.contains("**")) {
            String prefix = pattern.substring(0, pattern.indexOf("**"));
            return path.startsWith(prefix);
        } else if (pattern.contains("*")) {
            return path.matches(pattern.replace("*", ".*"));
        } else {
            return path.equals(pattern);
        }
    }

    /**
     * 提取Bearer令牌
     */
    private String extractBearerToken(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddress() != null ? 
               request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
    }

    /**
     * 获取Token哈希值（用于缓存键）
     */
    private String getTokenHash(String token) {
        return token.length() > 10 ? token.substring(token.length() - 10) : token;
    }

    /**
     * 添加追踪头并继续执行
     */
    private Mono<Void> addTraceHeaders(ServerWebExchange exchange, GatewayFilterChain chain, long startTime) {
        return chain.filter(exchange)
            .doFinally(signalType -> {
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().add("X-Response-Time", duration + "ms");
                response.getHeaders().add("X-Gateway-Auth", "UnifiedAuthFilter");
                
                log.debug("请求处理完成: {} {} - {}ms", 
                         exchange.getRequest().getMethod(), 
                         exchange.getRequest().getPath(), 
                         duration);
            });
    }

    /**
     * 处理未授权
     */
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message, long startTime) {
        return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, 401, message, startTime);
    }

    /**
     * 处理内部错误
     */
    private Mono<Void> handleInternalError(ServerWebExchange exchange, String message, long startTime) {
        return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, 500, message, startTime);
    }

    /**
     * 写入错误响应
     */
    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, int code, 
                                        String message, long startTime) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.getHeaders().add("X-Response-Time", (System.currentTimeMillis() - startTime) + "ms");
        response.getHeaders().add("X-Error-Source", "Gateway-Unified-Auth");

        Result<Void> errorResult = Result.error(code, message);
        
        try {
            String body = objectMapper.writeValueAsString(errorResult);
            return response.writeWith(Mono.just(
                response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))
            ));
        } catch (Exception e) {
            log.error("序列化错误响应失败: {}", e.getMessage());
            String fallbackBody = String.format("{\"code\":%d,\"message\":\"%s\"}", code, message);
            return response.writeWith(Mono.just(
                response.bufferFactory().wrap(fallbackBody.getBytes(StandardCharsets.UTF_8))
            ));
        }
    }

    @Override
    public int getOrder() {
        return -100; // 高优先级，最先执行
    }
}
