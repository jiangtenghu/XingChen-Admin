package com.admin.auth.controller;

import com.admin.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth 2.1 Token 控制器
 * 
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
@Tag(name = "OAuth2 Token", description = "OAuth 2.1 令牌管理接口")
public class OAuth2TokenController {

    private final OAuth2AuthorizationService authorizationService;

    /**
     * 验证访问令牌
     */
    @PostMapping("/introspect")
    @Operation(summary = "令牌内省", description = "验证并获取访问令牌的详细信息")
    public Result<Map<String, Object>> introspectToken(
            @Parameter(description = "访问令牌") @RequestParam("token") String token,
            @Parameter(description = "令牌类型提示") @RequestParam(value = "token_type_hint", required = false) String tokenTypeHint) {
        
        try {
            log.info("开始验证令牌: {}", token.substring(0, Math.min(token.length(), 20)) + "...");

            // 查找授权信息
            OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            
            Map<String, Object> response = new HashMap<>();
            
            if (authorization != null && authorization.getAccessToken() != null) {
                // 令牌有效
                response.put("active", true);
                response.put("client_id", authorization.getRegisteredClientId());
                response.put("username", authorization.getPrincipalName());
                response.put("scope", String.join(" ", authorization.getAuthorizedScopes()));
                response.put("exp", authorization.getAccessToken().getToken().getExpiresAt().getEpochSecond());
                response.put("iat", authorization.getAccessToken().getToken().getIssuedAt().getEpochSecond());
                response.put("token_type", "Bearer");

                // 添加自定义声明
                if (authorization.getAccessToken().getClaims() != null) {
                    authorization.getAccessToken().getClaims().forEach((key, value) -> {
                        if (!response.containsKey(key)) {
                            response.put(key, value);
                        }
                    });
                }

                log.info("令牌验证成功，用户: {}", authorization.getPrincipalName());
            } else {
                // 令牌无效
                response.put("active", false);
                log.warn("令牌验证失败，令牌无效或已过期");
            }

            return Result.success("令牌验证完成", response);
            
        } catch (Exception e) {
            log.error("令牌验证失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("active", false);
            response.put("error", "introspection_failed");
            return Result.error("令牌验证失败：" + e.getMessage());
        }
    }

    /**
     * 撤销令牌
     */
    @PostMapping("/revoke")
    @Operation(summary = "撤销令牌", description = "撤销访问令牌或刷新令牌")
    public Result<String> revokeToken(
            @Parameter(description = "要撤销的令牌") @RequestParam("token") String token,
            @Parameter(description = "令牌类型提示") @RequestParam(value = "token_type_hint", required = false) String tokenTypeHint) {
        
        try {
            log.info("开始撤销令牌: {}", token.substring(0, Math.min(token.length(), 20)) + "...");

            // 查找并撤销访问令牌
            OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            if (authorization == null) {
                // 尝试查找刷新令牌
                authorization = authorizationService.findByToken(token, OAuth2TokenType.REFRESH_TOKEN);
            }

            if (authorization != null) {
                // 撤销授权（这会使所有相关令牌失效）
                authorizationService.remove(authorization);
                log.info("令牌撤销成功，用户: {}", authorization.getPrincipalName());
                return Result.success("令牌撤销成功", "OK");
            } else {
                log.warn("未找到要撤销的令牌");
                return Result.success("令牌不存在或已撤销", "OK");
            }

        } catch (Exception e) {
            log.error("令牌撤销失败", e);
            return Result.error("令牌撤销失败：" + e.getMessage());
        }
    }

    /**
     * 获取授权服务器配置信息
     */
    @GetMapping("/.well-known/oauth-authorization-server")
    @Operation(summary = "授权服务器配置", description = "获取OAuth 2.1授权服务器的配置信息")
    public Result<Map<String, Object>> authorizationServerMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("issuer", "http://localhost:8081");
        metadata.put("authorization_endpoint", "http://localhost:8081/oauth2/authorize");
        metadata.put("token_endpoint", "http://localhost:8081/oauth2/token");
        metadata.put("token_endpoint_auth_methods_supported", new String[]{"client_secret_basic", "client_secret_post"});
        metadata.put("jwks_uri", "http://localhost:8081/oauth2/jwks");
        metadata.put("userinfo_endpoint", "http://localhost:8081/userinfo");
        metadata.put("response_types_supported", new String[]{"code"});
        metadata.put("grant_types_supported", new String[]{"authorization_code", "refresh_token", "client_credentials"});
        metadata.put("revocation_endpoint", "http://localhost:8081/oauth2/revoke");
        metadata.put("introspection_endpoint", "http://localhost:8081/oauth2/introspect");
        metadata.put("code_challenge_methods_supported", new String[]{"S256"});
        metadata.put("scopes_supported", new String[]{OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL, "read", "write"});

        return Result.success("获取配置成功", metadata);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查OAuth服务是否正常")
    public Result<String> health() {
        return Result.success("OAuth 2.1服务运行正常", "OK");
    }
}
