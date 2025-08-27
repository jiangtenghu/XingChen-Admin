-- =============================================
-- 统一身份管理服务数据库初始化脚本
-- 版本: 1.0.0
-- 创建时间: 2024-01-15
-- 说明: 合并用户、租户、组织架构、角色权限管理到统一数据库
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS admin_identity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE admin_identity;

-- =============================================
-- 1. 租户管理表
-- =============================================

-- 租户信息表
DROP TABLE IF EXISTS sys_tenant;
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

-- =============================================
-- 2. 用户管理表
-- =============================================

-- 用户信息表
DROP TABLE IF EXISTS sys_user;
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

-- =============================================
-- 3. 组织架构管理表
-- =============================================

-- 组织架构表
DROP TABLE IF EXISTS sys_organization;
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
DROP TABLE IF EXISTS sys_user_organization;
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

-- =============================================
-- 4. 角色权限管理表
-- =============================================

-- 权限信息表
DROP TABLE IF EXISTS sys_permission;
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

-- 角色信息表
DROP TABLE IF EXISTS sys_role;
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

-- 用户角色关联表
DROP TABLE IF EXISTS sys_user_role;
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
DROP TABLE IF EXISTS sys_role_permission;
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

-- =============================================
-- 5. 系统基础数据初始化
-- =============================================

-- 初始化默认租户
INSERT INTO sys_tenant (id, name, code, type, status, contact_person, contact_email, max_users, max_departments, max_roles, create_by, remark) VALUES
(1, '系统管理租户', 'SYSTEM', 'SYSTEM', 'ACTIVE', '系统管理员', 'admin@system.com', 10000, 1000, 100, 'system', '系统默认租户'),
(2, '演示企业租户', 'DEMO_ENTERPRISE', 'ENTERPRISE', 'ACTIVE', '演示管理员', 'admin@demo.com', 1000, 100, 50, 'system', '演示企业租户');

-- 初始化默认组织架构
INSERT INTO sys_organization (id, tenant_id, org_name, org_code, org_type, parent_id, ancestors, level, leader_id, status, create_by, remark) VALUES
(1, 1, '系统管理部', 'SYS_ADMIN', 'COMPANY', 0, '0', 1, NULL, '0', 'system', '系统管理部门'),
(2, 2, '演示公司', 'DEMO_COMPANY', 'COMPANY', 0, '0', 1, NULL, '0', 'system', '演示公司根节点'),
(3, 2, '技术部', 'TECH_DEPT', 'DEPT', 2, '0,2', 2, NULL, '0', 'system', '技术开发部门'),
(4, 2, '市场部', 'MARKET_DEPT', 'DEPT', 2, '0,2', 2, NULL, '0', 'system', '市场营销部门'),
(5, 2, '人事部', 'HR_DEPT', 'DEPT', 2, '0,2', 2, NULL, '0', 'system', '人力资源部门');

-- 初始化系统权限
INSERT INTO sys_permission (id, permission_name, permission_key, permission_type, parent_id, level, sort_order, resource_path, http_method, status, is_system, create_by, remark) VALUES
-- 系统管理
(1, '系统管理', 'system', 'MENU', 0, 1, 1, '/system', 'GET', '0', 1, 'system', '系统管理菜单'),
(2, '用户管理', 'system:user', 'MENU', 1, 2, 1, '/system/user', 'GET', '0', 1, 'system', '用户管理菜单'),
(3, '用户查询', 'system:user:query', 'BUTTON', 2, 3, 1, '/system/user/list', 'GET', '0', 1, 'system', '用户查询权限'),
(4, '用户新增', 'system:user:add', 'BUTTON', 2, 3, 2, '/system/user', 'POST', '0', 1, 'system', '用户新增权限'),
(5, '用户修改', 'system:user:edit', 'BUTTON', 2, 3, 3, '/system/user', 'PUT', '0', 1, 'system', '用户修改权限'),
(6, '用户删除', 'system:user:remove', 'BUTTON', 2, 3, 4, '/system/user/*', 'DELETE', '0', 1, 'system', '用户删除权限'),

-- 角色管理
(10, '角色管理', 'system:role', 'MENU', 1, 2, 2, '/system/role', 'GET', '0', 1, 'system', '角色管理菜单'),
(11, '角色查询', 'system:role:query', 'BUTTON', 10, 3, 1, '/system/role/list', 'GET', '0', 1, 'system', '角色查询权限'),
(12, '角色新增', 'system:role:add', 'BUTTON', 10, 3, 2, '/system/role', 'POST', '0', 1, 'system', '角色新增权限'),
(13, '角色修改', 'system:role:edit', 'BUTTON', 10, 3, 3, '/system/role', 'PUT', '0', 1, 'system', '角色修改权限'),
(14, '角色删除', 'system:role:remove', 'BUTTON', 10, 3, 4, '/system/role/*', 'DELETE', '0', 1, 'system', '角色删除权限'),

-- 组织管理
(20, '组织管理', 'system:org', 'MENU', 1, 2, 3, '/system/org', 'GET', '0', 1, 'system', '组织管理菜单'),
(21, '组织查询', 'system:org:query', 'BUTTON', 20, 3, 1, '/system/org/list', 'GET', '0', 1, 'system', '组织查询权限'),
(22, '组织新增', 'system:org:add', 'BUTTON', 20, 3, 2, '/system/org', 'POST', '0', 1, 'system', '组织新增权限'),
(23, '组织修改', 'system:org:edit', 'BUTTON', 20, 3, 3, '/system/org', 'PUT', '0', 1, 'system', '组织修改权限'),
(24, '组织删除', 'system:org:remove', 'BUTTON', 20, 3, 4, '/system/org/*', 'DELETE', '0', 1, 'system', '组织删除权限'),

-- 租户管理
(30, '租户管理', 'system:tenant', 'MENU', 1, 2, 4, '/system/tenant', 'GET', '0', 1, 'system', '租户管理菜单'),
(31, '租户查询', 'system:tenant:query', 'BUTTON', 30, 3, 1, '/system/tenant/list', 'GET', '0', 1, 'system', '租户查询权限'),
(32, '租户新增', 'system:tenant:add', 'BUTTON', 30, 3, 2, '/system/tenant', 'POST', '0', 1, 'system', '租户新增权限'),
(33, '租户修改', 'system:tenant:edit', 'BUTTON', 30, 3, 3, '/system/tenant', 'PUT', '0', 1, 'system', '租户修改权限'),
(34, '租户删除', 'system:tenant:remove', 'BUTTON', 30, 3, 4, '/system/tenant/*', 'DELETE', '0', 1, 'system', '租户删除权限');

-- 初始化系统角色
INSERT INTO sys_role (id, tenant_id, role_name, role_key, role_sort, data_scope, role_type, status, create_by, remark) VALUES
(1, 1, '超级管理员', 'SUPER_ADMIN', 1, '1', 'SYSTEM', '0', 'system', '超级管理员角色'),
(2, 1, '系统管理员', 'SYSTEM_ADMIN', 2, '2', 'SYSTEM', '0', 'system', '系统管理员角色'),
(3, 2, '租户管理员', 'TENANT_ADMIN', 1, '2', 'TENANT', '0', 'system', '租户管理员角色'),
(4, 2, '部门经理', 'DEPT_MANAGER', 2, '4', 'CUSTOM', '0', 'system', '部门经理角色'),
(5, 2, '普通员工', 'EMPLOYEE', 3, '5', 'CUSTOM', '0', 'system', '普通员工角色');

-- 初始化角色权限关联
INSERT INTO sys_role_permission (role_id, permission_id) VALUES
-- 超级管理员拥有所有权限
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 10), (1, 11), (1, 12), (1, 13), (1, 14),
(1, 20), (1, 21), (1, 22), (1, 23), (1, 24),
(1, 30), (1, 31), (1, 32), (1, 33), (1, 34),

-- 系统管理员拥有用户、角色、组织管理权限
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6),
(2, 10), (2, 11), (2, 12), (2, 13), (2, 14),
(2, 20), (2, 21), (2, 22), (2, 23), (2, 24),

-- 租户管理员拥有本租户内的管理权限
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6),
(3, 10), (3, 11), (3, 12), (3, 13), (3, 14),
(3, 20), (3, 21), (3, 22), (3, 23), (3, 24),

-- 部门经理拥有用户查询和组织查询权限
(4, 1), (4, 2), (4, 3), (4, 20), (4, 21),

-- 普通员工只有查询权限
(5, 1), (5, 2), (5, 3), (5, 20), (5, 21);

-- 初始化默认用户
INSERT INTO sys_user (id, tenant_id, username, nickname, real_name, email, phone, user_type, status, create_by, remark) VALUES
(1, 1, 'admin', '超级管理员', '系统管理员', 'admin@system.com', '13800138000', 'ADMIN', '0', 'system', '系统超级管理员'),
(2, 2, 'demo_admin', '演示管理员', '演示管理员', 'admin@demo.com', '13800138001', 'ADMIN', '0', 'system', '演示租户管理员'),
(3, 2, 'demo_user', '演示用户', '张三', 'zhangsan@demo.com', '13800138002', 'NORMAL', '0', 'system', '演示普通用户');

-- 初始化用户角色关联
INSERT INTO sys_user_role (tenant_id, user_id, role_id, assign_by, assign_reason) VALUES
(1, 1, 1, 'system', '系统初始化'),
(2, 2, 3, 'system', '系统初始化'),
(2, 3, 5, 'system', '系统初始化');

-- 初始化用户组织关联
INSERT INTO sys_user_organization (tenant_id, user_id, org_id, position, is_primary, is_leader, assign_reason, create_by) VALUES
(1, 1, 1, '系统管理员', 1, 1, '系统初始化', 'system'),
(2, 2, 2, '总经理', 1, 1, '系统初始化', 'system'),
(2, 3, 3, '开发工程师', 1, 0, '系统初始化', 'system');

-- =============================================
-- 6. 创建索引优化查询性能
-- =============================================

-- 用户相关索引
CREATE INDEX idx_user_tenant_status ON sys_user(tenant_id, status);
CREATE INDEX idx_user_phone_tenant ON sys_user(phone, tenant_id);
CREATE INDEX idx_user_email_tenant ON sys_user(email, tenant_id);

-- 组织相关索引
CREATE INDEX idx_org_tenant_parent_status ON sys_organization(tenant_id, parent_id, status);
CREATE INDEX idx_user_org_tenant_user ON sys_user_organization(tenant_id, user_id, status);
CREATE INDEX idx_user_org_tenant_org ON sys_user_organization(tenant_id, org_id, status);

-- 角色权限相关索引
CREATE INDEX idx_role_tenant_status ON sys_role(tenant_id, status);
CREATE INDEX idx_user_role_tenant_user ON sys_user_role(tenant_id, user_id, status);
CREATE INDEX idx_permission_type_status ON sys_permission(permission_type, status);

-- =============================================
-- 7. 创建视图简化查询
-- =============================================

-- 用户完整信息视图
CREATE VIEW v_user_full_info AS
SELECT 
    u.id,
    u.tenant_id,
    u.username,
    u.nickname,
    u.real_name,
    u.email,
    u.phone,
    u.user_type,
    u.status,
    t.name AS tenant_name,
    t.code AS tenant_code,
    GROUP_CONCAT(DISTINCT o.org_name) AS org_names,
    GROUP_CONCAT(DISTINCT r.role_name) AS role_names
FROM sys_user u
LEFT JOIN sys_tenant t ON u.tenant_id = t.id
LEFT JOIN sys_user_organization uo ON u.id = uo.user_id AND uo.status = '0'
LEFT JOIN sys_organization o ON uo.org_id = o.id AND o.del_flag = 0
LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.status = '0'
LEFT JOIN sys_role r ON ur.role_id = r.id AND r.del_flag = 0
WHERE u.del_flag = 0
GROUP BY u.id;

-- 组织架构树视图
CREATE VIEW v_organization_tree AS
SELECT 
    o.id,
    o.tenant_id,
    o.org_name,
    o.org_code,
    o.org_type,
    o.parent_id,
    o.level,
    o.sort_order,
    o.leader_id,
    u.real_name AS leader_name,
    o.status,
    (SELECT COUNT(*) FROM sys_user_organization uo WHERE uo.org_id = o.id AND uo.status = '0') AS member_count
FROM sys_organization o
LEFT JOIN sys_user u ON o.leader_id = u.id
WHERE o.del_flag = 0;

-- 角色权限关联视图
CREATE VIEW v_role_permission AS
SELECT 
    r.id AS role_id,
    r.tenant_id,
    r.role_name,
    r.role_key,
    r.data_scope,
    GROUP_CONCAT(p.permission_key) AS permission_keys,
    r.status
FROM sys_role r
LEFT JOIN sys_role_permission rp ON r.id = rp.role_id
LEFT JOIN sys_permission p ON rp.permission_id = p.id AND p.del_flag = 0
WHERE r.del_flag = 0
GROUP BY r.id;

-- =============================================
-- 脚本执行完成
-- =============================================

-- 显示初始化完成信息
SELECT '数据库初始化完成！' AS message;
SELECT COUNT(*) AS tenant_count FROM sys_tenant;
SELECT COUNT(*) AS user_count FROM sys_user;
SELECT COUNT(*) AS organization_count FROM sys_organization;
SELECT COUNT(*) AS role_count FROM sys_role;
SELECT COUNT(*) AS permission_count FROM sys_permission;
