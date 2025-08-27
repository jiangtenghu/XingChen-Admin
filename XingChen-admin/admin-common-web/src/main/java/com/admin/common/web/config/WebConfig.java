package com.admin.common.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置 - 仅用于 Web 服务
 * 
 * 注意：在微服务架构中，CORS应该只在API网关配置，
 * 后端服务不需要配置CORS以避免重复头信息
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 跨域配置 - 已禁用，由API网关统一处理
     */
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedOriginPatterns("*")
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //             .allowedHeaders("*")
    //             .allowCredentials(true)
    //             .maxAge(3600);
    // }
}
