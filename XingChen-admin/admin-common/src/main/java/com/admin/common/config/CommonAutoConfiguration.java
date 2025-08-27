package com.admin.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 公共模块自动配置
 */
@AutoConfiguration
@Import({
    RedisConfig.class,
    SwaggerConfig.class,
    MonitoringConfig.class
})
public class CommonAutoConfiguration {
    // 自动配置所有公共组件
}
