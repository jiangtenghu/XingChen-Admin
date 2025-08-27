package com.admin.identity.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 * 实现多级缓存策略：本地缓存(Caffeine) + 分布式缓存(Redis)
 *
 * @author admin
 * @since 2024-01-15
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 本地缓存管理器 (L1 Cache)
     * 用于热点数据的快速访问
     */
    @Bean("localCacheManager")
    public CacheManager localCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 最大缓存数量
                .maximumSize(10000)
                // 写入后过期时间
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 访问后过期时间
                .expireAfterAccess(2, TimeUnit.MINUTES)
                // 开启统计
                .recordStats());
        return cacheManager;
    }

    /**
     * Redis缓存管理器 (L2 Cache)
     * 用于分布式缓存和较大数据集
     */
    @Bean("redisCacheManager")
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存过期时间 30分钟
                .entryTtl(Duration.ofMinutes(30))
                // 设置key序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                // 禁用缓存空值
                .disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * 用户信息本地缓存
     * 缓存用户基础信息，访问频率高
     */
    @Bean("userLocalCache")
    public Cache<String, Object> userLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 权限信息本地缓存
     * 缓存用户权限列表，访问频率非常高
     */
    @Bean("permissionLocalCache")
    public Cache<String, Object> permissionLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(3000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 组织架构本地缓存
     * 缓存组织树结构，变更频率低但访问频率高
     */
    @Bean("organizationLocalCache")
    public Cache<String, Object> organizationLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(2000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 租户信息本地缓存
     * 缓存租户配置信息，变更频率低
     */
    @Bean("tenantLocalCache")
    public Cache<String, Object> tenantLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 角色信息本地缓存
     * 缓存角色定义和权限映射
     */
    @Bean("roleLocalCache")
    public Cache<String, Object> roleLocalCache() {
        return Caffeine.newBuilder()
                .maximumSize(2000)
                .expireAfterWrite(8, TimeUnit.MINUTES)
                .expireAfterAccess(3, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
