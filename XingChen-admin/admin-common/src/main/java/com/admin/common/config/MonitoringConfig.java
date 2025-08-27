package com.admin.common.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 监控配置
 */
@Configuration
public class MonitoringConfig {

    /**
     * 自定义监控指标
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer() {
        return registry -> registry.config().commonTags("application", "xingchen-admin");
    }

    /**
     * 启用 @Timed 注解支持
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
