package com.admin.gateway.controller;

import com.admin.gateway.service.SwaggerAggregationService;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * API文档聚合控制器
 */
@RestController
public class ApiDocsController {

    @Autowired
    private SwaggerAggregationService swaggerAggregationService;

    /**
     * 获取聚合的API文档
     */
    @GetMapping(value = "/v3/api-docs/aggregated", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<JSONObject> getAggregatedApiDocs() {
        return swaggerAggregationService.getAggregatedApiDocs();
    }

    /**
     * 获取Swagger资源列表
     */
    @GetMapping(value = "/swagger-resources", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getSwaggerResources() {
        return swaggerAggregationService.getSwaggerResources();
    }

    /**
     * 服务发现状态
     */
    @GetMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getServicesStatus() {
        Map<String, Object> status = new java.util.HashMap<>();
        status.put("gateway", "http://localhost:8080");
        status.put("auth", "http://localhost:8081");
        status.put("user", "http://localhost:8082");
        status.put("system", "http://localhost:8083");
        status.put("nacos", "http://localhost:8848/nacos");
        status.put("aggregated_docs", "http://localhost:8080/doc.html");
        return status;
    }
}
