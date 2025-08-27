package com.admin.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * CORS跨域配置
 * 
 * 注意：只使用此配置，不要与Spring Gateway的globalcors同时使用
 * 以避免重复的CORS头导致浏览器报错
 */
@Configuration
public class CorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 允许的域名 - 支持开发和生产环境
        corsConfiguration.addAllowedOriginPattern("http://localhost:*");
        corsConfiguration.addAllowedOriginPattern("https://localhost:*");
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("https://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("http://0.0.0.0:*");
        corsConfiguration.addAllowedOriginPattern("https://0.0.0.0:*");
        // 支持局域网访问
        corsConfiguration.addAllowedOriginPattern("http://192.168.*.*:*");
        corsConfiguration.addAllowedOriginPattern("https://192.168.*.*:*");
        corsConfiguration.addAllowedOriginPattern("http://10.*.*.*:*");
        corsConfiguration.addAllowedOriginPattern("https://10.*.*.*:*");
        
        // 允许的请求方法
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("HEAD");
        corsConfiguration.addAllowedMethod("PATCH");
        
        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");
        
        // 暴露的响应头 - 添加更多常用头
        corsConfiguration.addExposedHeader("Content-Type");
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("X-Requested-With");
        corsConfiguration.addExposedHeader("Accept");
        corsConfiguration.addExposedHeader("Accept-Language");
        corsConfiguration.addExposedHeader("X-Total-Count");
        corsConfiguration.addExposedHeader("X-Pagination-Limit");
        corsConfiguration.addExposedHeader("X-Pagination-Offset");
        corsConfiguration.addExposedHeader("Content-Disposition");
        corsConfiguration.addExposedHeader("Cache-Control");
        corsConfiguration.addExposedHeader("Expires");
        
        // 允许携带凭据
        corsConfiguration.setAllowCredentials(true);
        
        // 预检请求的缓存时间 (1小时)
        corsConfiguration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        return new CorsWebFilter(source);
    }
}
