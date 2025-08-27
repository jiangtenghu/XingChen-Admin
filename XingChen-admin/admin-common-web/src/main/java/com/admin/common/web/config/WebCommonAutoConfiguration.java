package com.admin.common.web.config;

import com.admin.common.web.config.MybatisPlusConfig;
import com.admin.common.web.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * Web 服务公共模块自动配置
 */
@AutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients
@Import({
    MybatisPlusConfig.class,
    WebConfig.class,
    GlobalExceptionHandler.class
})
public class WebCommonAutoConfiguration {
    // Web 服务专用的自动配置
}
