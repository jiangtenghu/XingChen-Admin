# 统一身份管理系统 API 测试指南

## 概述

本文档提供统一身份管理系统的API测试指南，包括租户管理、组织架构、用户管理和权限管理的完整测试流程。

## 测试环境

- **服务地址**: http://localhost:8081
- **API前缀**: `/api/identity`
- **认证方式**: JWT Token
- **数据格式**: JSON

## 测试流程

### 1. 系统初始化

#### 1.1 获取管理员Token
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123",
  "tenantCode": "system"
}
```

响应示例：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 7200
  }
}
```

#### 1.2 系统概览
```bash
GET /api/identity/overview
Authorization: Bearer {token}
```

### 2. 租户管理测试

#### 2.1 创建租户
```bash
POST /api/identity/tenants
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "测试企业",
  "code": "test_company",
  "type": "ENTERPRISE",
  "contactPerson": "张三",
  "contactPhone": "13800138000",
  "contactEmail": "zhangsan@test.com",
  "industry": "软件开发",
  "scale": "MEDIUM",
  "region": "北京",
  "packageId": 3,
  "featureConfig": {
    "modules": ["user", "org", "role"],
    "features": {
      "sso": true,
      "mfa": true,
      "api": true,
      "export": true
    }
  },
  "remark": "测试租户"
}
```

#### 2.2 查询租户列表
```bash
GET /api/identity/tenants?pageNum=1&pageSize=10&name=测试&type=ENTERPRISE
Authorization: Bearer {token}
```

#### 2.3 查询租户详情
```bash
GET /api/identity/tenants/{tenantId}
Authorization: Bearer {token}
```

#### 2.4 更新租户信息
```bash
PUT /api/identity/tenants/{tenantId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "测试企业(更新)",
  "contactPerson": "李四",
  "contactPhone": "13900139000",
  "maxUsers": 500,
  "status": "ACTIVE"
}
```

#### 2.5 租户配额查询
```bash
GET /api/identity/tenants/{tenantId}/quota
Authorization: Bearer {token}
```

#### 2.6 租户续费
```bash
PUT /api/identity/tenants/{tenantId}/renew?months=12
Authorization: Bearer {token}
```

### 3. 组织架构测试

#### 3.1 创建根组织
```bash
POST /api/identity/tenants/{tenantId}/organizations
Authorization: Bearer {token}
Content-Type: application/json

{
  "orgName": "测试企业",
  "orgCode": "ROOT",
  "orgType": "COMPANY",
  "parentId": 0,
  "sortOrder": 0,
  "phone": "010-12345678",
  "email": "contact@test.com",
  "address": "北京市朝阳区"
}
```

#### 3.2 创建部门
```bash
POST /api/identity/tenants/{tenantId}/organizations
Authorization: Bearer {token}
Content-Type: application/json

{
  "orgName": "技术部",
  "orgCode": "TECH",
  "orgType": "DEPT",
  "parentId": 1,
  "sortOrder": 10,
  "phone": "010-12345679",
  "email": "tech@test.com",
  "orgConfig": {
    "allowSubOrg": true,
    "maxMembers": 100,
    "workTime": "09:00-18:00"
  }
}
```

#### 3.3 查询组织树
```bash
GET /api/identity/tenants/{tenantId}/organizations/tree
Authorization: Bearer {token}
```

#### 3.4 移动组织
```bash
PUT /api/identity/tenants/{tenantId}/organizations/{orgId}/move?newParentId=2&sortOrder=20
Authorization: Bearer {token}
```

#### 3.5 设置组织负责人
```bash
PUT /api/identity/tenants/{tenantId}/organizations/{orgId}/leader?leaderId=1&deputyLeaderId=2
Authorization: Bearer {token}
```

### 4. 用户管理测试

#### 4.1 创建用户
```bash
POST /api/identity/tenants/{tenantId}/users
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "zhangsan",
  "password": "Zhang123!",
  "confirmPassword": "Zhang123!",
  "nickname": "张三",
  "realName": "张三",
  "email": "zhangsan@test.com",
  "phone": "13800138001",
  "sex": "0",
  "employeeNo": "EMP001",
  "position": "高级工程师",
  "userType": "NORMAL",
  "primaryOrgId": 2,
  "organizations": [
    {
      "orgId": 2,
      "position": "高级工程师",
      "isPrimary": true
    }
  ],
  "roleIds": [1, 2],
  "extendedAttributes": {
    "level": "P6",
    "skills": ["Java", "Spring", "MySQL"]
  },
  "remark": "技术骨干"
}
```

#### 4.2 查询用户列表
```bash
GET /api/identity/tenants/{tenantId}/users?pageNum=1&pageSize=10&orgId=2&status=0
Authorization: Bearer {token}
```

#### 4.3 查询用户详情
```bash
GET /api/identity/tenants/{tenantId}/users/{userId}
Authorization: Bearer {token}
```

#### 4.4 更新用户信息
```bash
PUT /api/identity/tenants/{tenantId}/users/{userId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nickname": "张三(更新)",
  "position": "技术专家",
  "phone": "13800138002",
  "extendedAttributes": {
    "level": "P7",
    "skills": ["Java", "Spring", "MySQL", "Redis"]
  }
}
```

#### 4.5 分配用户到组织
```bash
POST /api/identity/tenants/{tenantId}/users/{userId}/organizations/{orgId}?position=技术顾问&isPrimary=false
Authorization: Bearer {token}
```

#### 4.6 重置用户密码
```bash
PUT /api/identity/tenants/{tenantId}/users/{userId}/reset-password?newPassword=NewPass123!
Authorization: Bearer {token}
```

#### 4.7 锁定/解锁用户
```bash
# 锁定用户
PUT /api/identity/tenants/{tenantId}/users/{userId}/lock?reason=安全审计
Authorization: Bearer {token}

# 解锁用户
PUT /api/identity/tenants/{tenantId}/users/{userId}/unlock
Authorization: Bearer {token}
```

### 5. 角色权限测试

#### 5.1 创建角色
```bash
POST /api/identity/tenants/{tenantId}/roles
Authorization: Bearer {token}
Content-Type: application/json

{
  "roleName": "技术经理",
  "roleKey": "tech_manager",
  "roleSort": 10,
  "dataScope": "3",
  "roleType": "CUSTOM",
  "effectiveTime": "2024-01-15T09:00:00",
  "expireTime": "2024-12-31T23:59:59",
  "roleConfig": {
    "inheritable": true,
    "assignable": true,
    "maxUsers": 50
  },
  "remark": "技术部门经理角色"
}
```

#### 5.2 查询权限树
```bash
GET /api/identity/permissions/tree?permissionType=MENU
Authorization: Bearer {token}
```

#### 5.3 分配权限给角色
```bash
POST /api/identity/tenants/{tenantId}/roles/{roleId}/permissions
Authorization: Bearer {token}
Content-Type: application/json

[1, 2, 3, 21, 31, 41]
```

#### 5.4 分配角色给用户
```bash
POST /api/identity/tenants/{tenantId}/users/{userId}/roles?orgId={orgId}
Authorization: Bearer {token}
Content-Type: application/json

[1, 2]
```

#### 5.5 查询用户权限
```bash
GET /api/identity/tenants/{tenantId}/users/{userId}/permissions
Authorization: Bearer {token}
```

#### 5.6 检查用户权限
```bash
GET /api/identity/tenants/{tenantId}/users/{userId}/check-permission?permission=user:add
Authorization: Bearer {token}
```

### 6. 批量操作测试

#### 6.1 批量导入用户
```bash
POST /api/identity/tenants/{tenantId}/users/import
Authorization: Bearer {token}
Content-Type: application/json

[
  {
    "username": "user001",
    "password": "User123!",
    "confirmPassword": "User123!",
    "realName": "用户001",
    "email": "user001@test.com",
    "primaryOrgId": 2
  },
  {
    "username": "user002",
    "password": "User123!",
    "confirmPassword": "User123!",
    "realName": "用户002",
    "email": "user002@test.com",
    "primaryOrgId": 2
  }
]
```

#### 6.2 批量转移用户
```bash
POST /api/identity/tenants/{tenantId}/users/transfer?userIds=1,2,3&fromOrgId=2&toOrgId=3
Authorization: Bearer {token}
```

#### 6.3 批量删除用户
```bash
DELETE /api/identity/tenants/{tenantId}/users/batch
Authorization: Bearer {token}
Content-Type: application/json

[1, 2, 3]
```

### 7. 数据导出测试

#### 7.1 导出用户数据
```bash
POST /api/identity/tenants/{tenantId}/users/export?format=excel
Authorization: Bearer {token}
Content-Type: application/json

{
  "orgId": 2,
  "status": "0",
  "createTimeStart": "2024-01-01T00:00:00",
  "createTimeEnd": "2024-12-31T23:59:59"
}
```

#### 7.2 导出组织架构
```bash
POST /api/identity/tenants/{tenantId}/organizations/export?format=excel
Authorization: Bearer {token}
```

### 8. 统计分析测试

#### 8.1 权限使用分析
```bash
GET /api/identity/tenants/{tenantId}/permissions/usage-analysis?days=30
Authorization: Bearer {token}
```

#### 8.2 权限审计
```bash
GET /api/identity/tenants/{tenantId}/permissions/audit?pageNum=1&pageSize=10&operation=LOGIN&startTime=2024-01-01T00:00:00
Authorization: Bearer {token}
```

## 错误处理测试

### 常见错误场景

1. **认证失败** (401)
```bash
GET /api/identity/tenants
# 不携带Authorization头
```

2. **权限不足** (403)
```bash
GET /api/identity/tenants
Authorization: Bearer {invalid_or_insufficient_token}
```

3. **参数验证失败** (400)
```bash
POST /api/identity/tenants
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "",  # 空名称
  "code": "invalid code"  # 无效编码格式
}
```

4. **资源不存在** (404)
```bash
GET /api/identity/tenants/99999
Authorization: Bearer {token}
```

5. **业务规则冲突** (409)
```bash
POST /api/identity/tenants
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "重复租户",
  "code": "existing_code"  # 已存在的编码
}
```

## 性能测试

### 并发测试脚本示例

```bash
# 使用Apache Bench进行并发测试
ab -n 1000 -c 10 -H "Authorization: Bearer {token}" \
   "http://localhost:8081/api/identity/tenants?pageNum=1&pageSize=10"

# 使用JMeter进行复杂场景测试
# 创建租户 -> 创建组织 -> 创建用户 -> 分配权限的完整流程测试
```

## 测试数据清理

测试完成后，清理测试数据：

```bash
# 删除测试用户
DELETE /api/identity/tenants/{tenantId}/users/{userId}

# 删除测试组织
DELETE /api/identity/tenants/{tenantId}/organizations/{orgId}

# 删除测试租户
DELETE /api/identity/tenants/{tenantId}
```

## 自动化测试

### Postman Collection

可以导入提供的Postman Collection进行自动化测试：

```json
{
  "info": {
    "name": "Identity Management API Tests",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Authentication",
      "item": [
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "header": [],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\",\n  \"tenantCode\": \"system\"\n}",
              "options": {
                "raw": {
                  "language": "json"
                }
              }
            },
            "url": {
              "raw": "{{baseUrl}}/api/auth/login",
              "host": ["{{baseUrl}}"],
              "path": ["api", "auth", "login"]
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8081",
      "type": "string"
    },
    {
      "key": "token",
      "value": "",
      "type": "string"
    }
  ]
}
```

## 注意事项

1. **环境隔离**: 测试环境与生产环境完全隔离
2. **数据备份**: 测试前备份重要数据
3. **权限控制**: 使用测试专用的管理员账号
4. **性能监控**: 监控测试过程中的系统性能指标
5. **日志记录**: 保留详细的测试日志用于问题排查

## 测试报告模板

```markdown
# 测试报告

## 测试概述
- 测试时间: 2024-01-15
- 测试环境: 开发环境
- 测试人员: 张三

## 测试结果
- 总测试用例: 50
- 通过用例: 48
- 失败用例: 2
- 通过率: 96%

## 失败用例
1. 批量导入用户 - 超大文件处理异常
2. 权限审计查询 - 时间范围过大导致超时

## 性能测试结果
- 平均响应时间: 150ms
- 95%响应时间: 300ms
- 最大并发用户: 100
- 错误率: 0.1%

## 建议
1. 优化批量导入功能的文件大小限制
2. 增加权限审计查询的分页和缓存机制
```
