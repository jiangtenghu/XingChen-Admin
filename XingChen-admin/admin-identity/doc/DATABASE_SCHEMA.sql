-- =====================================================
-- 统一身份管理系统数据库建表脚本
-- 版本: 1.0.0
-- 创建时间: 2024-01-15
-- 描述: 租户、组织架构、用户管理及权限管理数据表
-- =====================================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. 租户管理相关表
-- =====================================================

-- 租户信息表
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '租户ID',
    `name` VARCHAR(64) NOT NULL COMMENT '租户名称',
    `code` VARCHAR(32) NOT NULL UNIQUE COMMENT '租户编码',
    `type` VARCHAR(16) NOT NULL DEFAULT 'STANDARD' COMMENT '租户类型(SYSTEM/ENTERPRISE/STANDARD/PERSONAL)',
    `status` VARCHAR(16) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/SUSPENDED/EXPIRED)',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父租户ID',
    `level` INT DEFAULT 1 COMMENT '租户层级',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    
    -- 联系信息
    `contact_person` VARCHAR(32) COMMENT '联系人',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `contact_email` VARCHAR(64) COMMENT '联系邮箱',
    
    -- 企业信息
    `industry` VARCHAR(32) COMMENT '所属行业',
    `scale` VARCHAR(16) COMMENT '企业规模(SMALL/MEDIUM/LARGE/ENTERPRISE)',
    `region` VARCHAR(64) COMMENT '所在地区',
    `business_license` VARCHAR(64) COMMENT '营业执照号',
    
    -- 配额限制
    `max_users` INT DEFAULT 100 COMMENT '最大用户数',
    `max_storage` BIGINT DEFAULT 1073741824 COMMENT '最大存储空间(字节)',
    `max_departments` INT DEFAULT 50 COMMENT '最大部门数',
    `max_roles` INT DEFAULT 20 COMMENT '最大角色数',
    
    -- 服务配置
    `package_id` BIGINT COMMENT '套餐ID',
    `expire_time` DATETIME COMMENT '过期时间',
    `domain` VARCHAR(128) COMMENT '自定义域名',
    `logo` VARCHAR(255) COMMENT '租户Logo',
    
    -- JSON配置
    `feature_config` JSON COMMENT '功能配置{"modules":["user","org","role"],"features":{"sso":true,"mfa":false}}',
    `theme_config` JSON COMMENT '主题配置{"primaryColor":"#1890ff","logo":"","title":""}',
    `security_config` JSON COMMENT '安全配置{"passwordPolicy":{},"sessionConfig":{},"ipWhitelist":[]}',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system' COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT 'system' COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` TINYINT DEFAULT 0 COMMENT '删除标志(0-正常,1-删除)',
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    KEY `idx_tenant_code` (`code`),
    KEY `idx_tenant_status` (`status`),
    KEY `idx_tenant_parent` (`parent_id`),
    KEY `idx_tenant_level` (`level`),
    KEY `idx_tenant_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户信息表';

-- 租户套餐表
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '套餐ID',
    `package_name` VARCHAR(64) NOT NULL COMMENT '套餐名称',
    `package_code` VARCHAR(32) NOT NULL UNIQUE COMMENT '套餐编码',
    `package_type` VARCHAR(16) NOT NULL DEFAULT 'STANDARD' COMMENT '套餐类型(FREE/BASIC/STANDARD/PREMIUM/ENTERPRISE)',
    `price` DECIMAL(10,2) DEFAULT 0.00 COMMENT '套餐价格(元/月)',
    `duration` INT DEFAULT 12 COMMENT '有效期(月)',
    
    -- 配额限制
    `max_users` INT DEFAULT 100 COMMENT '最大用户数',
    `max_storage` BIGINT DEFAULT 1073741824 COMMENT '最大存储空间(字节)',
    `max_departments` INT DEFAULT 50 COMMENT '最大部门数',
    `max_roles` INT DEFAULT 20 COMMENT '最大角色数',
    
    -- 功能权限
    `features` JSON COMMENT '功能列表{"sso":true,"mfa":false,"api":true,"export":true}',
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag` TINYINT DEFAULT 0,
    `remark` TEXT COMMENT '套餐描述',
    
    KEY `idx_package_code` (`package_code`),
    KEY `idx_package_type` (`package_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户套餐表';

-- =====================================================
-- 2. 组织架构相关表
-- =====================================================

-- 组织架构表(已存在，需要优化)
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '组织ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `org_name` VARCHAR(64) NOT NULL COMMENT '组织名称',
    `org_code` VARCHAR(32) NOT NULL COMMENT '组织编码',
    `org_type` VARCHAR(16) NOT NULL DEFAULT 'DEPT' COMMENT '组织类型(COMPANY/DEPT/TEAM/GROUP)',
    
    -- 树形结构
    `parent_id` BIGINT DEFAULT 0 COMMENT '父组织ID(0表示顶级)',
    `ancestors` VARCHAR(500) DEFAULT '0' COMMENT '祖级列表(0,1,2)',
    `level` INT DEFAULT 1 COMMENT '组织层级',
    `sort_order` INT DEFAULT 0 COMMENT '显示顺序',
    
    -- 负责人信息
    `leader_id` BIGINT COMMENT '负责人ID',
    `deputy_leader_id` BIGINT COMMENT '副负责人ID',
    
    -- 联系信息
    `phone` VARCHAR(20) COMMENT '联系电话',
    `email` VARCHAR(64) COMMENT '邮箱',
    `address` VARCHAR(255) COMMENT '地址',
    
    -- 组织配置
    `org_config` JSON COMMENT '组织配置{"allowSubOrg":true,"maxMembers":100,"workTime":"09:00-18:00"}',
    `status` VARCHAR(16) DEFAULT 'NORMAL' COMMENT '组织状态(NORMAL/DISABLED)',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag` TINYINT DEFAULT 0,
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    UNIQUE KEY `uk_tenant_org_code` (`tenant_id`, `org_code`),
    KEY `idx_org_tenant` (`tenant_id`),
    KEY `idx_org_parent` (`parent_id`),
    KEY `idx_org_leader` (`leader_id`),
    KEY `idx_org_level` (`level`),
    KEY `idx_org_ancestors` (`ancestors`),
    KEY `idx_org_status` (`tenant_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织架构表';

-- =====================================================
-- 3. 用户管理相关表
-- =====================================================

-- 用户信息表(在现有基础上优化)
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户账号',
    `password` VARCHAR(128) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(64) COMMENT '用户昵称',
    `real_name` VARCHAR(64) COMMENT '真实姓名',
    `email` VARCHAR(64) COMMENT '用户邮箱',
    `phone` VARCHAR(20) COMMENT '手机号码',
    `sex` VARCHAR(2) DEFAULT '2' COMMENT '用户性别(0-男,1-女,2-未知)',
    `avatar` VARCHAR(255) COMMENT '头像地址',
    `birthday` DATE COMMENT '生日',
    `address` VARCHAR(255) COMMENT '地址',
    
    -- 员工信息
    `employee_no` VARCHAR(32) COMMENT '员工工号',
    `entry_date` DATE COMMENT '入职日期',
    `position` VARCHAR(64) COMMENT '职位',
    `superior_id` BIGINT COMMENT '直属上级ID',
    
    -- 用户分类
    `user_type` VARCHAR(16) DEFAULT 'NORMAL' COMMENT '用户类型(ADMIN/NORMAL/GUEST/SYSTEM)',
    `user_source` VARCHAR(16) DEFAULT 'MANUAL' COMMENT '用户来源(MANUAL/IMPORT/SYNC/SSO)',
    `user_tags` VARCHAR(255) COMMENT '用户标签(逗号分隔)',
    
    -- 状态信息
    `status` VARCHAR(2) DEFAULT '0' COMMENT '账号状态(0-正常,1-停用,2-锁定)',
    `login_date` DATETIME COMMENT '最后登录时间',
    `login_ip` VARCHAR(128) COMMENT '最后登录IP',
    `login_count` INT DEFAULT 0 COMMENT '登录次数',
    `password_update_time` DATETIME COMMENT '密码更新时间',
    `lock_time` DATETIME COMMENT '锁定时间',
    `unlock_time` DATETIME COMMENT '解锁时间',
    
    -- 扩展属性
    `extended_attributes` JSON COMMENT '扩展属性{"department":"技术部","level":"P6","skills":["Java","Python"]}',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag` TINYINT DEFAULT 0,
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    UNIQUE KEY `uk_tenant_username` (`tenant_id`, `username`),
    UNIQUE KEY `uk_tenant_email` (`tenant_id`, `email`),
    UNIQUE KEY `uk_tenant_phone` (`tenant_id`, `phone`),
    KEY `idx_user_tenant` (`tenant_id`),
    KEY `idx_user_employee_no` (`tenant_id`, `employee_no`),
    KEY `idx_user_status` (`tenant_id`, `status`, `del_flag`),
    KEY `idx_user_type` (`user_type`),
    KEY `idx_user_superior` (`superior_id`),
    KEY `idx_user_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 用户组织关联表
DROP TABLE IF EXISTS `sys_user_organization`;
CREATE TABLE `sys_user_organization` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `org_id` BIGINT NOT NULL COMMENT '组织ID',
    `position` VARCHAR(64) COMMENT '职位',
    `is_primary` TINYINT DEFAULT 0 COMMENT '是否主组织(0-否,1-是)',
    `join_time` DATETIME COMMENT '加入时间',
    `leave_time` DATETIME COMMENT '离开时间',
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 索引
    UNIQUE KEY `uk_tenant_user_org` (`tenant_id`, `user_id`, `org_id`),
    KEY `idx_user_org_tenant` (`tenant_id`),
    KEY `idx_user_org_user` (`user_id`),
    KEY `idx_user_org_org` (`org_id`),
    KEY `idx_user_org_primary` (`tenant_id`, `user_id`, `is_primary`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户组织关联表';

-- =====================================================
-- 4. 权限管理相关表
-- =====================================================

-- 权限资源表(在现有基础上优化)
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    `permission_name` VARCHAR(64) NOT NULL COMMENT '权限名称',
    `permission_key` VARCHAR(128) NOT NULL COMMENT '权限标识',
    `permission_type` VARCHAR(16) NOT NULL DEFAULT 'MENU' COMMENT '权限类型(MENU/BUTTON/API/DATA)',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
    `level` INT DEFAULT 1 COMMENT '权限层级',
    `sort_order` INT DEFAULT 0 COMMENT '显示顺序',
    
    -- 资源信息
    `resource_path` VARCHAR(255) COMMENT '资源路径',
    `http_method` VARCHAR(16) COMMENT 'HTTP方法(GET/POST/PUT/DELETE)',
    `component_path` VARCHAR(255) COMMENT '组件路径',
    `icon` VARCHAR(64) COMMENT '图标',
    
    -- 权限属性
    `status` VARCHAR(2) DEFAULT '0' COMMENT '权限状态(0-正常,1-停用)',
    `is_system` TINYINT DEFAULT 0 COMMENT '是否系统权限(0-否,1-是)',
    `is_external` TINYINT DEFAULT 0 COMMENT '是否外部链接(0-否,1-是)',
    `visible` TINYINT DEFAULT 1 COMMENT '是否可见(0-隐藏,1-显示)',
    
    -- 权限配置
    `permission_config` JSON COMMENT '权限配置{"cache":true,"log":true,"anonymous":false}',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag` TINYINT DEFAULT 0,
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    UNIQUE KEY `uk_permission_key` (`permission_key`),
    KEY `idx_permission_parent` (`parent_id`),
    KEY `idx_permission_type` (`permission_type`),
    KEY `idx_permission_status` (`status`),
    KEY `idx_permission_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限资源表';

-- 角色表(在现有基础上优化)
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `role_name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `role_key` VARCHAR(128) NOT NULL COMMENT '角色权限字符串',
    `role_sort` INT DEFAULT 0 COMMENT '显示顺序',
    
    -- 数据权限
    `data_scope` VARCHAR(2) DEFAULT '4' COMMENT '数据范围(1-全部,2-租户,3-本组织,4-本组织及以下,5-仅本人,6-自定义)',
    `data_scope_org_ids` VARCHAR(1000) COMMENT '数据范围组织ID集(自定义数据权限时使用)',
    
    -- 角色属性
    `role_type` VARCHAR(16) DEFAULT 'CUSTOM' COMMENT '角色类型(SYSTEM/TENANT/CUSTOM/TEMPLATE)',
    `status` VARCHAR(2) DEFAULT '0' COMMENT '角色状态(0-正常,1-停用)',
    `effective_time` DATETIME COMMENT '生效时间',
    `expire_time` DATETIME COMMENT '过期时间',
    
    -- 角色配置
    `role_config` JSON COMMENT '角色配置{"inheritable":true,"assignable":true,"maxUsers":100}',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `del_flag` TINYINT DEFAULT 0,
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    UNIQUE KEY `uk_tenant_role_key` (`tenant_id`, `role_key`),
    KEY `idx_role_tenant` (`tenant_id`),
    KEY `idx_role_type` (`role_type`),
    KEY `idx_role_status` (`tenant_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `org_id` BIGINT COMMENT '组织范围ID(某个组织下的角色)',
    `effective_time` DATETIME COMMENT '生效时间',
    `expire_time` DATETIME COMMENT '过期时间',
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/EXPIRED)',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 索引
    UNIQUE KEY `uk_tenant_user_role_org` (`tenant_id`, `user_id`, `role_id`, `org_id`),
    KEY `idx_user_role_tenant` (`tenant_id`),
    KEY `idx_user_role_user` (`user_id`),
    KEY `idx_user_role_role` (`role_id`),
    KEY `idx_user_role_org` (`org_id`),
    KEY `idx_user_role_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- 索引
    UNIQUE KEY `uk_tenant_role_permission` (`tenant_id`, `role_id`, `permission_id`),
    KEY `idx_role_perm_tenant` (`tenant_id`),
    KEY `idx_role_perm_role` (`role_id`),
    KEY `idx_role_perm_permission` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 用户直接权限表(用于特殊权限授权)
DROP TABLE IF EXISTS `sys_user_permission`;
CREATE TABLE `sys_user_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `tenant_id` BIGINT NOT NULL COMMENT '租户ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `permission_type` VARCHAR(16) DEFAULT 'GRANT' COMMENT '权限类型(GRANT-授权,DENY-拒绝)',
    `effective_time` DATETIME COMMENT '生效时间',
    `expire_time` DATETIME COMMENT '过期时间',
    `status` VARCHAR(16) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/EXPIRED)',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `remark` TEXT COMMENT '授权原因',
    
    -- 索引
    UNIQUE KEY `uk_tenant_user_permission` (`tenant_id`, `user_id`, `permission_id`),
    KEY `idx_user_perm_tenant` (`tenant_id`),
    KEY `idx_user_perm_user` (`user_id`),
    KEY `idx_user_perm_permission` (`permission_id`),
    KEY `idx_user_perm_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户直接权限表';

-- =====================================================
-- 5. 审计日志相关表
-- =====================================================

-- 操作日志表
DROP TABLE IF EXISTS `sys_audit_log`;
CREATE TABLE `sys_audit_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    `tenant_id` BIGINT COMMENT '租户ID',
    `user_id` BIGINT COMMENT '用户ID',
    `username` VARCHAR(64) COMMENT '用户名',
    `operation` VARCHAR(64) NOT NULL COMMENT '操作类型',
    `resource` VARCHAR(128) COMMENT '操作资源',
    `resource_id` VARCHAR(64) COMMENT '资源ID',
    `method` VARCHAR(16) COMMENT 'HTTP方法',
    `url` VARCHAR(255) COMMENT '请求URL',
    `ip` VARCHAR(128) COMMENT 'IP地址',
    `location` VARCHAR(128) COMMENT '操作地点',
    `user_agent` VARCHAR(255) COMMENT '用户代理',
    `result` VARCHAR(16) COMMENT '操作结果(SUCCESS/FAILED)',
    `error_msg` TEXT COMMENT '错误信息',
    `request_data` JSON COMMENT '请求数据',
    `response_data` JSON COMMENT '响应数据',
    `cost_time` INT COMMENT '耗时(毫秒)',
    `operation_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    
    -- 索引
    KEY `idx_audit_tenant` (`tenant_id`),
    KEY `idx_audit_user` (`user_id`),
    KEY `idx_audit_operation` (`operation`),
    KEY `idx_audit_time` (`operation_time`),
    KEY `idx_audit_result` (`result`),
    KEY `idx_audit_ip` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作审计日志表';

-- 登录日志表
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    `tenant_id` BIGINT COMMENT '租户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `ip` VARCHAR(128) COMMENT 'IP地址',
    `location` VARCHAR(128) COMMENT '登录地点',
    `user_agent` VARCHAR(255) COMMENT '用户代理',
    `login_type` VARCHAR(16) DEFAULT 'PASSWORD' COMMENT '登录类型(PASSWORD/SSO/MFA)',
    `result` VARCHAR(16) NOT NULL COMMENT '登录结果(SUCCESS/FAILED)',
    `reason` VARCHAR(255) COMMENT '失败原因',
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    
    -- 索引
    KEY `idx_login_tenant` (`tenant_id`),
    KEY `idx_login_username` (`username`),
    KEY `idx_login_ip` (`ip`),
    KEY `idx_login_time` (`login_time`),
    KEY `idx_login_result` (`result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- =====================================================
-- 6. 系统配置相关表
-- =====================================================

-- 系统配置表
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID(0表示全局配置)',
    `config_name` VARCHAR(128) NOT NULL COMMENT '配置名称',
    `config_key` VARCHAR(128) NOT NULL COMMENT '配置键名',
    `config_value` TEXT COMMENT '配置值',
    `config_type` VARCHAR(16) DEFAULT 'STRING' COMMENT '配置类型(STRING/NUMBER/BOOLEAN/JSON)',
    `category` VARCHAR(64) COMMENT '配置分类',
    `description` VARCHAR(255) COMMENT '配置描述',
    `is_system` TINYINT DEFAULT 0 COMMENT '是否系统配置',
    `editable` TINYINT DEFAULT 1 COMMENT '是否可编辑',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    UNIQUE KEY `uk_tenant_config_key` (`tenant_id`, `config_key`),
    KEY `idx_config_category` (`category`),
    KEY `idx_config_system` (`is_system`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 字典数据表
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '字典ID',
    `tenant_id` BIGINT DEFAULT 0 COMMENT '租户ID(0表示全局字典)',
    `dict_type` VARCHAR(64) NOT NULL COMMENT '字典类型',
    `dict_label` VARCHAR(128) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(128) NOT NULL COMMENT '字典键值',
    `dict_sort` INT DEFAULT 0 COMMENT '显示顺序',
    `css_class` VARCHAR(128) COMMENT '样式属性(其他样式扩展)',
    `list_class` VARCHAR(128) COMMENT '表格回显样式',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认(1是 0否)',
    `status` VARCHAR(2) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    
    -- 基础字段
    `create_by` VARCHAR(64) DEFAULT 'system',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(64) DEFAULT 'system',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `remark` TEXT COMMENT '备注',
    
    -- 索引
    KEY `idx_dict_tenant_type` (`tenant_id`, `dict_type`),
    KEY `idx_dict_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

-- =====================================================
-- 7. 初始化数据
-- =====================================================

-- 插入系统租户
INSERT INTO `sys_tenant` (`id`, `name`, `code`, `type`, `status`, `parent_id`, `level`, `contact_person`, `contact_email`, `max_users`, `max_storage`, `max_departments`, `max_roles`, `feature_config`, `theme_config`, `security_config`) VALUES
(1, '系统租户', 'system', 'SYSTEM', 'ACTIVE', 0, 1, '系统管理员', 'admin@system.com', 9999, 10737418240, 999, 999, 
'{"modules": ["user", "org", "role", "tenant", "system"], "features": {"sso": true, "mfa": true, "api": true, "export": true, "audit": true}}',
'{"primaryColor": "#1890ff", "logo": "", "title": "XingChen管理系统"}',
'{"passwordPolicy": {"minLength": 8, "requireSpecialChar": true, "requireNumber": true, "requireUpperCase": true, "maxAge": 90}, "sessionConfig": {"timeout": 30, "maxConcurrent": 1}, "ipWhitelist": []}');

-- 插入默认套餐
INSERT INTO `sys_tenant_package` (`id`, `package_name`, `package_code`, `package_type`, `price`, `duration`, `max_users`, `max_storage`, `max_departments`, `max_roles`, `features`) VALUES
(1, '免费版', 'FREE', 'FREE', 0.00, 12, 10, 1073741824, 5, 5, '{"sso": false, "mfa": false, "api": false, "export": false}'),
(2, '基础版', 'BASIC', 'BASIC', 99.00, 12, 50, 5368709120, 20, 10, '{"sso": true, "mfa": false, "api": true, "export": true}'),
(3, '标准版', 'STANDARD', 'STANDARD', 299.00, 12, 200, 21474836480, 50, 30, '{"sso": true, "mfa": true, "api": true, "export": true}'),
(4, '企业版', 'ENTERPRISE', 'ENTERPRISE', 999.00, 12, 1000, 107374182400, 200, 100, '{"sso": true, "mfa": true, "api": true, "export": true, "audit": true}');

-- 插入根组织
INSERT INTO `sys_organization` (`id`, `tenant_id`, `org_name`, `org_code`, `org_type`, `parent_id`, `ancestors`, `level`, `sort_order`, `status`) VALUES
(1, 1, 'XingChen科技', 'ROOT', 'COMPANY', 0, '0', 1, 0, 'NORMAL');

-- 插入系统管理员用户
INSERT INTO `sys_user` (`id`, `tenant_id`, `username`, `password`, `nickname`, `real_name`, `email`, `user_type`, `status`, `password_update_time`) VALUES
(1, 1, 'admin', '$2a$12$7J8ZGnr5nI8p4KqXuKQl3e5wOz8ZQxl0p9J8ZGnr5nI8p4KqXuKQl3', '系统管理员', '管理员', 'admin@xingchen.com', 'ADMIN', '0', NOW());

-- 插入用户组织关联
INSERT INTO `sys_user_organization` (`tenant_id`, `user_id`, `org_id`, `position`, `is_primary`, `join_time`, `status`) VALUES
(1, 1, 1, '系统管理员', 1, NOW(), 'ACTIVE');

-- 插入系统权限菜单
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_key`, `permission_type`, `parent_id`, `level`, `sort_order`, `resource_path`, `component_path`, `icon`, `status`, `is_system`) VALUES
-- 主菜单
(1, '系统管理', 'system', 'MENU', 0, 1, 1, '/system', '', 'setting', '0', 1),
(2, '租户管理', 'tenant', 'MENU', 0, 1, 2, '/tenant', '', 'team', '0', 1),
(3, '用户管理', 'user', 'MENU', 0, 1, 3, '/user', '', 'user', '0', 1),
(4, '组织管理', 'organization', 'MENU', 0, 1, 4, '/organization', '', 'apartment', '0', 1),
(5, '权限管理', 'permission', 'MENU', 0, 1, 5, '/permission', '', 'safety', '0', 1),

-- 租户管理子菜单
(11, '租户列表', 'tenant:list', 'MENU', 2, 2, 1, '/tenant/list', '/tenant/list', '', '0', 1),
(12, '套餐管理', 'package:list', 'MENU', 2, 2, 2, '/tenant/package', '/tenant/package', '', '0', 1),

-- 用户管理子菜单
(21, '用户列表', 'user:list', 'MENU', 3, 2, 1, '/user/list', '/user/list', '', '0', 1),

-- 组织管理子菜单
(31, '组织架构', 'org:list', 'MENU', 4, 2, 1, '/organization/list', '/organization/list', '', '0', 1),

-- 权限管理子菜单
(41, '角色管理', 'role:list', 'MENU', 5, 2, 1, '/permission/role', '/permission/role', '', '0', 1),
(42, '权限管理', 'permission:list', 'MENU', 5, 2, 2, '/permission/list', '/permission/list', '', '0', 1),

-- 按钮权限
(111, '新增租户', 'tenant:add', 'BUTTON', 11, 3, 1, '', '', '', '0', 1),
(112, '修改租户', 'tenant:edit', 'BUTTON', 11, 3, 2, '', '', '', '0', 1),
(113, '删除租户', 'tenant:remove', 'BUTTON', 11, 3, 3, '', '', '', '0', 1),
(114, '租户详情', 'tenant:detail', 'BUTTON', 11, 3, 4, '', '', '', '0', 1),

(211, '新增用户', 'user:add', 'BUTTON', 21, 3, 1, '', '', '', '0', 1),
(212, '修改用户', 'user:edit', 'BUTTON', 21, 3, 2, '', '', '', '0', 1),
(213, '删除用户', 'user:remove', 'BUTTON', 21, 3, 3, '', '', '', '0', 1),
(214, '重置密码', 'user:resetPwd', 'BUTTON', 21, 3, 4, '', '', '', '0', 1),

-- API权限
(1001, '租户API', 'api:tenant:*', 'API', 0, 1, 1001, '/api/identity/tenants/**', '', '', '0', 1),
(1002, '用户API', 'api:user:*', 'API', 0, 1, 1002, '/api/identity/users/**', '', '', '0', 1),
(1003, '组织API', 'api:org:*', 'API', 0, 1, 1003, '/api/identity/organizations/**', '', '', '0', 1),
(1004, '角色API', 'api:role:*', 'API', 0, 1, 1004, '/api/identity/roles/**', '', '', '0', 1),
(1005, '权限API', 'api:permission:*', 'API', 0, 1, 1005, '/api/identity/permissions/**', '', '', '0', 1);

-- 插入超级管理员角色
INSERT INTO `sys_role` (`id`, `tenant_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `role_type`, `status`) VALUES
(1, 1, '超级管理员', 'SUPER_ADMIN', 1, '1', 'SYSTEM', '0'),
(2, 1, '系统管理员', 'SYSTEM_ADMIN', 2, '2', 'SYSTEM', '0'),
(3, 1, '租户管理员', 'TENANT_ADMIN', 3, '3', 'TENANT', '0'),
(4, 1, '普通用户', 'USER', 4, '5', 'CUSTOM', '0');

-- 分配用户角色
INSERT INTO `sys_user_role` (`tenant_id`, `user_id`, `role_id`, `status`) VALUES
(1, 1, 1, 'ACTIVE');

-- 分配角色权限(超级管理员拥有所有权限)
INSERT INTO `sys_role_permission` (`tenant_id`, `role_id`, `permission_id`)
SELECT 1, 1, id FROM `sys_permission` WHERE `is_system` = 1;

-- 插入系统配置
INSERT INTO `sys_config` (`tenant_id`, `config_name`, `config_key`, `config_value`, `config_type`, `category`, `description`, `is_system`) VALUES
(0, '用户初始密码', 'sys.user.initPassword', '123456', 'STRING', 'user', '用户初始密码', 1),
(0, '密码有效期', 'sys.password.expireDays', '90', 'NUMBER', 'security', '密码有效期(天)', 1),
(0, '登录失败锁定次数', 'sys.login.maxFailTimes', '5', 'NUMBER', 'security', '连续登录失败锁定次数', 1),
(0, '账号锁定时间', 'sys.login.lockTime', '30', 'NUMBER', 'security', '账号锁定时间(分钟)', 1),
(0, '会话超时时间', 'sys.session.timeout', '30', 'NUMBER', 'security', '会话超时时间(分钟)', 1);

-- 插入字典数据
INSERT INTO `sys_dict_data` (`tenant_id`, `dict_type`, `dict_label`, `dict_value`, `dict_sort`, `status`) VALUES
-- 用户性别
(0, 'sys_user_sex', '男', '0', 1, '0'),
(0, 'sys_user_sex', '女', '1', 2, '0'),
(0, 'sys_user_sex', '未知', '2', 3, '0'),

-- 用户状态
(0, 'sys_user_status', '正常', '0', 1, '0'),
(0, 'sys_user_status', '停用', '1', 2, '0'),
(0, 'sys_user_status', '锁定', '2', 3, '0'),

-- 租户类型
(0, 'sys_tenant_type', '系统租户', 'SYSTEM', 1, '0'),
(0, 'sys_tenant_type', '企业租户', 'ENTERPRISE', 2, '0'),
(0, 'sys_tenant_type', '标准租户', 'STANDARD', 3, '0'),
(0, 'sys_tenant_type', '个人租户', 'PERSONAL', 4, '0'),

-- 租户状态
(0, 'sys_tenant_status', '正常', 'ACTIVE', 1, '0'),
(0, 'sys_tenant_status', '未激活', 'INACTIVE', 2, '0'),
(0, 'sys_tenant_status', '暂停', 'SUSPENDED', 3, '0'),
(0, 'sys_tenant_status', '过期', 'EXPIRED', 4, '0');

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 脚本执行完成
-- =====================================================
