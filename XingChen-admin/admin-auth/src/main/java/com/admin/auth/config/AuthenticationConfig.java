package com.admin.auth.config;

import com.admin.auth.security.UserAuthenticationProvider;
import com.admin.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 认证配置
 * 
 * @author admin
 * @since 2024-08-27
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 数据库认证提供者
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        // 使用数据库认证提供者和自定义认证提供者
        return new ProviderManager(daoAuthenticationProvider(), userAuthenticationProvider);
    }

    /**
     * 主要的用户详情服务（使用真实用户数据）
     */
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    /**
     * 备用用户详情服务（用于测试和管理）
     */
    @Bean("memoryUserDetailsService")
    public UserDetailsService memoryUserDetailsService() {
        // 创建一个默认的管理员用户用于OAuth授权码流程测试
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
