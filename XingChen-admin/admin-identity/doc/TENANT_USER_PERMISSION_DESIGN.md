# 租户、组织架构、用户管理及权限管理详细设计文档

## 1. 系统概述

### 1.1 设计目标
构建一个支持多租户的统一身份管理系统，提供完整的用户管理、组织架构管理和细粒度权限控制功能。

### 1.2 核心功能
- **租户管理**: 支持多租户隔离，提供租户生命周期管理
- **组织架构**: 灵活的树形组织结构，支持多层级部门管理
- **用户管理**: 完整的用户生命周期管理，支持多种用户类型
- **权限管理**: 基于RBAC模型的细粒度权限控制，支持数据权限

## 2. 架构设计

### 2.1 整体架构
```
┌─────────────────────────────────────────────────────┐
│                前端应用层                             │
├─────────────────────────────────────────────────────┤
│              API网关层 (admin-gateway)               │
├─────────────────────────────────────────────────────┤
│          身份认证服务 (admin-auth)                    │
├─────────────────────────────────────────────────────┤
│          身份管理服务 (admin-identity)                │
├─────────────────────────────────────────────────────┤
│          系统管理服务 (admin-system)                  │
├─────────────────────────────────────────────────────┤
│              数据持久层 (MySQL)                      │
└─────────────────────────────────────────────────────┘
```

### 2.2 服务职责划分

#### admin-identity (身份管理服务)
- 租户管理
- 组织架构管理
- 用户基础信息管理
- 权限资源定义

#### admin-auth (认证服务)
- 用户认证授权
- Token管理
- 会话管理
- 权限验证

#### admin-system (系统管理服务)
- 系统配置管理
- 日志管理
- 监控管理

### 2.3 技术栈选型

#### 后端技术栈
- **基础框架**: Spring Boot 3.x + Spring Cloud 2023.x
- **数据访问**: MyBatis-Plus 3.5.x
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.x + Caffeine (本地缓存)
- **服务注册发现**: Nacos 2.x
- **配置中心**: Nacos Config
- **网关**: Spring Cloud Gateway
- **认证授权**: Spring Security + JWT
- **文档**: SpringDoc (OpenAPI 3)
- **监控**: Micrometer + Prometheus
- **链路追踪**: Spring Cloud Sleuth + Zipkin

#### 开发工具
- **代码生成**: MyBatis-Plus Generator
- **参数校验**: Hibernate Validator
- **工具库**: Hutool 5.x
- **JSON处理**: Jackson 2.x
- **密码加密**: BCrypt

## 3. 数据模型设计

### 3.1 租户模型 (sys_tenant)

#### 3.1.1 核心字段设计
```sql
CREATE TABLE sys_tenant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
    name VARCHAR(64) NOT NULL COMMENT '租户名称',
    code VARCHAR(32) NOT NULL UNIQUE COMMENT '租户编码',
    type VARCHAR(16) NOT NULL DEFAULT 'STANDARD' COMMENT '租户类型',
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    parent_id BIGINT DEFAULT 0 COMMENT '父租户ID',
    level INT DEFAULT 1 COMMENT '租户层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    
    -- 联系信息
    contact_person VARCHAR(32) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(64) COMMENT '联系邮箱',
    
    -- 企业信息
    industry VARCHAR(32) COMMENT '所属行业',
    scale VARCHAR(16) COMMENT '企业规模',
    region VARCHAR(64) COMMENT '所在地区',
    business_license VARCHAR(64) COMMENT '营业执照号',
    
    -- 配额限制
    max_users INT DEFAULT 100 COMMENT '最大用户数',
    max_storage BIGINT DEFAULT 1073741824 COMMENT '最大存储空间(1GB)',
    max_departments INT DEFAULT 50 COMMENT '最大部门数',
    max_roles INT DEFAULT 20 COMMENT '最大角色数',
    
    -- 服务配置
    package_id BIGINT COMMENT '套餐ID',
    expire_time DATETIME COMMENT '过期时间',
    domain VARCHAR(128) COMMENT '自定义域名',
    logo VARCHAR(255) COMMENT '租户Logo',
    
    -- JSON配置
    feature_config JSON COMMENT '功能配置',
    theme_config JSON COMMENT '主题配置',
    security_config JSON COMMENT '安全配置',
    
    -- 基础字段
    create_by VARCHAR(64) DEFAULT 'system',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT 'system',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag TINYINT DEFAULT 0 COMMENT '删除标志',
    remark TEXT COMMENT '备注',
    
    KEY idx_tenant_code (code),
    KEY idx_tenant_status (status),
    KEY idx_tenant_parent (parent_id),
    KEY idx_tenant_level (level)
) COMMENT = '租户信息表';
```

#### 3.1.2 租户类型设计
- **SYSTEM**: 系统租户（超级管理员）
- **ENTERPRISE**: 企业租户（大型企业，支持子租户）
- **STANDARD**: 标准租户（中小企业）
- **PERSONAL**: 个人租户（个人用户）

#### 3.1.3 租户状态流转
```
INACTIVE(未激活) → ACTIVE(正常) → SUSPENDED(暂停) → EXPIRED(过期)
                     ↑              ↓
                     └─── 重新激活 ←──┘
```

### 3.2 组织架构模型 (sys_organization)

#### 3.2.1 核心字段设计
```sql
CREATE TABLE sys_organization (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '组织ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    org_name VARCHAR(64) NOT NULL COMMENT '组织名称',
    org_code VARCHAR(32) NOT NULL COMMENT '组织编码',
    org_type VARCHAR(16) NOT NULL DEFAULT 'DEPT' COMMENT '组织类型',
    
    -- 树形结构
    parent_id BIGINT DEFAULT 0 COMMENT '父组织ID',
    ancestors VARCHAR(500) DEFAULT '0' COMMENT '祖级列表',
    level INT DEFAULT 1 COMMENT '组织层级',
    sort_order INT DEFAULT 0 COMMENT '显示顺序',
    
    -- 负责人信息
    leader_id BIGINT COMMENT '负责人ID',
    deputy_leader_id BIGINT COMMENT '副负责人ID',
    
    -- 联系信息
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(64) COMMENT '邮箱',
    address VARCHAR(255) COMMENT '地址',
    
    -- 组织配置
    org_config JSON COMMENT '组织配置',
    status VARCHAR(16) DEFAULT 'NORMAL' COMMENT '组织状态',
    
    -- 基础字段
    create_by VARCHAR(64) DEFAULT 'system',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64) DEFAULT 'system',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    del_flag TINYINT DEFAULT 0 COMMENT '删除标志',
    remark TEXT COMMENT '备注',
    
    UNIQUE KEY uk_tenant_org_code (tenant_id, org_code),
    KEY idx_org_tenant (tenant_id),
    KEY idx_org_parent (parent_id),
    KEY idx_org_leader (leader_id),
    KEY idx_org_level (level)
) COMMENT = '组织架构表';
```

#### 3.2.2 组织类型设计
- **COMPANY**: 公司（顶层组织）
- **DEPT**: 部门（主要组织单位）
- **TEAM**: 团队（业务团队）
- **GROUP**: 小组（最小组织单位）

### 3.3 用户模型 (sys_user) - 已有基础，需优化

#### 3.3.1 优化建议
在现有用户表基础上增加以下字段：
- 多组织支持
- 用户标签系统
- 扩展属性支持

### 3.4 权限模型设计

#### 3.4.1 权限资源表 (sys_permission) - 已有基础

#### 3.4.2 角色表 (sys_role) - 已有基础

#### 3.4.3 用户角色关联表 (sys_user_role)
```sql
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    org_id BIGINT COMMENT '组织范围ID（某个组织下的角色）',
    effective_time DATETIME COMMENT '生效时间',
    expire_time DATETIME COMMENT '过期时间',
    create_by VARCHAR(64) DEFAULT 'system',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_tenant_user_role_org (tenant_id, user_id, role_id, org_id),
    KEY idx_user_role_tenant (tenant_id),
    KEY idx_user_role_user (user_id),
    KEY idx_user_role_role (role_id)
) COMMENT = '用户角色关联表';
```

#### 3.4.4 角色权限关联表 (sys_role_permission)
```sql
CREATE TABLE sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_by VARCHAR(64) DEFAULT 'system',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_tenant_role_permission (tenant_id, role_id, permission_id),
    KEY idx_role_perm_tenant (tenant_id),
    KEY idx_role_perm_role (role_id)
) COMMENT = '角色权限关联表';
```

#### 3.4.5 用户组织关联表 (sys_user_organization)
```sql
CREATE TABLE sys_user_organization (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    org_id BIGINT NOT NULL COMMENT '组织ID',
    position VARCHAR(64) COMMENT '职位',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主组织',
    join_time DATETIME COMMENT '加入时间',
    leave_time DATETIME COMMENT '离开时间',
    create_by VARCHAR(64) DEFAULT 'system',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_tenant_user_org (tenant_id, user_id, org_id),
    KEY idx_user_org_tenant (tenant_id),
    KEY idx_user_org_user (user_id),
    KEY idx_user_org_org (org_id)
) COMMENT = '用户组织关联表';
```

## 4. 业务流程设计

### 4.1 租户管理流程

#### 4.1.1 租户注册流程
```
租户申请 → 信息验证 → 管理员审核 → 租户激活 → 初始化配置 → 开始使用
```

#### 4.1.2 租户生命周期管理
- **创建**: 系统管理员创建租户，分配基础配额
- **激活**: 租户信息完善后激活服务
- **暂停**: 违规或欠费时暂停服务
- **恢复**: 问题解决后恢复服务
- **过期**: 服务期满自动过期
- **续费**: 续费后重新激活

### 4.2 组织架构管理流程

#### 4.2.1 组织创建流程
```
组织规划 → 层级设计 → 创建组织 → 分配负责人 → 设置权限 → 用户分配
```

#### 4.2.2 组织调整流程
- **重命名**: 修改组织名称和编码
- **移动**: 调整组织层级关系
- **合并**: 合并多个组织
- **拆分**: 拆分大型组织
- **删除**: 删除空组织（需处理用户归属）

### 4.3 用户管理流程

#### 4.3.1 用户入职流程
```
创建用户 → 分配组织 → 分配角色 → 设置权限 → 账号激活 → 首次登录
```

#### 4.3.2 用户离职流程
```
离职申请 → 权限回收 → 数据交接 → 账号停用 → 组织移除 → 数据归档
```

### 4.4 权限管理流程

#### 4.4.1 权限分配流程
```
权限定义 → 角色创建 → 权限绑定 → 角色分配 → 权限验证 → 使用监控
```

#### 4.4.2 权限审计流程
- **定期审计**: 每季度进行权限审计
- **异常监控**: 实时监控权限使用异常
- **合规检查**: 确保权限分配符合安全规范

## 5. 安全设计

### 5.1 多租户数据隔离

#### 5.1.1 数据隔离策略
- **物理隔离**: 超大型租户独立数据库
- **逻辑隔离**: 通过tenant_id字段隔离（推荐）
- **混合隔离**: 重要租户物理隔离，其他逻辑隔离

#### 5.1.2 数据访问控制
```java
// 自动注入租户ID的MyBatis插件
@Intercepts({@Signature(type = Executor.class, method = "query")})
public class TenantInterceptor implements Interceptor {
    // 自动为SQL添加tenant_id条件
}
```

### 5.2 权限控制策略

#### 5.2.1 访问控制模型
- **基础模型**: RBAC (Role-Based Access Control)
- **扩展模型**: ABAC (Attribute-Based Access Control)
- **数据权限**: 基于组织层级的数据访问控制

#### 5.2.2 权限验证流程
```
请求拦截 → Token验证 → 用户识别 → 权限查询 → 数据权限检查 → 授权决策
```

### 5.3 安全防护机制

#### 5.3.1 认证安全
- **密码策略**: 复杂度要求、定期更换
- **多因子认证**: 支持MFA（可选）
- **会话管理**: Token过期、单点登录

#### 5.3.2 授权安全
- **最小权限原则**: 默认最小权限
- **权限继承**: 支持权限继承机制
- **临时授权**: 支持临时权限分配

## 6. 性能优化设计

### 6.1 缓存策略

#### 6.1.1 多级缓存架构
```
本地缓存(Caffeine) → Redis缓存 → 数据库
```

#### 6.1.2 缓存数据设计
- **用户信息**: 缓存30分钟
- **权限信息**: 缓存1小时
- **组织架构**: 缓存2小时
- **租户配置**: 缓存4小时

### 6.2 数据库优化

#### 6.2.1 索引设计
- **租户隔离索引**: (tenant_id, ...)
- **组织层级索引**: (tenant_id, parent_id, level)
- **用户查询索引**: (tenant_id, username), (tenant_id, email)
- **权限查询索引**: (tenant_id, user_id), (tenant_id, role_id)

#### 6.2.2 分库分表策略
- **水平分表**: 大表按租户ID分表
- **读写分离**: 读写分离提升查询性能
- **分页优化**: 使用游标分页替代offset

### 6.3 接口性能优化

#### 6.3.1 批量操作支持
- **批量用户导入**: 支持Excel批量导入
- **批量权限分配**: 支持批量角色分配
- **批量组织操作**: 支持批量组织调整

#### 6.3.2 异步处理
- **大数据量操作**: 异步处理并提供进度查询
- **通知推送**: 异步发送操作通知
- **数据同步**: 异步同步数据到其他系统

## 7. 监控与运维

### 7.1 业务监控

#### 7.1.1 关键指标监控
- **租户数量**: 活跃租户数、新增租户数
- **用户活跃度**: DAU、MAU、登录成功率
- **权限使用**: 权限验证次数、拒绝率
- **系统性能**: 响应时间、吞吐量、错误率

#### 7.1.2 异常监控
- **权限异常**: 非法权限访问
- **登录异常**: 异常登录行为
- **数据异常**: 数据一致性检查

### 7.2 日志管理

#### 7.2.1 日志分类
- **访问日志**: 用户访问记录
- **操作日志**: 用户操作记录
- **安全日志**: 安全相关事件
- **系统日志**: 系统运行日志

#### 7.2.2 日志格式标准
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "tenantId": "1001",
  "userId": "2001",
  "operation": "USER_CREATE",
  "resource": "/api/identity/users",
  "result": "SUCCESS",
  "ip": "192.168.1.100",
  "userAgent": "Chrome/120.0",
  "details": {...}
}
```

## 8. 扩展性设计

### 8.1 微服务架构扩展
- **服务拆分**: 支持按业务域进一步拆分
- **服务发现**: 基于Nacos的动态服务发现
- **配置中心**: 集中化配置管理
- **链路追踪**: 全链路性能监控

### 8.2 功能扩展点
- **认证方式扩展**: 支持SSO、LDAP、OAuth2等
- **权限模型扩展**: 支持更复杂的权限模型
- **组织模型扩展**: 支持矩阵式组织结构
- **数据权限扩展**: 支持字段级权限控制

### 8.3 集成能力
- **第三方系统集成**: 提供标准API接口
- **数据同步**: 支持与HR系统数据同步
- **单点登录**: 支持企业级SSO集成
- **移动端支持**: 提供移动端SDK

## 9. 部署方案

### 9.1 容器化部署
```yaml
# docker-compose.yml示例
version: '3.8'
services:
  admin-identity:
    image: admin/identity:latest
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - NACOS_SERVER=nacos:8848
    depends_on:
      - mysql
      - redis
      - nacos
```

### 9.2 高可用部署
- **服务集群**: 多实例部署
- **数据库高可用**: 主从复制、读写分离
- **缓存高可用**: Redis Cluster
- **负载均衡**: Nginx + Gateway

### 9.3 监控部署
- **Prometheus**: 指标收集
- **Grafana**: 指标展示
- **Zipkin**: 链路追踪
- **ELK**: 日志分析

## 10. 开发计划

### 10.1 开发阶段规划
1. **第一阶段**: 租户管理模块开发（2周）
2. **第二阶段**: 组织架构管理模块开发（2周）
3. **第三阶段**: 用户管理模块优化（1周）
4. **第四阶段**: 权限管理模块完善（2周）
5. **第五阶段**: 系统集成测试（1周）

### 10.2 技术债务管理
- **代码质量**: SonarQube静态代码分析
- **测试覆盖率**: 单元测试覆盖率>80%
- **文档维护**: API文档自动生成和更新
- **性能测试**: 定期性能测试和优化

这是第一版详细设计文档，接下来我们将基于这个设计进行架构优化和代码实现。
