# 统一身份管理服务架构设计文档

## 🎯 设计目标

基于业务内聚性和数据一致性要求，将用户管理、租户管理、组织架构管理、角色权限管理合并为统一的身份管理服务（admin-identity），实现高内聚、低耦合的微服务架构。

## 🏗️ 整体架构设计

### 微服务架构布局

```
🏗️ 优化后的微服务架构
├── 🚪 admin-gateway (API网关)
│   ├── 统一入口和路由
│   ├── 认证和授权过滤
│   ├── 限流和熔断
│   └── 跨域处理
├── 🔐 admin-auth (认证授权服务)
│   ├── 用户登录/注册
│   ├── JWT Token管理
│   ├── OAuth2认证
│   └── 短信验证码
├── 👤 admin-identity (统一身份管理服务) [核心重构]
│   ├── 租户管理 (Tenant Management)
│   ├── 用户管理 (User Management)
│   ├── 组织架构管理 (Organization Management)
│   ├── 角色权限管理 (Role & Permission Management)
│   └── 用户关联关系管理 (User Relations)
└── ⚙️ admin-system (系统管理服务)
    ├── 菜单管理
    ├── 字典管理
    ├── 系统配置管理
    └── 操作日志审计
```

## 📊 admin-identity 服务详细设计

### 1. 服务职责边界

```yaml
核心职责:
  租户管理:
    - 租户注册、认证、配置
    - 租户资源配额管理
    - 租户套餐和功能开关
    - 租户数据隔离
    
  用户管理:
    - 用户基础信息CRUD
    - 用户状态管理
    - 用户资料维护
    - 用户认证信息同步
    
  组织架构管理:
    - 组织树结构维护
    - 部门层级管理
    - 用户组织关联
    - 组织变更历史
    
  角色权限管理:
    - 角色定义和管理
    - 权限资源管理
    - 用户角色分配
    - 数据权限控制

服务边界:
  ✅ 包含: 所有身份相关的核心数据和业务逻辑
  ❌ 不包含: 认证逻辑、系统配置、业务数据
```

### 2. 内部模块划分

```
📦 admin-identity
├── 📁 domain (领域层)
│   ├── tenant/
│   │   ├── Tenant.java
│   │   ├── TenantConfig.java
│   │   └── TenantPackage.java
│   ├── user/
│   │   ├── User.java
│   │   ├── UserProfile.java
│   │   └── UserStatus.java
│   ├── organization/
│   │   ├── Organization.java
│   │   ├── OrganizationType.java
│   │   └── UserOrganization.java
│   ├── permission/
│   │   ├── Role.java
│   │   ├── Permission.java
│   │   ├── UserRole.java
│   │   └── RolePermission.java
│   └── common/
│       ├── BaseEntity.java
│       └── TenantEntity.java
├── 📁 service (服务层)
│   ├── tenant/
│   │   ├── TenantService.java
│   │   └── TenantManagementService.java
│   ├── user/
│   │   ├── UserService.java
│   │   └── UserProfileService.java
│   ├── organization/
│   │   ├── OrganizationService.java
│   │   └── OrganizationTreeService.java
│   ├── permission/
│   │   ├── RoleService.java
│   │   ├── PermissionService.java
│   │   └── UserPermissionService.java
│   └── integration/
│       ├── IdentityAggregateService.java
│       └── IdentityCacheService.java
├── 📁 controller (控制层)
│   ├── TenantController.java
│   ├── UserController.java
│   ├── OrganizationController.java
│   ├── RoleController.java
│   └── PermissionController.java
├── 📁 mapper (数据访问层)
│   ├── TenantMapper.java
│   ├── UserMapper.java
│   ├── OrganizationMapper.java
│   ├── RoleMapper.java
│   └── PermissionMapper.java
└── 📁 config (配置层)
    ├── IdentityConfig.java
    ├── CacheConfig.java
    └── DatabaseConfig.java
```

### 3. 数据库设计

#### 数据库Schema设计
```sql
-- 统一身份管理数据库
CREATE DATABASE admin_identity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 租户表
CREATE TABLE sys_tenant (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '租户ID',
    name varchar(100) NOT NULL COMMENT '租户名称',
    code varchar(50) NOT NULL UNIQUE COMMENT '租户编码',
    type varchar(20) DEFAULT 'STANDARD' COMMENT '租户类型(ENTERPRISE/STANDARD/PERSONAL/SYSTEM)',
    status varchar(10) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/SUSPENDED/EXPIRED)',
    
    -- 层级关系
    parent_id bigint DEFAULT NULL COMMENT '父租户ID',
    level int DEFAULT 1 COMMENT '租户层级',
    sort_order int DEFAULT 0 COMMENT '排序',
    
    -- 联系信息
    contact_person varchar(50) COMMENT '联系人',
    contact_phone varchar(20) COMMENT '联系电话',
    contact_email varchar(100) COMMENT '联系邮箱',
    
    -- 业务信息
    industry varchar(50) COMMENT '所属行业',
    scale varchar(20) COMMENT '企业规模',
    region varchar(50) COMMENT '所在地区',
    business_license varchar(100) COMMENT '营业执照号',
    
    -- 配额限制
    max_users int DEFAULT 100 COMMENT '最大用户数',
    max_storage bigint DEFAULT 1073741824 COMMENT '最大存储空间(字节)',
    max_departments int DEFAULT 50 COMMENT '最大部门数',
    max_roles int DEFAULT 20 COMMENT '最大角色数',
    
    -- 服务配置
    package_id bigint COMMENT '套餐ID',
    expire_time datetime COMMENT '过期时间',
    domain varchar(100) COMMENT '自定义域名',
    logo varchar(200) COMMENT '租户Logo',
    
    -- 功能配置JSON
    feature_config json COMMENT '功能配置',
    theme_config json COMMENT '主题配置',
    security_config json COMMENT '安全配置',
    
    -- 审计字段
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag tinyint(1) DEFAULT 0 COMMENT '删除标志',
    remark varchar(500) COMMENT '备注',
    
    INDEX idx_parent_id (parent_id),
    INDEX idx_type_status (type, status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户信息表';

-- 用户表
CREATE TABLE sys_user (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    tenant_id bigint NOT NULL COMMENT '租户ID',
    
    -- 基础信息
    username varchar(30) NOT NULL COMMENT '用户账号',
    password varchar(100) COMMENT '密码(认证服务管理)',
    nickname varchar(30) COMMENT '用户昵称',
    real_name varchar(50) COMMENT '真实姓名',
    email varchar(50) COMMENT '用户邮箱',
    phone varchar(11) COMMENT '手机号码',
    
    -- 个人信息
    sex char(1) DEFAULT '0' COMMENT '用户性别(0-男,1-女,2-未知)',
    avatar varchar(100) DEFAULT '' COMMENT '头像地址',
    birthday date COMMENT '生日',
    address varchar(200) COMMENT '地址',
    
    -- 工作信息
    employee_no varchar(50) COMMENT '员工工号',
    entry_date date COMMENT '入职日期',
    position varchar(50) COMMENT '职位',
    superior_id bigint COMMENT '直属上级ID',
    
    -- 状态信息
    user_type varchar(10) DEFAULT 'NORMAL' COMMENT '用户类型(ADMIN/NORMAL/GUEST)',
    status char(1) DEFAULT '0' COMMENT '账号状态(0-正常,1-停用,2-删除)',
    login_date datetime COMMENT '最后登录时间',
    login_ip varchar(128) DEFAULT '' COMMENT '最后登录IP',
    
    -- 审计字段
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag tinyint(1) DEFAULT 0 COMMENT '删除标志',
    remark varchar(500) COMMENT '备注',
    
    UNIQUE KEY uk_tenant_username (tenant_id, username),
    UNIQUE KEY uk_tenant_phone (tenant_id, phone),
    UNIQUE KEY uk_tenant_email (tenant_id, email),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_superior_id (superior_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    
    FOREIGN KEY (tenant_id) REFERENCES sys_tenant(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 组织架构表
CREATE TABLE sys_organization (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '组织ID',
    tenant_id bigint NOT NULL COMMENT '租户ID',
    
    -- 组织信息
    org_name varchar(50) NOT NULL COMMENT '组织名称',
    org_code varchar(30) COMMENT '组织编码',
    org_type varchar(20) DEFAULT 'DEPT' COMMENT '组织类型(COMPANY/DEPT/TEAM/GROUP)',
    
    -- 层级关系
    parent_id bigint DEFAULT 0 COMMENT '父组织ID(0表示顶级)',
    ancestors varchar(500) DEFAULT '' COMMENT '祖级列表',
    level int DEFAULT 1 COMMENT '组织层级',
    sort_order int DEFAULT 0 COMMENT '显示顺序',
    
    -- 管理信息
    leader_id bigint COMMENT '负责人ID',
    deputy_leader_id bigint COMMENT '副负责人ID',
    phone varchar(11) COMMENT '联系电话',
    email varchar(50) COMMENT '邮箱',
    address varchar(200) COMMENT '地址',
    
    -- 状态信息
    status char(1) DEFAULT '0' COMMENT '组织状态(0-正常,1-停用)',
    
    -- 配置信息
    org_config json COMMENT '组织配置JSON',
    
    -- 审计字段
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag tinyint(1) DEFAULT 0 COMMENT '删除标志',
    remark varchar(500) COMMENT '备注',
    
    UNIQUE KEY uk_tenant_org_code (tenant_id, org_code),
    INDEX idx_tenant_parent (tenant_id, parent_id),
    INDEX idx_leader_id (leader_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    
    FOREIGN KEY (tenant_id) REFERENCES sys_tenant(id),
    FOREIGN KEY (leader_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构表';

-- 用户组织关联表
CREATE TABLE sys_user_organization (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    tenant_id bigint NOT NULL COMMENT '租户ID',
    user_id bigint NOT NULL COMMENT '用户ID',
    org_id bigint NOT NULL COMMENT '组织ID',
    
    -- 职位信息
    position varchar(50) COMMENT '职位名称',
    job_level varchar(20) COMMENT '职级',
    job_grade varchar(20) COMMENT '职等',
    work_location varchar(100) COMMENT '工作地点',
    
    -- 关系属性
    is_primary tinyint(1) DEFAULT 1 COMMENT '是否主要组织',
    is_leader tinyint(1) DEFAULT 0 COMMENT '是否负责人',
    is_deputy_leader tinyint(1) DEFAULT 0 COMMENT '是否副负责人',
    work_ratio decimal(5,2) DEFAULT 100.00 COMMENT '工作比例(用于兼职)',
    
    -- 时间信息
    join_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    leave_time datetime COMMENT '离开时间',
    effective_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '生效时间',
    expire_time datetime COMMENT '过期时间',
    
    -- 状态信息
    status char(1) DEFAULT '0' COMMENT '分配状态(0-正常,1-停用,2-离职)',
    assign_reason varchar(200) COMMENT '分配原因',
    leave_reason varchar(200) COMMENT '离开原因',
    
    -- 审计字段
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_user_org_primary (user_id, org_id, is_primary),
    INDEX idx_tenant_user (tenant_id, user_id),
    INDEX idx_tenant_org (tenant_id, org_id),
    INDEX idx_status (status),
    INDEX idx_join_time (join_time),
    
    FOREIGN KEY (tenant_id) REFERENCES sys_tenant(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (org_id) REFERENCES sys_organization(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组织关联表';

-- 角色表
CREATE TABLE sys_role (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    tenant_id bigint NOT NULL COMMENT '租户ID',
    
    -- 角色信息
    role_name varchar(30) NOT NULL COMMENT '角色名称',
    role_key varchar(100) NOT NULL COMMENT '角色权限字符串',
    role_sort int NOT NULL COMMENT '显示顺序',
    
    -- 权限范围
    data_scope char(1) DEFAULT '1' COMMENT '数据范围(1-全部,2-租户,3-本组织,4-本组织及以下,5-仅本人,6-自定义)',
    data_scope_org_ids varchar(500) COMMENT '数据范围组织ID集',
    
    -- 角色类型
    role_type varchar(20) DEFAULT 'CUSTOM' COMMENT '角色类型(SYSTEM/TENANT/CUSTOM/TEMPLATE)',
    
    -- 状态信息
    status char(1) NOT NULL COMMENT '角色状态(0-正常,1-停用)',
    
    -- 时间约束
    effective_time datetime COMMENT '生效时间',
    expire_time datetime COMMENT '过期时间',
    
    -- 审计字段
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag tinyint(1) DEFAULT 0 COMMENT '删除标志',
    remark varchar(500) COMMENT '备注',
    
    UNIQUE KEY uk_tenant_role_key (tenant_id, role_key),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_role_type (role_type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    
    FOREIGN KEY (tenant_id) REFERENCES sys_tenant(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- 权限表
CREATE TABLE sys_permission (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    
    -- 权限信息
    permission_name varchar(50) NOT NULL COMMENT '权限名称',
    permission_key varchar(100) NOT NULL UNIQUE COMMENT '权限标识',
    permission_type varchar(20) DEFAULT 'MENU' COMMENT '权限类型(MENU/BUTTON/API/DATA)',
    
    -- 层级关系
    parent_id bigint DEFAULT 0 COMMENT '父权限ID',
    level int DEFAULT 1 COMMENT '权限层级',
    sort_order int DEFAULT 0 COMMENT '显示顺序',
    
    -- 权限属性
    resource_path varchar(200) COMMENT '资源路径',
    http_method varchar(10) COMMENT 'HTTP方法',
    component_path varchar(200) COMMENT '组件路径',
    icon varchar(100) COMMENT '图标',
    
    -- 状态信息
    status char(1) DEFAULT '0' COMMENT '权限状态(0-正常,1-停用)',
    is_system tinyint(1) DEFAULT 0 COMMENT '是否系统权限',
    
    -- 审计字段
    create_by varchar(64) DEFAULT '' COMMENT '创建者',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by varchar(64) DEFAULT '' COMMENT '更新者',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag tinyint(1) DEFAULT 0 COMMENT '删除标志',
    remark varchar(500) COMMENT '备注',
    
    INDEX idx_parent_id (parent_id),
    INDEX idx_permission_type (permission_type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限信息表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    tenant_id bigint NOT NULL COMMENT '租户ID',
    user_id bigint NOT NULL COMMENT '用户ID',
    role_id bigint NOT NULL COMMENT '角色ID',
    
    -- 分配信息
    assign_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    assign_by varchar(64) COMMENT '分配人',
    assign_reason varchar(200) COMMENT '分配原因',
    
    -- 时间约束
    effective_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '生效时间',
    expire_time datetime COMMENT '过期时间',
    
    -- 状态信息
    status char(1) DEFAULT '0' COMMENT '分配状态(0-正常,1-停用,2-过期)',
    
    -- 审计字段
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_tenant_user (tenant_id, user_id),
    INDEX idx_tenant_role (tenant_id, role_id),
    INDEX idx_status (status),
    INDEX idx_assign_time (assign_time),
    
    FOREIGN KEY (tenant_id) REFERENCES sys_tenant(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE sys_role_permission (
    id bigint PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    role_id bigint NOT NULL COMMENT '角色ID',
    permission_id bigint NOT NULL COMMENT '权限ID',
    
    -- 审计字段
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id),
    
    FOREIGN KEY (role_id) REFERENCES sys_role(id),
    FOREIGN KEY (permission_id) REFERENCES sys_permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
```

## 🔄 服务间调用设计

### 1. Feign客户端接口设计

```java
// 认证服务调用身份服务的接口
@FeignClient(name = "admin-identity", fallback = IdentityServiceFallback.class)
public interface IdentityFeignClient {
    
    /**
     * 根据用户名获取用户信息(用于登录验证)
     */
    @GetMapping("/api/identity/users/by-username")
    Result<UserAuthInfo> getUserByUsername(@RequestParam String username, @RequestParam Long tenantId);
    
    /**
     * 获取用户完整信息(包含角色和组织)
     */
    @GetMapping("/api/identity/users/{userId}/full")
    Result<UserFullInfo> getUserFullInfo(@PathVariable Long userId);
    
    /**
     * 验证用户权限
     */
    @PostMapping("/api/identity/permissions/check")
    Result<Boolean> checkUserPermission(@RequestBody PermissionCheckRequest request);
    
    /**
     * 获取用户菜单权限
     */
    @GetMapping("/api/identity/users/{userId}/menu-permissions")
    Result<List<MenuPermissionVO>> getUserMenuPermissions(@PathVariable Long userId);
}

// 系统服务调用身份服务的接口
@FeignClient(name = "admin-identity")  
public interface IdentitySystemFeignClient {
    
    /**
     * 批量获取用户信息
     */
    @PostMapping("/api/identity/users/batch")
    Result<List<UserVO>> getUsersBatch(@RequestBody List<Long> userIds);
    
    /**
     * 获取组织架构树
     */
    @GetMapping("/api/identity/organizations/tree")
    Result<List<OrganizationTreeVO>> getOrganizationTree(@RequestParam Long tenantId);
    
    /**
     * 获取用户的数据权限范围
     */
    @GetMapping("/api/identity/users/{userId}/data-scope")
    Result<DataScopeVO> getUserDataScope(@PathVariable Long userId);
}
```

### 2. 缓存策略设计

```java
@Service
public class IdentityCacheService {
    
    private static final String USER_CACHE_KEY = "identity:user:";
    private static final String ROLE_CACHE_KEY = "identity:role:";
    private static final String ORG_CACHE_KEY = "identity:org:";
    private static final String PERMISSION_CACHE_KEY = "identity:permission:";
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired  
    private CaffeineCache localCache;
    
    /**
     * 多级缓存获取用户信息
     */
    public UserVO getUser(Long userId) {
        // L1: 本地缓存 (1分钟)
        UserVO user = localCache.get(USER_CACHE_KEY + userId, UserVO.class);
        if (user != null) {
            return user;
        }
        
        // L2: Redis缓存 (30分钟)
        user = (UserVO) redisTemplate.opsForValue().get(USER_CACHE_KEY + userId);
        if (user != null) {
            localCache.put(USER_CACHE_KEY + userId, user, 1, TimeUnit.MINUTES);
            return user;
        }
        
        // L3: 数据库查询
        user = userService.getUserById(userId);
        if (user != null) {
            // 写入缓存
            redisTemplate.opsForValue().set(USER_CACHE_KEY + userId, user, 30, TimeUnit.MINUTES);
            localCache.put(USER_CACHE_KEY + userId, user, 1, TimeUnit.MINUTES);
        }
        
        return user;
    }
    
    /**
     * 级联清理用户相关缓存
     */
    public void evictUserCache(Long userId) {
        // 清理用户基础信息缓存
        localCache.evict(USER_CACHE_KEY + userId);
        redisTemplate.delete(USER_CACHE_KEY + userId);
        
        // 清理用户权限缓存
        redisTemplate.delete(PERMISSION_CACHE_KEY + userId);
        
        // 清理用户角色缓存
        redisTemplate.delete(ROLE_CACHE_KEY + userId);
        
        // 通知其他服务清理相关缓存
        notifyOtherServicesToEvictCache(userId);
    }
}
```

## 🚀 性能优化策略

### 1. 数据库优化

```yaml
读写分离配置:
  master:
    url: jdbc:mysql://master-db:3306/admin_identity
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: password
    
  slaves:
    - url: jdbc:mysql://slave1-db:3306/admin_identity
      weight: 1
    - url: jdbc:mysql://slave2-db:3306/admin_identity  
      weight: 1

连接池配置:
  initial-size: 10
  min-idle: 10
  max-active: 100
  max-wait: 60000
  validation-query: SELECT 1
```

### 2. 查询优化

```java
@Service
public class UserQueryService {
    
    /**
     * 优化用户信息查询 - 减少N+1问题
     */
    @Query("SELECT u FROM User u " +
           "LEFT JOIN FETCH u.organizations uo " +
           "LEFT JOIN FETCH uo.organization o " +
           "LEFT JOIN FETCH u.roles ur " +
           "LEFT JOIN FETCH ur.role r " +
           "WHERE u.tenantId = :tenantId AND u.delFlag = 0")
    List<User> findUsersWithRelations(@Param("tenantId") Long tenantId);
    
    /**
     * 分页查询优化
     */
    public PageResult<UserVO> findUsersByPage(UserQueryDTO query) {
        // 先查询总数
        long total = userMapper.countByQuery(query);
        if (total == 0) {
            return PageResult.empty();
        }
        
        // 再查询数据
        List<User> users = userMapper.selectByPage(query);
        List<UserVO> userVOs = convertToVO(users);
        
        return PageResult.of(userVOs, total, query.getPageNum(), query.getPageSize());
    }
}
```

## 📝 API接口设计

### 1. 租户管理接口

```java
@RestController
@RequestMapping("/api/identity/tenants")
@Api(tags = "租户管理")
public class TenantController {
    
    @PostMapping
    @ApiOperation("创建租户")
    public Result<TenantVO> createTenant(@Valid @RequestBody CreateTenantRequest request) {
        TenantVO tenant = tenantService.createTenant(request);
        return Result.success(tenant);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取租户信息")
    public Result<TenantVO> getTenant(@PathVariable Long id) {
        TenantVO tenant = tenantService.getTenantById(id);
        return Result.success(tenant);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("更新租户信息")
    public Result<Void> updateTenant(@PathVariable Long id, @Valid @RequestBody UpdateTenantRequest request) {
        tenantService.updateTenant(id, request);
        return Result.success();
    }
    
    @PutMapping("/{id}/status")
    @ApiOperation("更新租户状态")
    public Result<Void> updateTenantStatus(@PathVariable Long id, @RequestParam String status) {
        tenantService.updateTenantStatus(id, status);
        return Result.success();
    }
    
    @GetMapping("/{id}/statistics")
    @ApiOperation("获取租户统计信息")
    public Result<TenantStatisticsVO> getTenantStatistics(@PathVariable Long id) {
        TenantStatisticsVO statistics = tenantService.getTenantStatistics(id);
        return Result.success(statistics);
    }
}
```

### 2. 聚合查询接口

```java
@RestController
@RequestMapping("/api/identity/aggregate")
@Api(tags = "聚合查询")
public class IdentityAggregateController {
    
    @GetMapping("/users/{userId}/complete")
    @ApiOperation("获取用户完整信息")
    public Result<UserCompleteVO> getUserCompleteInfo(@PathVariable Long userId) {
        UserCompleteVO userInfo = identityAggregateService.getUserCompleteInfo(userId);
        return Result.success(userInfo);
    }
    
    @GetMapping("/organizations/{orgId}/members")
    @ApiOperation("获取组织成员信息")
    public Result<OrganizationMembersVO> getOrganizationMembers(@PathVariable Long orgId) {
        OrganizationMembersVO members = identityAggregateService.getOrganizationMembers(orgId);
        return Result.success(members);
    }
    
    @PostMapping("/permissions/batch-check")
    @ApiOperation("批量权限验证")
    public Result<Map<String, Boolean>> batchCheckPermissions(@RequestBody BatchPermissionCheckRequest request) {
        Map<String, Boolean> results = identityAggregateService.batchCheckPermissions(request);
        return Result.success(results);
    }
}
```

## 🧪 测试策略

### 1. 单元测试框架

```java
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class IdentityServiceTest {
    
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("admin_identity_test")
            .withUsername("root")
            .withPassword("test123");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TenantService tenantService;
    
    @Test
    @DisplayName("创建用户并分配角色 - 事务一致性测试")
    @Transactional
    void createUserWithRoles_Success() {
        // Given
        CreateUserRequest request = CreateUserRequest.builder()
                .tenantId(1L)
                .username("testuser")
                .realName("测试用户")
                .orgId(1L)
                .roleIds(Arrays.asList(1L, 2L))
                .build();
        
        // When
        UserVO user = userService.createUserWithRoles(request);
        
        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("testuser");
        
        // 验证角色分配
        List<RoleVO> roles = userService.getUserRoles(user.getId());
        assertThat(roles).hasSize(2);
        
        // 验证组织关联
        List<OrganizationVO> orgs = userService.getUserOrganizations(user.getId());
        assertThat(orgs).hasSize(1);
        assertThat(orgs.get(0).getId()).isEqualTo(1L);
    }
}
```

## 📊 监控和指标

### 1. 关键指标监控

```yaml
业务指标:
  - 租户数量变化
  - 用户注册/登录量
  - 角色权限变更频率
  - 组织架构调整次数

性能指标:
  - API响应时间
  - 数据库查询性能
  - 缓存命中率
  - 服务调用成功率

资源指标:
  - CPU使用率
  - 内存使用率  
  - 数据库连接数
  - Redis连接数
```

## 🔄 迁移策略

### 1. 数据迁移方案

```sql
-- 阶段1: 创建新的统一数据库
CREATE DATABASE admin_identity;

-- 阶段2: 迁移用户数据
INSERT INTO admin_identity.sys_user 
SELECT * FROM admin_user.sys_user;

-- 阶段3: 迁移权限数据  
INSERT INTO admin_identity.sys_role
SELECT * FROM admin_system.sys_role;

-- 阶段4: 验证数据完整性
SELECT COUNT(*) FROM admin_identity.sys_user;
SELECT COUNT(*) FROM admin_identity.sys_role;
```

### 2. 服务切换方案

```yaml
灰度发布策略:
  阶段1: 双写模式 (写入新旧两个数据库)
  阶段2: 读取验证 (对比新旧数据一致性)  
  阶段3: 切换读取 (从新数据库读取)
  阶段4: 停止旧服务 (完全切换到新服务)

回滚预案:
  - 保留旧数据库1周时间
  - 快速切换DNS配置
  - 数据库主从切换
  - 服务版本回退
```

---

## 🎯 实施计划

该设计文档为统一身份管理服务提供了完整的架构方案，接下来将按照以下步骤实施：

1. **数据库设计** - 创建统一的数据库Schema
2. **服务重构** - 合并现有的用户、组织、权限服务
3. **API优化** - 设计统一的API接口和调用规范
4. **缓存优化** - 实现多级缓存策略
5. **测试验证** - 编写完整的单元测试和集成测试
6. **性能调优** - 数据库优化和查询性能提升
7. **监控部署** - 添加监控指标和运维工具

这样的设计既保证了业务的内聚性，又提供了良好的扩展性和性能表现。
