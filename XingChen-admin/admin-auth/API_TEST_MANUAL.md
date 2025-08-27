# OAuth 2.1 + JWT 认证系统接口测试手册

## 📋 测试概述

本文档提供了完整的OAuth 2.1 + JWT认证系统接口测试方案，包括手动测试和自动化测试。

## 🔧 测试环境准备

### 1. 启动服务
```bash
cd XingChen-admin/admin-auth
mvn spring-boot:run
```

### 2. 服务健康检查
```bash
curl -X GET http://localhost:8081/actuator/health
```

预期响应：
```json
{
  "status": "UP"
}
```

## 🎯 接口功能测试

### 1. 用户登录接口

#### 1.1 成功登录
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

预期响应：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "uuid-refresh-token",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userInfo": {
      "userId": "1",
      "username": "admin",
      "nickname": "管理员",
      "email": "admin@example.com",
      "roles": ["SUPER_ADMIN", "ADMIN", "USER"]
    }
  }
}
```

#### 1.2 登录失败 - 用户名为空
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "password": "admin123"
  }'
```

预期响应：
```json
{
  "code": 400,
  "message": "用户名或密码不能为空"
}
```

#### 1.3 登录失败 - 密码错误
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "wrongpassword"
  }'
```

预期响应：
```json
{
  "code": 401,
  "message": "用户名或密码错误"
}
```

### 2. OAuth 2.1 标准令牌端点

#### 2.1 密码模式获取令牌
```bash
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=web-admin-client&client_secret=web-admin-secret&username=admin&password=admin123"
```

预期响应：
```json
{
  "code": 200,
  "message": "令牌获取成功",
  "data": {
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refresh_token": "uuid-refresh-token",
    "token_type": "Bearer",
    "expires_in": 7200
  }
}
```

#### 2.2 刷新令牌
```bash
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=refresh_token&client_id=web-admin-client&client_secret=web-admin-secret&refresh_token=YOUR_REFRESH_TOKEN"
```

#### 2.3 客户端凭证模式
```bash
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=service-client&client_secret=service-secret&scope=service"
```

### 3. 令牌管理接口

#### 3.1 令牌撤销
```bash
curl -X POST http://localhost:8081/oauth2/revoke \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "token=YOUR_ACCESS_TOKEN&client_id=web-admin-client&client_secret=web-admin-secret"
```

预期响应：
```json
{
  "code": 200,
  "message": "令牌撤销成功"
}
```

#### 3.2 令牌自省
```bash
curl -X POST http://localhost:8081/oauth2/introspect \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "token=YOUR_ACCESS_TOKEN&client_id=web-admin-client&client_secret=web-admin-secret"
```

预期响应：
```json
{
  "code": 200,
  "message": "令牌检查成功",
  "data": {
    "active": true,
    "client_id": "web-admin-client",
    "username": "admin",
    "scope": "openid profile read write",
    "exp": 1642593600,
    "iat": 1642586400
  }
}
```

### 4. 用户信息接口

#### 4.1 获取当前用户信息
```bash
curl -X GET http://localhost:8081/auth/me \
  -H "X-User-Id: 1"
```

预期响应：
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "userId": "1",
    "username": "admin",
    "nickname": "管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "roles": ["SUPER_ADMIN", "ADMIN", "USER"]
  }
}
```

### 5. 用户注销接口

```bash
curl -X POST http://localhost:8081/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

预期响应：
```json
{
  "code": 200,
  "message": "注销成功"
}
```

## 🧪 自动化测试脚本

### 完整流程测试脚本

```bash
#!/bin/bash

# 测试配置
BASE_URL="http://localhost:8081"
USERNAME="admin"
PASSWORD="admin123"

echo "🚀 开始OAuth 2.1 + JWT认证系统测试"

# 1. 健康检查
echo "📋 1. 健康检查"
health_response=$(curl -s -X GET $BASE_URL/actuator/health)
echo "健康检查响应: $health_response"

# 2. 用户登录
echo "📋 2. 用户登录测试"
login_response=$(curl -s -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}")

echo "登录响应: $login_response"

# 提取访问令牌
access_token=$(echo $login_response | jq -r '.data.accessToken // empty')
refresh_token=$(echo $login_response | jq -r '.data.refreshToken // empty')

if [ -n "$access_token" ] && [ "$access_token" != "null" ]; then
    echo "✅ 登录成功，获得访问令牌"
else
    echo "❌ 登录失败"
    exit 1
fi

# 3. 获取用户信息
echo "📋 3. 获取用户信息测试"
user_info_response=$(curl -s -X GET $BASE_URL/auth/me \
  -H "X-User-Id: 1")
echo "用户信息响应: $user_info_response"

# 4. OAuth 2.1 密码模式测试
echo "📋 4. OAuth 2.1 密码模式测试"
oauth_response=$(curl -s -X POST $BASE_URL/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=web-admin-client&client_secret=web-admin-secret&username=$USERNAME&password=$PASSWORD")
echo "OAuth令牌响应: $oauth_response"

# 5. 令牌自省测试
if [ -n "$access_token" ]; then
    echo "📋 5. 令牌自省测试"
    introspect_response=$(curl -s -X POST $BASE_URL/oauth2/introspect \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "token=$access_token&client_id=web-admin-client&client_secret=web-admin-secret")
    echo "令牌自省响应: $introspect_response"
fi

# 6. 刷新令牌测试
if [ -n "$refresh_token" ]; then
    echo "📋 6. 刷新令牌测试"
    refresh_response=$(curl -s -X POST $BASE_URL/oauth2/token \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "grant_type=refresh_token&client_id=web-admin-client&client_secret=web-admin-secret&refresh_token=$refresh_token")
    echo "刷新令牌响应: $refresh_response"
fi

# 7. 用户注销
if [ -n "$access_token" ]; then
    echo "📋 7. 用户注销测试"
    logout_response=$(curl -s -X POST $BASE_URL/auth/logout \
      -H "Authorization: Bearer $access_token")
    echo "注销响应: $logout_response"
fi

echo "🎉 测试完成！"
```

## 📊 性能测试

### 1. 并发登录测试

```bash
# 使用Apache Bench进行并发测试
ab -n 1000 -c 10 -p login_data.json -T application/json http://localhost:8081/auth/login

# login_data.json内容：
# {"username":"admin","password":"admin123"}
```

### 2. 令牌验证性能测试

```bash
# 测试令牌自省性能
ab -n 1000 -c 10 -p token_data.txt -T application/x-www-form-urlencoded http://localhost:8081/oauth2/introspect

# token_data.txt内容：
# token=YOUR_ACCESS_TOKEN&client_id=web-admin-client&client_secret=web-admin-secret
```

## 🔍 错误场景测试

### 1. 参数验证测试

```bash
# 测试缺少必需参数
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=web-admin-client"

# 预期：400 Bad Request
```

### 2. 客户端认证测试

```bash
# 测试无效客户端
curl -X POST http://localhost:8081/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=invalid-client&client_secret=invalid-secret"

# 预期：401 Unauthorized
```

### 3. 令牌格式测试

```bash
# 测试无效令牌格式
curl -X POST http://localhost:8081/oauth2/introspect \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "token=invalid-token&client_id=web-admin-client&client_secret=web-admin-secret"

# 预期：返回 active: false
```

## 📈 测试报告模板

### 测试结果记录表

| 测试项目 | 测试方法 | 预期结果 | 实际结果 | 状态 | 备注 |
|---------|---------|---------|---------|------|------|
| 用户登录 | POST /auth/login | 200 + Token | ✅ | 通过 | - |
| 获取用户信息 | GET /auth/me | 200 + UserInfo | ✅ | 通过 | - |
| OAuth密码模式 | POST /oauth2/token | 200 + Token | ✅ | 通过 | - |
| 令牌刷新 | POST /oauth2/token | 200 + NewToken | ✅ | 通过 | - |
| 令牌撤销 | POST /oauth2/revoke | 200 + Success | ✅ | 通过 | - |
| 令牌自省 | POST /oauth2/introspect | 200 + TokenInfo | ✅ | 通过 | - |
| 用户注销 | POST /auth/logout | 200 + Success | ✅ | 通过 | - |

### 性能测试结果

- **并发用户数**: 10
- **总请求数**: 1000
- **平均响应时间**: < 100ms
- **成功率**: > 99%
- **错误率**: < 1%

## 🎯 测试检查清单

### 功能测试
- [ ] 用户登录成功
- [ ] 用户登录失败处理
- [ ] 用户信息获取
- [ ] 用户注销
- [ ] OAuth 2.1 密码模式
- [ ] OAuth 2.1 客户端凭证模式
- [ ] 令牌刷新
- [ ] 令牌撤销
- [ ] 令牌自省

### 安全测试
- [ ] 密码错误处理
- [ ] 客户端认证
- [ ] 令牌有效性验证
- [ ] HTTPS支持（生产环境）
- [ ] CORS配置

### 性能测试
- [ ] 并发请求处理
- [ ] 响应时间测试
- [ ] 内存使用情况
- [ ] CPU使用情况

### 兼容性测试
- [ ] 不同客户端类型
- [ ] 不同浏览器
- [ ] 移动设备兼容性

这份测试手册提供了全面的接口功能验证方案，确保OAuth 2.1 + JWT认证系统的各项功能都能正常工作。
