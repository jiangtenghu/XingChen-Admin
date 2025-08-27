package com.admin.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * CORS头清理过滤器
 * 用于移除重复的CORS头信息
 */
@Component
public class CorsCleanupFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            
            // 清理重复的CORS头
            cleanupDuplicateHeaders(response, "Access-Control-Allow-Origin");
            cleanupDuplicateHeaders(response, "Access-Control-Allow-Credentials");
            cleanupDuplicateHeaders(response, "Access-Control-Allow-Methods");
            cleanupDuplicateHeaders(response, "Access-Control-Allow-Headers");
            cleanupDuplicateHeaders(response, "Access-Control-Expose-Headers");
            cleanupDuplicateHeaders(response, "Access-Control-Max-Age");
        }));
    }
    
    private void cleanupDuplicateHeaders(ServerHttpResponse response, String headerName) {
        if (response.getHeaders().containsKey(headerName)) {
            // 获取第一个值，移除其他重复的值
            String firstValue = response.getHeaders().getFirst(headerName);
            response.getHeaders().remove(headerName);
            if (firstValue != null) {
                response.getHeaders().add(headerName, firstValue);
            }
        }
    }
    
    @Override
    public int getOrder() {
        // 在所有其他过滤器之后执行
        return Ordered.LOWEST_PRECEDENCE;
    }
}
