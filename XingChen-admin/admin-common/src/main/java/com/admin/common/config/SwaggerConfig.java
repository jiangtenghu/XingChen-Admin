package com.admin.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置
 */
@Configuration
@ConditionalOnClass(name = "io.swagger.v3.oas.models.OpenAPI")
public class SwaggerConfig {

    @Value("${spring.application.name:Spring Cloud Admin}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " API 文档")
                        .description("Spring Cloud 分布式微服务管理系统")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Admin Team")
                                .email("admin@example.com")
                                .url("https://github.com/admin/xingchen-admin"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
