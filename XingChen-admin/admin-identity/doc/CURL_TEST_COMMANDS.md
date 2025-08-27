# 用户注册和登录接口 cURL 测试命令

## 🚀 快速测试指南

### 环境配置
```bash
# 设置基础URL变量
export BASE_URL="http://localhost:8081"
export API_BASE="${BASE_URL}/api/identity"
```

## 📝 1. 用户注册接口测试

### 1.1 基础用户注册
```bash
curl -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "username": "testuser001",
    "password": "Test123!@#",
    "confirmPassword": "Test123!@#",
    "email": "testuser001@example.com",
    "nickname": "测试用户001",
    "realName": "张三",
    "phone": "13800138001"
  }' | jq '.'
```

### 1.2 完整信息注册
```bash
curl -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "email": "john.doe@company.com",
    "nickname": "John",
    "realName": "John Doe",
    "phone": "13912345678",
    "sex": "0",
    "birthday": "1990-01-15",
    "address": "北京市朝阳区",
    "employeeNo": "EMP001",
    "position": "软件工程师"
  }' | jq '.'
```

### 1.3 注册验证测试

#### 检查用户名是否存在
```bash
curl -X GET "${API_BASE}/auth/check-username?username=testuser001" \
  -H "Accept: application/json" | jq '.'
```

#### 检查邮箱是否存在
```bash
curl -X GET "${API_BASE}/auth/check-email?email=testuser001@example.com" \
  -H "Accept: application/json" | jq '.'
```

### 1.4 注册错误场景测试

#### 密码不匹配
```bash
curl -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "username": "testuser002",
    "password": "Test123!@#",
    "confirmPassword": "Different123!",
    "email": "testuser002@example.com"
  }' | jq '.'
```

#### 用户名重复
```bash
curl -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "username": "testuser001",
    "password": "Test123!@#",
    "confirmPassword": "Test123!@#",
    "email": "different@example.com"
  }' | jq '.'
```

#### 邮箱重复
```bash
curl -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "username": "differentuser",
    "password": "Test123!@#",
    "confirmPassword": "Test123!@#",
    "email": "testuser001@example.com"
  }' | jq '.'
```

#### 密码强度不够
```bash
curl -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "username": "weakpassuser",
    "password": "123456",
    "confirmPassword": "123456",
    "email": "weak@example.com"
  }' | jq '.'
```

## 🔐 2. 用户登录接口测试

### 2.1 账号密码登录

#### 使用用户名登录
```bash
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "testuser001",
    "password": "Test123!@#",
    "loginType": "PASSWORD",
    "tenantCode": "system",
    "rememberMe": false
  }' | jq '.'
```

#### 使用邮箱登录
```bash
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "testuser001@example.com",
    "password": "Test123!@#",
    "loginType": "PASSWORD",
    "tenantCode": "system",
    "rememberMe": true
  }' | jq '.'
```

#### 管理员登录
```bash
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "admin",
    "password": "admin123",
    "loginType": "PASSWORD",
    "tenantCode": "system"
  }' | jq '.'
```

### 2.2 登录错误场景测试

#### 用户名不存在
```bash
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "nonexistent",
    "password": "Test123!@#",
    "loginType": "PASSWORD"
  }' | jq '.'
```

#### 密码错误
```bash
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "testuser001",
    "password": "WrongPassword123!",
    "loginType": "PASSWORD"
  }' | jq '.'
```

#### 账号被禁用
```bash
# 先禁用账号，然后尝试登录
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "disableduser",
    "password": "Test123!@#",
    "loginType": "PASSWORD"
  }' | jq '.'
```

## 🔄 3. Token管理测试

### 3.1 Token验证
```bash
# 先登录获取token
TOKEN=$(curl -s -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "account": "testuser001",
    "password": "Test123!@#",
    "loginType": "PASSWORD"
  }' | jq -r '.data.accessToken')

# 验证token
curl -X POST "${API_BASE}/auth/verify-token" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Accept: application/json" | jq '.'
```

### 3.2 获取当前用户信息
```bash
curl -X GET "${API_BASE}/auth/user-info" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Accept: application/json" | jq '.'
```

### 3.3 刷新Token
```bash
# 获取refresh token
REFRESH_TOKEN=$(curl -s -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "account": "testuser001",
    "password": "Test123!@#",
    "loginType": "PASSWORD"
  }' | jq -r '.data.refreshToken')

# 刷新token
curl -X POST "${API_BASE}/auth/refresh" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d "{\"refreshToken\": \"${REFRESH_TOKEN}\"}" | jq '.'
```

### 3.4 用户登出
```bash
curl -X POST "${API_BASE}/auth/logout" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Accept: application/json" | jq '.'
```

## 📧 4. 验证码和密码重置测试

### 4.1 发送验证码

#### 发送邮箱验证码
```bash
curl -X POST "${API_BASE}/auth/send-code" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "type": "EMAIL",
    "target": "testuser001@example.com",
    "scene": "LOGIN"
  }' | jq '.'
```

#### 发送短信验证码
```bash
curl -X POST "${API_BASE}/auth/send-code" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "type": "SMS",
    "target": "13800138001",
    "scene": "RESET_PASSWORD"
  }' | jq '.'
```

### 4.2 找回密码
```bash
curl -X POST "${API_BASE}/auth/forgot-password" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "account": "testuser001@example.com",
    "type": "EMAIL"
  }' | jq '.'
```

### 4.3 重置密码
```bash
curl -X POST "${API_BASE}/auth/reset-password" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "resetToken": "reset_token_here",
    "newPassword": "NewPassword123!",
    "confirmPassword": "NewPassword123!"
  }' | jq '.'
```

### 4.4 修改密码
```bash
curl -X POST "${API_BASE}/auth/change-password" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{
    "oldPassword": "Test123!@#",
    "newPassword": "NewPassword123!",
    "confirmPassword": "NewPassword123!"
  }' | jq '.'
```

## 🧪 5. 批量测试脚本

### 5.1 完整流程测试
```bash
#!/bin/bash

BASE_URL="http://localhost:8081"
API_BASE="${BASE_URL}/api/identity"

echo "=== 用户注册和登录完整流程测试 ==="

# 1. 注册用户
echo "1. 注册新用户..."
REGISTER_RESULT=$(curl -s -X POST "${API_BASE}/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "flowtest001",
    "password": "FlowTest123!",
    "confirmPassword": "FlowTest123!",
    "email": "flowtest001@example.com",
    "nickname": "流程测试用户",
    "realName": "测试用户"
  }')

echo "注册结果：$REGISTER_RESULT"

# 2. 检查注册是否成功
if echo "$REGISTER_RESULT" | jq -e '.code == 200' > /dev/null; then
    echo "✅ 用户注册成功"
else
    echo "❌ 用户注册失败"
    exit 1
fi

# 3. 登录用户
echo "2. 用户登录..."
LOGIN_RESULT=$(curl -s -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "account": "flowtest001",
    "password": "FlowTest123!",
    "loginType": "PASSWORD"
  }')

echo "登录结果：$LOGIN_RESULT"

# 4. 提取token
TOKEN=$(echo "$LOGIN_RESULT" | jq -r '.data.accessToken')
if [ "$TOKEN" != "null" ] && [ "$TOKEN" != "" ]; then
    echo "✅ 用户登录成功，Token: ${TOKEN:0:20}..."
else
    echo "❌ 用户登录失败"
    exit 1
fi

# 5. 验证token
echo "3. 验证Token..."
VERIFY_RESULT=$(curl -s -X POST "${API_BASE}/auth/verify-token" \
  -H "Authorization: Bearer ${TOKEN}")

echo "验证结果：$VERIFY_RESULT"

# 6. 获取用户信息
echo "4. 获取用户信息..."
USER_INFO=$(curl -s -X GET "${API_BASE}/auth/user-info" \
  -H "Authorization: Bearer ${TOKEN}")

echo "用户信息：$USER_INFO"

# 7. 登出
echo "5. 用户登出..."
LOGOUT_RESULT=$(curl -s -X POST "${API_BASE}/auth/logout" \
  -H "Authorization: Bearer ${TOKEN}")

echo "登出结果：$LOGOUT_RESULT"

echo "=== 完整流程测试结束 ==="
```

### 5.2 压力测试脚本
```bash
#!/bin/bash

# 并发登录测试
echo "=== 并发登录压力测试 ==="

BASE_URL="http://localhost:8081"
API_BASE="${BASE_URL}/api/identity"

# 创建多个并发登录请求
for i in {1..10}; do
    (
        echo "并发请求 $i 开始..."
        RESULT=$(curl -s -X POST "${API_BASE}/auth/login" \
          -H "Content-Type: application/json" \
          -d '{
            "account": "testuser001",
            "password": "Test123!@#",
            "loginType": "PASSWORD"
          }')
        
        if echo "$RESULT" | jq -e '.code == 200' > /dev/null; then
            echo "✅ 并发请求 $i 成功"
        else
            echo "❌ 并发请求 $i 失败"
        fi
    ) &
done

wait
echo "=== 并发测试完成 ==="
```

## 📊 6. 响应示例

### 6.1 成功注册响应
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 123,
    "username": "testuser001",
    "email": "testuser001@example.com",
    "needEmailVerification": true,
    "needAdminApproval": false,
    "message": "注册成功，请查收邮箱验证邮件",
    "nextStep": "请查看邮箱并点击验证链接"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 6.2 成功登录响应
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userInfo": {
      "id": 123,
      "username": "testuser001",
      "nickname": "测试用户001",
      "email": "testuser001@example.com",
      "avatar": null,
      "userType": "NORMAL",
      "status": "0"
    },
    "permissions": ["user:view", "user:edit"],
    "roles": ["USER"],
    "loginTime": "2024-01-15T10:30:00",
    "loginIp": "192.168.1.100",
    "firstLogin": false
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 6.3 错误响应示例
```json
{
  "code": 400,
  "message": "注册失败：用户名已存在",
  "data": null,
  "timestamp": "2024-01-15T10:30:00"
}
```

## 🛠️ 7. 调试技巧

### 7.1 查看详细响应
```bash
# 显示HTTP头信息
curl -v -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"account": "testuser001", "password": "Test123!@#"}'

# 保存响应到文件
curl -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"account": "testuser001", "password": "Test123!@#"}' \
  -o login_response.json

# 格式化JSON输出
curl -s -X POST "${API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"account": "testuser001", "password": "Test123!@#"}' | \
  python -m json.tool
```

### 7.2 环境变量使用
```bash
# 在 ~/.bashrc 或 ~/.zshrc 中设置
export IDENTITY_API_BASE="http://localhost:8081/api/identity"
export TEST_USERNAME="testuser001"
export TEST_PASSWORD="Test123!@#"

# 使用环境变量
curl -X POST "${IDENTITY_API_BASE}/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"account\": \"${TEST_USERNAME}\", \"password\": \"${TEST_PASSWORD}\"}"
```

这些测试命令覆盖了用户注册和登录的所有主要功能，包括正常流程和异常情况。您可以根据实际需要选择相应的测试命令进行验证。
