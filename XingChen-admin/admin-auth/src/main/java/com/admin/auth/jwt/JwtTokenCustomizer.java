package com.admin.auth.jwt;

import com.admin.auth.feign.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

/**
 * JWT Token 定制器
 * 自定义JWT Token的声明内容
 * 
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final UserServiceClient userServiceClient;

    @Override
    public void customize(JwtEncodingContext context) {
        if (context.getTokenType().getValue().equals("access_token")) {
            customizeAccessToken(context);
        }
    }

    /**
     * 自定义访问令牌
     */
    private void customizeAccessToken(JwtEncodingContext context) {
        try {
            // 获取用户名
            String username = context.getPrincipal().getName();
            log.info("正在为用户 {} 定制JWT Token", username);

            // 调用用户服务获取用户信息
            Map<String, Object> userResult = userServiceClient.getUserByUsername(username);
            if (userResult != null && userResult.get("data") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userData = (Map<String, Object>) userResult.get("data");

                // 添加自定义声明
                context.getClaims()
                    .claim("userId", userData.get("id"))
                    .claim("username", userData.get("username"))
                    .claim("email", userData.get("email"))
                    .claim("realName", userData.get("realName"))
                    .claim("userType", userData.get("userType"))
                    .claim("tenantId", userData.get("tenantId"))
                    .claim("status", userData.get("status"))
                    .claim("iat", Instant.now().getEpochSecond())
                    .claim("token_type", "Bearer");

                // 获取用户角色
                try {
                    Long userId = Long.valueOf(userData.get("id").toString());
                    Map<String, Object> rolesResult = userServiceClient.getUserRoles(userId);
                    if (rolesResult != null && rolesResult.get("data") != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> rolesData = (Map<String, Object>) rolesResult.get("data");
                        context.getClaims().claim("roles", rolesData.get("roles"));
                    }
                } catch (Exception e) {
                    log.warn("获取用户角色失败: {}", e.getMessage());
                    context.getClaims().claim("roles", new String[]{"USER"});
                }

                log.info("JWT Token定制完成，用户: {}, ID: {}", username, userData.get("id"));
            } else {
                log.warn("未能获取用户信息: {}", username);
                // 添加基本声明
                context.getClaims()
                    .claim("username", username)
                    .claim("iat", Instant.now().getEpochSecond())
                    .claim("token_type", "Bearer");
            }

        } catch (Exception e) {
            log.error("JWT Token定制失败", e);
            // 添加基本声明作为降级
            context.getClaims()
                .claim("username", context.getPrincipal().getName())
                .claim("iat", Instant.now().getEpochSecond())
                .claim("token_type", "Bearer")
                .claim("error", "Token customization failed");
        }
    }
}
