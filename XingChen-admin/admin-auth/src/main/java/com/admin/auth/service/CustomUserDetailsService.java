package com.admin.auth.service;

import com.admin.auth.feign.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义用户详情服务
 * 集成用户服务进行真实用户认证
 * 
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("加载用户详情: {}", username);

        try {
            // 调用用户服务获取用户信息
            Map<String, Object> userResult = userServiceClient.getUserByUsername(username);
            if (userResult == null || userResult.get("data") == null) {
                log.warn("用户不存在: {}", username);
                throw new UsernameNotFoundException("用户不存在: " + username);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> userData = (Map<String, Object>) userResult.get("data");

            // 检查用户状态
            String status = (String) userData.get("status");
            if (!"0".equals(status)) {
                log.warn("用户账户已禁用: {}", username);
                throw new UsernameNotFoundException("账户已被禁用: " + username);
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

            String password = (String) userData.get("password");
            boolean enabled = "0".equals(status);
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;

            log.info("用户详情加载成功: {}, 角色: {}", username, authorities);

            return new User(username, password, enabled, accountNonExpired, 
                          credentialsNonExpired, accountNonLocked, authorities);

        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("加载用户详情失败", e);
            throw new UsernameNotFoundException("加载用户详情失败: " + username, e);
        }
    }
}
