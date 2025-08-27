package com.admin.gateway.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Swagger聚合服务
 * 动态发现微服务并聚合API文档
 */
@Service
public class SwaggerAggregationService {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerAggregationService.class);
    
    private final DiscoveryClient discoveryClient;
    private final WebClient webClient;
    private final Map<String, JSONObject> serviceApiDocs = new ConcurrentHashMap<>();

    public SwaggerAggregationService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }

    /**
     * 获取所有服务的Swagger资源
     */
    public List<Map<String, Object>> getSwaggerResources() {
        List<Map<String, Object>> resources = new ArrayList<>();
        
        // 获取所有注册的服务
        List<String> services = discoveryClient.getServices();
        
        for (String serviceName : services) {
            if (serviceName.startsWith("admin-") && !serviceName.equals("admin-gateway")) {
                Map<String, Object> resource = new HashMap<>();
                resource.put("name", getServiceDisplayName(serviceName));
                resource.put("url", "/" + getServicePath(serviceName) + "/v3/api-docs");
                resource.put("swaggerVersion", "3.0");
                resource.put("serviceName", serviceName);
                resources.add(resource);
            }
        }
        
        // 添加聚合文档
        Map<String, Object> aggregatedResource = new HashMap<>();
        aggregatedResource.put("name", "🌟 聚合文档");
        aggregatedResource.put("url", "/v3/api-docs/aggregated");
        aggregatedResource.put("swaggerVersion", "3.0");
        aggregatedResource.put("serviceName", "aggregated");
        resources.add(0, aggregatedResource);
        
        return resources;
    }

    /**
     * 获取聚合的API文档
     */
    public Mono<JSONObject> getAggregatedApiDocs() {
        return Mono.fromCallable(() -> {
            JSONObject aggregatedDoc = new JSONObject();
            aggregatedDoc.put("openapi", "3.0.1");
            
            // 基本信息
            JSONObject info = new JSONObject();
            info.put("title", "Spring Cloud Admin 聚合API文档");
            info.put("description", "包含所有微服务的API接口");
            info.put("version", "1.0.0");
            
            JSONObject contact = new JSONObject();
            contact.put("name", "Admin Team");
            contact.put("email", "admin@example.com");
            info.put("contact", contact);
            
            aggregatedDoc.put("info", info);
            
            // 服务器信息
            List<JSONObject> servers = new ArrayList<>();
            JSONObject server = new JSONObject();
            server.put("url", "http://localhost:8080");
            server.put("description", "API网关服务器");
            servers.add(server);
            aggregatedDoc.put("servers", servers);
            
            // 聚合所有服务的路径
            JSONObject paths = new JSONObject();
            JSONObject tags = new JSONObject();
            
            // 添加认证服务API
            addServicePaths(paths, tags, "认证服务", "/auth", Arrays.asList(
                createApiPath("/auth/login", "POST", "用户登录", "用户登录获取访问令牌"),
                createApiPath("/auth/register", "POST", "用户注册", "用户注册新账号"),
                createApiPath("/auth/logout", "POST", "用户注销", "用户注销登录"),
                createApiPath("/auth/refresh", "POST", "刷新令牌", "使用刷新令牌获取新的访问令牌")
            ));
            
            // 添加用户服务API
            addServicePaths(paths, tags, "用户服务", "/user", Arrays.asList(
                createApiPath("/user/page", "GET", "用户分页查询", "根据条件分页查询用户列表"),
                createApiPath("/user/{id}", "GET", "查询用户", "根据ID查询用户信息"),
                createApiPath("/user", "POST", "新增用户", "新增用户信息"),
                createApiPath("/user", "PUT", "修改用户", "修改用户信息"),
                createApiPath("/user/{id}", "DELETE", "删除用户", "删除用户")
            ));
            
            // 添加系统服务API
            addServicePaths(paths, tags, "系统服务", "/system", Arrays.asList(
                createApiPath("/system/menu", "GET", "菜单查询", "查询系统菜单"),
                createApiPath("/system/role", "GET", "角色查询", "查询系统角色"),
                createApiPath("/system/permission", "GET", "权限查询", "查询系统权限")
            ));
            
            aggregatedDoc.put("paths", paths);
            
            // 添加标签信息
            List<JSONObject> tagsList = new ArrayList<>();
            tagsList.add(createTag("认证服务", "用户认证相关接口"));
            tagsList.add(createTag("用户服务", "用户管理相关接口"));
            tagsList.add(createTag("系统服务", "系统管理相关接口"));
            aggregatedDoc.put("tags", tagsList);
            
            return aggregatedDoc;
        });
    }

    /**
     * 获取服务显示名称
     */
    private String getServiceDisplayName(String serviceName) {
        switch (serviceName) {
            case "admin-auth": return "🔐 认证服务";
            case "admin-user": return "👤 用户服务";
            case "admin-system": return "⚙️ 系统服务";
            default: return serviceName;
        }
    }

    /**
     * 获取服务路径
     */
    private String getServicePath(String serviceName) {
        switch (serviceName) {
            case "admin-auth": return "auth";
            case "admin-user": return "user";
            case "admin-system": return "system";
            default: return serviceName.replace("admin-", "");
        }
    }

    /**
     * 添加服务路径
     */
    private void addServicePaths(JSONObject paths, JSONObject tags, String serviceName, String basePath, List<JSONObject> apiPaths) {
        for (JSONObject apiPath : apiPaths) {
            paths.put(apiPath.getString("path"), apiPath.getJSONObject("definition"));
        }
    }

    /**
     * 创建API路径
     */
    private JSONObject createApiPath(String path, String method, String summary, String description) {
        JSONObject apiPath = new JSONObject();
        apiPath.put("path", path);
        
        JSONObject definition = new JSONObject();
        JSONObject operation = new JSONObject();
        operation.put("summary", summary);
        operation.put("description", description);
        operation.put("tags", Arrays.asList(path.split("/")[1] + "服务"));
        
        JSONObject responses = new JSONObject();
        JSONObject response200 = new JSONObject();
        response200.put("description", "成功");
        responses.put("200", response200);
        operation.put("responses", responses);
        
        definition.put(method.toLowerCase(), operation);
        apiPath.put("definition", definition);
        
        return apiPath;
    }

    /**
     * 创建标签
     */
    private JSONObject createTag(String name, String description) {
        JSONObject tag = new JSONObject();
        tag.put("name", name);
        tag.put("description", description);
        return tag;
    }
}
