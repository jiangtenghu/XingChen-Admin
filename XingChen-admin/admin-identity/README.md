# Admin-Identity 统一身份管理服务

## 📋 服务概述

统一身份管理服务(admin-identity)是基于微服务架构设计的企业级身份认证和权限管理解决方案。它将用户管理、租户管理、组织架构管理、角色权限管理统一整合，提供高内聚、低耦合的身份管理能力。

## 🎯 设计原则

- **单一职责原则**：专注于身份管理相关的所有业务功能
- **数据一致性**：统一数据库设计，本地事务保证强一致性
- **高性能缓存**：多级缓存策略，支持高并发访问
- **服务解耦**：通过Feign接口对外提供服务，降低服务间耦合
- **可扩展性**：支持水平扩展和业务功能扩展

## 🏗️ 架构设计

### 核心功能模块

```
📦 admin-identity (统一身份管理服务)
├── 🏢 租户管理 (Tenant Management)
│   ├── 租户注册、认证、配置
│   ├── 租户资源配额管理
│   ├── 租户套餐和功能开关
│   └── 租户数据隔离
├── 👥 用户管理 (User Management)
│   ├── 用户基础信息CRUD
│   ├── 用户状态管理
│   ├── 用户资料维护
│   └── 用户认证信息同步
├── 🏛️ 组织架构管理 (Organization Management)
│   ├── 组织树结构维护
│   ├── 部门层级管理
│   ├── 用户组织关联
│   └── 组织变更历史
├── 🔑 角色权限管理 (Role & Permission Management)
│   ├── 角色定义和管理
│   ├── 权限资源管理
│   ├── 用户角色分配
│   └── 数据权限控制
└── 🔗 关联关系管理 (Relations Management)
    ├── 用户组织关联
    ├── 用户角色关联
    ├── 角色权限关联
    └── 聚合业务操作
```

### 数据库设计

采用统一的数据库设计，包含以下核心表：

- `sys_tenant` - 租户信息表
- `sys_user` - 用户信息表
- `sys_organization` - 组织架构表
- `sys_role` - 角色信息表
- `sys_permission` - 权限信息表
- `sys_user_organization` - 用户组织关联表
- `sys_user_role` - 用户角色关联表
- `sys_role_permission` - 角色权限关联表

### 缓存架构

实现多级缓存策略：

```
🚀 多级缓存架构
├── L1: 本地缓存 (Caffeine)
│   ├── 用户信息缓存 (3分钟)
│   ├── 权限信息缓存 (5分钟)
│   ├── 组织架构缓存 (10分钟)
│   └── 租户信息缓存 (15分钟)
└── L2: 分布式缓存 (Redis)
    ├── 用户信息缓存 (30分钟)
    ├── 权限信息缓存 (60分钟)
    ├── 组织架构缓存 (120分钟)
    └── 租户信息缓存 (180分钟)
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7+
- Nacos 2.3+

### 数据库初始化

1. 创建数据库：
```sql
CREATE DATABASE admin_identity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```bash
mysql -h localhost -u root -p admin_identity < sql/admin_identity_schema.sql
```

### 服务启动

1. 启动依赖服务：
```bash
# 启动MySQL
# 启动Redis
# 启动Nacos
```

2. 配置应用参数：
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_identity
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

3. 启动服务：
```bash
mvn spring-boot:run
```

## 📡 API接口

### 核心API端点

| 功能模块 | 端点路径 | 描述 |
|---------|---------|------|
| 用户管理 | `/api/identity/users/**` | 用户CRUD、角色分配、组织关联等 |
| 租户管理 | `/api/identity/tenants/**` | 租户管理、配额控制、状态管理等 |
| 组织管理 | `/api/identity/organizations/**` | 组织架构、成员管理、层级维护等 |
| 角色管理 | `/api/identity/roles/**` | 角色定义、权限分配、数据范围等 |
| 权限管理 | `/api/identity/permissions/**` | 权限验证、菜单权限、数据权限等 |
| 聚合服务 | `/api/identity/aggregate/**` | 跨领域复合操作、批量处理等 |

### Feign客户端接口

其他微服务可通过Feign客户端调用身份管理服务：

```java
@FeignClient(name = "admin-identity")
public interface IdentityFeignClient {
    
    @GetMapping("/api/identity/users/{userId}/complete")
    Result<UserCompleteVO> getUserCompleteInfo(@PathVariable Long userId);
    
    @PostMapping("/api/identity/permissions/check")
    Result<Boolean> checkUserPermission(@RequestBody PermissionCheckRequest request);
    
    // 更多接口...
}
```

## 🔧 核心功能

### 1. 用户生命周期管理

```java
// 创建用户并分配组织角色
CreateUserWithOrgRoleRequest request = CreateUserWithOrgRoleRequest.builder()
    .tenantId(1L)
    .username("john.doe")
    .realName("John Doe")
    .orgId(10L)
    .roleIds(Arrays.asList(1L, 2L))
    .build();

UserVO user = identityAggregateService.createUserWithOrgAndRoles(request);
```

### 2. 权限验证

```java
// 单个权限验证
PermissionCheckRequest checkRequest = new PermissionCheckRequest();
checkRequest.setUserId(1L);
checkRequest.setPermission("system:user:edit");
checkRequest.setResourceId("USER_123");

Boolean hasPermission = permissionService.checkUserPermission(checkRequest);
```

### 3. 组织架构管理

```java
// 获取组织架构树
List<OrganizationTreeVO> orgTree = organizationService.getOrganizationTree(tenantId);

// 获取组织成员
OrganizationMembersVO members = identityAggregateService.getOrganizationMembers(orgId, true);
```

### 4. 数据权限控制

```java
// 获取用户数据权限范围
DataScopeVO dataScope = identityAggregateService.getUserDataScope(userId, "USER");

// 根据数据权限过滤查询
if ("1".equals(dataScope.getDataScope())) {
    // 全部数据权限
} else if ("5".equals(dataScope.getDataScope())) {
    // 仅本人数据权限
    query.eq("create_by", userId);
}
```

## 🎯 业务场景

### 1. 用户入职场景

```java
@Transactional
public UserVO handleUserOnboarding(UserOnboardingRequest request) {
    // 1. 创建用户
    UserVO user = userService.createUser(request.getUserInfo());
    
    // 2. 分配组织
    userService.assignUserToOrganization(user.getId(), request.getOrgAssignment());
    
    // 3. 分配角色
    userService.assignUserRoles(user.getId(), request.getRoleAssignment());
    
    // 4. 发送通知
    notificationService.sendWelcomeNotification(user);
    
    return user;
}
```

### 2. 用户转岗场景

```java
@Transactional
public boolean handleUserTransfer(UserTransferRequest request) {
    // 1. 验证转岗权限
    validateTransferPermission(request);
    
    // 2. 更新组织关联
    updateUserOrganization(request);
    
    // 3. 调整角色权限
    adjustUserRoles(request);
    
    // 4. 清理相关缓存
    clearUserCache(request.getUserId());
    
    // 5. 记录变更日志
    logUserTransfer(request);
    
    return true;
}
```

### 3. 权限动态验证场景

```java
@PreAuthorize("@permissionService.hasPermission(authentication.name, 'system:user:edit')")
public Result<Void> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
    // 业务逻辑
    return userService.updateUser(id, request);
}
```

## 📊 性能优化

### 1. 查询优化

- 使用联合索引优化多条件查询
- 分页查询避免深度分页
- 批量操作减少数据库交互

### 2. 缓存优化

- 热点数据本地缓存
- 权限信息分布式缓存
- 缓存预热和更新策略

### 3. 数据库优化

- 读写分离提升并发能力
- 连接池优化减少连接开销
- 慢查询监控和优化

## 🔒 安全设计

### 1. 数据安全

- 敏感信息加密存储
- SQL注入防护
- 数据脱敏处理

### 2. 访问控制

- 基于角色的访问控制(RBAC)
- 数据权限精细化控制
- API接口权限验证

### 3. 审计日志

- 用户操作日志记录
- 权限变更审计
- 异常访问监控

## 📈 监控指标

### 业务指标

- 用户活跃度
- 权限验证成功率
- 组织架构变更频率

### 性能指标

- API响应时间
- 缓存命中率
- 数据库连接数

### 系统指标

- CPU使用率
- 内存使用率
- 磁盘I/O

## 🚀 部署指南

### Docker部署

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/admin-identity.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes部署

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: admin-identity
spec:
  replicas: 3
  selector:
    matchLabels:
      app: admin-identity
  template:
    metadata:
      labels:
        app: admin-identity
    spec:
      containers:
      - name: admin-identity
        image: admin-identity:latest
        ports:
        - containerPort: 8083
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启 Pull Request

## 📄 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情

---

## 🔗 相关文档

- [架构设计文档](../doc/UNIFIED_IDENTITY_SERVICE_DESIGN.md)
- [数据库设计文档](../sql/admin_identity_schema.sql)
- [API接口文档](http://localhost:8083/identity/doc.html)
- [部署运维文档](docs/deployment.md)
