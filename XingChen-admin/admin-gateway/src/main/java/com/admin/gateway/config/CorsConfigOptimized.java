package com.admin.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * 优化的CORS跨域配置 - 完全符合微服务设计原则
 * 
 * 设计原则：
 * 1. 单一职责：仅在API网关配置CORS，后端服务不参与
 * 2. 环境隔离：开发/测试/生产环境使用不同的CORS策略
 * 3. 安全优先：生产环境严格限制允许的源
 * 4. 可配置：通过配置文件控制CORS行为
 * 5. 可观测：提供详细的日志和监控
 */
@Configuration
public class CorsConfigOptimized {

    @Value("${cors.allowed-origins:}")
    private List<String> allowedOrigins;
    
    @Value("${cors.allowed-origin-patterns:}")
    private List<String> allowedOriginPatterns;
    
    @Value("${cors.max-age:3600}")
    private Long maxAge;
    
    @Value("${cors.allow-credentials:true}")
    private Boolean allowCredentials;

    private final Environment environment;

    public CorsConfigOptimized(Environment environment) {
        this.environment = environment;
    }

    /**
     * 开发环境CORS配置 - 宽松策略
     */
    @Bean
    @Profile({"dev", "development", "local"})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilterDev() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 开发环境允许本地和局域网访问
        corsConfiguration.addAllowedOriginPattern("http://localhost:*");
        corsConfiguration.addAllowedOriginPattern("https://localhost:*");
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("https://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("http://0.0.0.0:*");
        corsConfiguration.addAllowedOriginPattern("https://0.0.0.0:*");
        corsConfiguration.addAllowedOriginPattern("http://192.168.*.*:*");
        corsConfiguration.addAllowedOriginPattern("https://192.168.*.*:*");
        corsConfiguration.addAllowedOriginPattern("http://10.*.*.*:*");
        corsConfiguration.addAllowedOriginPattern("https://10.*.*.*:*");
        
        return createCorsWebFilter(corsConfiguration, "DEVELOPMENT");
    }

    /**
     * 测试环境CORS配置 - 中等严格策略
     */
    @Bean
    @Profile({"test", "testing", "stage", "staging"})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilterTest() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 测试环境允许特定域名
        corsConfiguration.addAllowedOriginPattern("https://*.test.company.com");
        corsConfiguration.addAllowedOriginPattern("https://*.staging.company.com");
        corsConfiguration.addAllowedOriginPattern("http://localhost:*");
        corsConfiguration.addAllowedOriginPattern("https://localhost:*");
        
        return createCorsWebFilter(corsConfiguration, "TESTING");
    }

    /**
     * 生产环境CORS配置 - 严格安全策略
     */
    @Bean
    @Profile({"prod", "production"})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilterProd() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 生产环境严格限制允许的源
        if (!allowedOrigins.isEmpty()) {
            corsConfiguration.setAllowedOrigins(allowedOrigins);
        } else if (!allowedOriginPatterns.isEmpty()) {
            corsConfiguration.setAllowedOriginPatterns(allowedOriginPatterns);
        } else {
            // 默认生产环境配置
            corsConfiguration.addAllowedOriginPattern("https://admin.company.com");
            corsConfiguration.addAllowedOriginPattern("https://www.company.com");
        }
        
        return createCorsWebFilter(corsConfiguration, "PRODUCTION");
    }

    /**
     * 默认CORS配置 - 用于未指定profile的情况
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilterDefault() {
        // 检查是否已有其他profile的bean
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            // 如果有活动profile，则不创建默认bean
            return null;
        }
        
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 默认配置（相对保守）
        corsConfiguration.addAllowedOriginPattern("http://localhost:*");
        corsConfiguration.addAllowedOriginPattern("https://localhost:*");
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("https://127.0.0.1:*");
        
        return createCorsWebFilter(corsConfiguration, "DEFAULT");
    }

    /**
     * 创建CORS Web Filter的通用方法
     */
    private CorsWebFilter createCorsWebFilter(CorsConfiguration corsConfiguration, String profile) {
        // 允许的请求方法
        corsConfiguration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"
        ));
        
        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");
        
        // 暴露的响应头
        corsConfiguration.setExposedHeaders(Arrays.asList(
            "Content-Type", "Authorization", "X-Requested-With", "Accept", 
            "Accept-Language", "X-Total-Count", "X-Pagination-Limit", 
            "X-Pagination-Offset", "Content-Disposition", "Cache-Control", 
            "Expires", "X-Request-ID", "X-Trace-ID"
        ));
        
        // 允许携带凭据
        corsConfiguration.setAllowCredentials(allowCredentials);
        
        // 预检请求的缓存时间
        corsConfiguration.setMaxAge(maxAge);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        // 记录CORS配置信息
        logCorsConfiguration(corsConfiguration, profile);
        
        return new CorsWebFilter(source);
    }

    /**
     * 记录CORS配置信息用于调试和监控
     */
    private void logCorsConfiguration(CorsConfiguration config, String profile) {
        System.out.println("===============================================");
        System.out.println("🌐 CORS Configuration Loaded - Profile: " + profile);
        System.out.println("📍 Allowed Origin Patterns: " + config.getAllowedOriginPatterns());
        System.out.println("📍 Allowed Origins: " + config.getAllowedOrigins());
        System.out.println("🔧 Allowed Methods: " + config.getAllowedMethods());
        System.out.println("🔐 Allow Credentials: " + config.getAllowCredentials());
        System.out.println("⏰ Max Age: " + config.getMaxAge() + " seconds");
        System.out.println("===============================================");
    }
}
