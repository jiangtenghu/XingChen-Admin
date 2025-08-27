package com.admin.auth.security;

import com.admin.auth.feign.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户认证提供者
 * 
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.info("开始认证用户: {}", username);

        try {
            // 调用用户服务获取用户信息
            Map<String, Object> userResult = userServiceClient.getUserByUsername(username);
            if (userResult == null || userResult.get("data") == null) {
                log.warn("用户不存在: {}", username);
                throw new BadCredentialsException("用户名或密码错误");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> userData = (Map<String, Object>) userResult.get("data");

            // 验证密码
            String storedPassword = (String) userData.get("password");
            if (!passwordEncoder.matches(password, storedPassword)) {
                log.warn("密码验证失败: {}", username);
                throw new BadCredentialsException("用户名或密码错误");
            }

            // 检查用户状态
            String status = (String) userData.get("status");
            if (!"0".equals(status)) {
                log.warn("用户账户已禁用: {}", username);
                throw new BadCredentialsException("账户已被禁用");
            }

            // 获取用户角色
            List<GrantedAuthority> authorities = new ArrayList<>();
            try {
                Long userId = Long.valueOf(userData.get("id").toString());
                Map<String, Object> rolesResult = userServiceClient.getUserRoles(userId);
                if (rolesResult != null && rolesResult.get("data") != null) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> rolesData = (Map<String, Object>) rolesResult.get("data");
                    @SuppressWarnings("unchecked")
                    List<String> roles = (List<String>) rolesData.get("roles");
                    if (roles != null) {
                        for (String role : roles) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("获取用户角色失败: {}", e.getMessage());
            }

            // 添加默认角色
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            log.info("用户认证成功: {}, 角色: {}", username, authorities);

            // 更新登录信息
            try {
                Long userId = Long.valueOf(userData.get("id").toString());
                userServiceClient.getUserById(userId); // 触发登录信息更新
            } catch (Exception e) {
                log.warn("更新登录信息失败: {}", e.getMessage());
            }

            return new UsernamePasswordAuthenticationToken(username, password, authorities);

        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            log.error("认证过程发生异常", e);
            throw new BadCredentialsException("认证失败");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
