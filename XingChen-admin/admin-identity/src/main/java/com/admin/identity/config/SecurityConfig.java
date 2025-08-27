package com.admin.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 简化安全配置类
 * 信任网关认证，所有请求都允许通过，通过HTTP头获取用户信息
 *
 * @author admin
 * @since 2024-08-27
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（微服务内部调用）
            .csrf(AbstractHttpConfigurer::disable)
            
            // 配置请求权限 - 信任网关，所有请求都允许
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // 所有请求都允许，因为网关已经处理认证
            )
            
            // 禁用session，使用无状态
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            )
            
            // 禁用frame options（为了方便调试）
            .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }
}
