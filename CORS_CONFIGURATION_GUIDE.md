# 前端跨域配置指南

## 📋 配置概览

本项目采用**代理模式**解决跨域问题，同时提供直接访问的CORS支持。

### 🔄 跨域解决方案

#### 方案一：代理模式 (推荐)
- **前端**: 使用Vite代理，将 `/api` 请求转发到后端
- **优点**: 无跨域问题，开发体验好
- **配置**: `VITE_GLOB_API_URL=/api`

#### 方案二：直接访问 + CORS
- **前端**: 直接访问后端API
- **后端**: 配置CORS允许跨域访问
- **配置**: `VITE_GLOB_API_URL=http://localhost:8080`

## 🛠️ 前端配置

### 1. Vite代理配置 (`vite.config.mts`)

```typescript
export default defineConfig(async () => {
  return {
    application: {},
    vite: {
      server: {
        host: '0.0.0.0', // 允许外部访问
        port: 5666,
        cors: true, // 启用CORS
        proxy: {
          // API代理配置
          '/api': {
            target: 'http://localhost:8080',
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, ''),
            ws: true,
            secure: false,
            configure: (proxy, options) => {
              proxy.on('proxyReq', (proxyReq, req, res) => {
                proxyReq.setHeader('X-Forwarded-Proto', 'http');
                proxyReq.setHeader('X-Forwarded-Host', req.headers.host);
              });
            },
          },
          // 文件上传代理
          '/upload': {
            target: 'http://localhost:8080',
            changeOrigin: true,
            ws: false,
          },
          // WebSocket代理
          '/ws': {
            target: 'ws://localhost:8080',
            changeOrigin: true,
            ws: true,
          },
        },
      },
    },
  };
});
```

### 2. 环境变量配置 (`.env.development`)

```bash
# 端口号
VITE_PORT=5666

# 接口地址配置 - 使用代理模式
VITE_GLOB_API_URL=/api

# 其他配置
VITE_GLOB_UPLOAD_URL=/upload
VITE_GLOB_WS_URL=/ws
VITE_GLOB_API_TIMEOUT=60000
```

## 🚀 后端配置

### 1. Java CORS配置 (`CorsConfig.java`)

```java
@Configuration
public class CorsConfig {
    
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 允许的域名
        corsConfiguration.addAllowedOriginPattern("http://localhost:*");
        corsConfiguration.addAllowedOriginPattern("https://localhost:*");
        corsConfiguration.addAllowedOriginPattern("http://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("https://127.0.0.1:*");
        corsConfiguration.addAllowedOriginPattern("http://192.168.*.*:*");
        corsConfiguration.addAllowedOriginPattern("https://192.168.*.*:*");
        
        // 允许的请求方法
        corsConfiguration.addAllowedMethod("*");
        
        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");
        
        // 暴露的响应头
        corsConfiguration.addExposedHeader("Content-Type");
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("X-Total-Count");
        // ... 更多响应头
        
        // 允许携带凭据
        corsConfiguration.setAllowCredentials(true);
        
        // 预检请求缓存时间
        corsConfiguration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        return new CorsWebFilter(source);
    }
}
```

### 2. Spring Gateway CORS配置 (`application.yml`)

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOriginPatterns: 
              - "http://localhost:*"
              - "https://localhost:*"
              - "http://192.168.*.*:*"
            allowedMethods: 
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
              - HEAD
              - PATCH
            allowedHeaders: 
              - "*"
            exposedHeaders:
              - "Content-Type"
              - "Authorization"
              - "X-Total-Count"
            allowCredentials: true
            maxAge: 3600
```

## 🧪 测试跨域配置

### 1. 测试代理模式

```bash
# 启动前端服务
npm run dev

# 测试API调用 (通过代理)
curl http://localhost:5666/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 2. 测试直接访问

```bash
# 测试CORS预检请求
curl -X OPTIONS http://localhost:8080/auth/login \
  -H "Origin: http://localhost:5666" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type"

# 测试实际请求
curl -X POST http://localhost:8080/auth/login \
  -H "Origin: http://localhost:5666" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 🔧 常见问题解决

### 1. CORS预检请求失败
```
Access to XMLHttpRequest blocked by CORS policy: 
Response to preflight request doesn't pass access control check
```

**解决方案**:
- 检查后端CORS配置是否包含请求的Origin
- 确认OPTIONS请求方法已允许
- 验证请求头是否在allowedHeaders中

### 2. 凭据请求被拒绝
```
Access to XMLHttpRequest blocked by CORS policy: 
The value of the 'Access-Control-Allow-Credentials' header is false
```

**解决方案**:
- 设置 `allowCredentials: true`
- 不能使用通配符 `*` 作为allowedOrigins
- 使用 `allowedOriginPatterns` 代替

### 3. 代理请求失败
```
[vite] http proxy error: Error: connect ECONNREFUSED
```

**解决方案**:
- 确认后端服务已启动
- 检查代理target地址是否正确
- 验证网络连接

## 📊 配置对比

| 特性 | 代理模式 | 直接访问+CORS |
|------|----------|---------------|
| 开发体验 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| 配置复杂度 | ⭐⭐ | ⭐⭐⭐⭐ |
| 生产环境 | 需要额外配置 | 直接可用 |
| 调试难度 | ⭐⭐ | ⭐⭐⭐⭐ |
| 性能 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## 🚀 最佳实践

1. **开发环境**: 使用代理模式，简单可靠
2. **生产环境**: 使用CORS配置，性能更好
3. **安全考虑**: 生产环境限制allowedOrigins
4. **调试技巧**: 使用浏览器开发者工具查看Network标签
5. **监控**: 记录CORS相关的错误和警告

## 🔄 切换模式

### 切换到代理模式
```bash
# .env.development
VITE_GLOB_API_URL=/api
```

### 切换到直接访问
```bash
# .env.development
VITE_GLOB_API_URL=http://localhost:8080
```

重启前端服务使配置生效：
```bash
npm run dev
```
