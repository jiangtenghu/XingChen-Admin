-- OAuth 2.1 + JWT 认证系统数据库设计
-- 支持多种登录方式、租户隔离、组织权限、角色管理

-- 使用认证数据库
CREATE DATABASE IF NOT EXISTS `admin_auth_oauth21` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `admin_auth_oauth21`;

-- =============================================
-- OAuth 2.1 标准表
-- =============================================

-- OAuth2.1 客户端注册表
CREATE TABLE `oauth2_registered_client` (
  `id` varchar(100) NOT NULL COMMENT '客户端ID',
  `client_id` varchar(100) NOT NULL COMMENT '客户端标识',
  `client_id_issued_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '客户端ID签发时间',
  `client_secret` varchar(200) DEFAULT NULL COMMENT '客户端密钥',
  `client_secret_expires_at` timestamp NULL DEFAULT NULL COMMENT '客户端密钥过期时间',
  `client_name` varchar(200) NOT NULL COMMENT '客户端名称',
  `client_authentication_methods` varchar(1000) NOT NULL COMMENT '客户端认证方法',
  `authorization_grant_types` varchar(1000) NOT NULL COMMENT '授权类型',
  `redirect_uris` varchar(1000) DEFAULT NULL COMMENT '重定向URI',
  `post_logout_redirect_uris` varchar(1000) DEFAULT NULL COMMENT '注销重定向URI',
  `scopes` varchar(1000) NOT NULL COMMENT '权限范围',
  `client_settings` varchar(2000) NOT NULL COMMENT '客户端设置',
  `token_settings` varchar(2000) NOT NULL COMMENT '令牌设置',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.1客户端注册表';

-- OAuth2.1 授权记录表
CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) NOT NULL COMMENT '授权ID',
  `registered_client_id` varchar(100) NOT NULL COMMENT '注册客户端ID',
  `principal_name` varchar(200) NOT NULL COMMENT '主体名称',
  `authorization_grant_type` varchar(100) NOT NULL COMMENT '授权类型',
  `authorized_scopes` varchar(1000) DEFAULT NULL COMMENT '授权范围',
  `attributes` blob DEFAULT NULL COMMENT '属性',
  `state` varchar(500) DEFAULT NULL COMMENT '状态',
  `authorization_code_value` blob DEFAULT NULL COMMENT '授权码值',
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '授权码签发时间',
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '授权码过期时间',
  `authorization_code_metadata` blob DEFAULT NULL COMMENT '授权码元数据',
  `access_token_value` blob DEFAULT NULL COMMENT '访问令牌值',
  `access_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌签发时间',
  `access_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌过期时间',
  `access_token_metadata` blob DEFAULT NULL COMMENT '访问令牌元数据',
  `access_token_type` varchar(100) DEFAULT NULL COMMENT '访问令牌类型',
  `access_token_scopes` varchar(1000) DEFAULT NULL COMMENT '访问令牌范围',
  `oidc_id_token_value` blob DEFAULT NULL COMMENT 'OIDC ID令牌值',
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL COMMENT 'OIDC ID令牌签发时间',
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL COMMENT 'OIDC ID令牌过期时间',
  `oidc_id_token_metadata` blob DEFAULT NULL COMMENT 'OIDC ID令牌元数据',
  `refresh_token_value` blob DEFAULT NULL COMMENT '刷新令牌值',
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '刷新令牌签发时间',
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '刷新令牌过期时间',
  `refresh_token_metadata` blob DEFAULT NULL COMMENT '刷新令牌元数据',
  `user_code_value` blob DEFAULT NULL COMMENT '用户码值',
  `user_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '用户码签发时间',
  `user_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '用户码过期时间',
  `user_code_metadata` blob DEFAULT NULL COMMENT '用户码元数据',
  `device_code_value` blob DEFAULT NULL COMMENT '设备码值',
  `device_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '设备码签发时间',
  `device_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '设备码过期时间',
  `device_code_metadata` blob DEFAULT NULL COMMENT '设备码元数据',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_registered_client_id` (`registered_client_id`),
  KEY `idx_principal_name` (`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.1授权记录表';

-- OAuth2.1 授权同意表
CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) NOT NULL COMMENT '注册客户端ID',
  `principal_name` varchar(200) NOT NULL COMMENT '主体名称',
  `authorities` varchar(1000) NOT NULL COMMENT '授权权限',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.1授权同意表';

-- =============================================
-- 用户认证相关表
-- =============================================

-- 用户表 (扩展)
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint(1) DEFAULT 0 COMMENT '性别(0男1女2未知)',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `user_type` varchar(20) DEFAULT 'EMPLOYEE' COMMENT '用户类型',
  `employee_no` varchar(50) DEFAULT NULL COMMENT '员工编号',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `superior_id` bigint(20) DEFAULT NULL COMMENT '直属上级ID',
  `status` tinyint(1) DEFAULT 0 COMMENT '状态(0正常1停用)',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(45) DEFAULT NULL COMMENT '最后登录IP',
  `password_update_time` datetime DEFAULT NULL COMMENT '密码更新时间',
  `account_expire_time` datetime DEFAULT NULL COMMENT '账号过期时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_mobile` (`mobile`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 用户社交账号绑定表
CREATE TABLE `user_social_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '绑定ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `social_type` varchar(20) NOT NULL COMMENT '社交类型(WECHAT/QQ/GITHUB/DINGTALK)',
  `social_id` varchar(100) NOT NULL COMMENT '社交平台用户ID',
  `social_nickname` varchar(100) DEFAULT NULL COMMENT '社交平台昵称',
  `social_avatar` varchar(200) DEFAULT NULL COMMENT '社交平台头像',
  `social_email` varchar(100) DEFAULT NULL COMMENT '社交平台邮箱',
  `union_id` varchar(100) DEFAULT NULL COMMENT 'UnionID(微信)',
  `open_id` varchar(100) DEFAULT NULL COMMENT 'OpenID(微信)',
  `bind_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_count` int DEFAULT 0 COMMENT '登录次数',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_social_account` (`social_type`, `social_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_union_id` (`union_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户社交账号绑定表';

-- 短信验证码表
CREATE TABLE `sms_verification_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '验证码ID',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `code` varchar(10) NOT NULL COMMENT '验证码',
  `code_type` varchar(20) NOT NULL COMMENT '验证码类型(LOGIN/REGISTER/RESET_PASSWORD/BIND_MOBILE)',
  `scene` varchar(50) DEFAULT NULL COMMENT '使用场景',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `used_time` datetime DEFAULT NULL COMMENT '使用时间',
  `is_used` tinyint(1) DEFAULT 0 COMMENT '是否已使用',
  `ip_address` varchar(45) DEFAULT NULL COMMENT '请求IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `send_count` int DEFAULT 1 COMMENT '发送次数',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_mobile_type` (`mobile`, `code_type`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码表';

-- 图形验证码表
CREATE TABLE `captcha_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '验证码ID',
  `captcha_key` varchar(100) NOT NULL COMMENT '验证码键',
  `captcha_code` varchar(10) NOT NULL COMMENT '验证码值',
  `captcha_type` varchar(20) DEFAULT 'IMAGE' COMMENT '验证码类型(IMAGE/SLIDER)',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `used_time` datetime DEFAULT NULL COMMENT '使用时间',
  `is_used` tinyint(1) DEFAULT 0 COMMENT '是否已使用',
  `ip_address` varchar(45) DEFAULT NULL COMMENT '请求IP',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_captcha_key` (`captcha_key`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图形验证码表';

-- =============================================
-- 权限管理相关表
-- =============================================

-- 租户表 (增强)
CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '租户ID',
  `name` varchar(100) NOT NULL COMMENT '租户名称',
  `code` varchar(50) NOT NULL COMMENT '租户编码',
  `type` varchar(20) DEFAULT 'STANDARD' COMMENT '租户类型(SYSTEM/ENTERPRISE/STANDARD/PERSONAL)',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父租户ID',
  `level` int DEFAULT 1 COMMENT '租户层级',
  `status` varchar(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE/SUSPENDED)',
  `domain` varchar(100) DEFAULT NULL COMMENT '自定义域名',
  `logo` varchar(200) DEFAULT NULL COMMENT '租户Logo',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `industry` varchar(50) DEFAULT NULL COMMENT '所属行业',
  `scale` varchar(20) DEFAULT NULL COMMENT '企业规模',
  `region` varchar(50) DEFAULT NULL COMMENT '所在地区',
  `max_users` int DEFAULT 100 COMMENT '最大用户数',
  `max_storage` bigint DEFAULT 1073741824 COMMENT '最大存储空间(字节)',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐ID',
  `feature_config` json DEFAULT NULL COMMENT '功能配置',
  `theme_config` json DEFAULT NULL COMMENT '主题配置',
  `security_config` json DEFAULT NULL COMMENT '安全配置',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type_status` (`type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户信息表';

-- 组织结构表
CREATE TABLE `sys_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父组织ID',
  `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
  `org_name` varchar(50) NOT NULL COMMENT '组织名称',
  `org_code` varchar(50) NOT NULL COMMENT '组织编码',
  `org_type` varchar(20) DEFAULT 'DEPT' COMMENT '组织类型(COMPANY/DEPT/TEAM/PROJECT)',
  `level` int DEFAULT 1 COMMENT '组织层级',
  `sort` int DEFAULT 0 COMMENT '显示顺序',
  `leader_id` bigint(20) DEFAULT NULL COMMENT '负责人ID',
  `leader_name` varchar(50) DEFAULT NULL COMMENT '负责人姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '办公地址',
  `description` text DEFAULT NULL COMMENT '组织描述',
  `status` char(1) DEFAULT '1' COMMENT '状态(1正常0停用)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_org_code` (`tenant_id`, `org_code`),
  KEY `idx_tenant_parent` (`tenant_id`, `parent_id`),
  KEY `idx_leader_id` (`leader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织结构表';

-- 用户组织关联表
CREATE TABLE `sys_user_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `org_id` bigint(20) NOT NULL COMMENT '组织ID',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `job_level` varchar(20) DEFAULT NULL COMMENT '职级',
  `is_primary` tinyint(1) DEFAULT 0 COMMENT '是否主要组织',
  `is_leader` tinyint(1) DEFAULT 0 COMMENT '是否负责人',
  `join_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `leave_time` datetime DEFAULT NULL COMMENT '离开时间',
  `status` char(1) DEFAULT '1' COMMENT '状态(1正常0停用)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_org` (`user_id`, `org_id`),
  KEY `idx_tenant_user` (`tenant_id`, `user_id`),
  KEY `idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组织关联表';

-- 角色表 (增强)
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID(NULL表示系统角色)',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_type` varchar(20) DEFAULT 'CUSTOM' COMMENT '角色类型(SYSTEM/TENANT/CUSTOM)',
  `data_scope` varchar(20) DEFAULT 'SELF' COMMENT '数据范围(ALL/TENANT/ORG/DEPT/SELF/CUSTOM)',
  `data_scope_rule` json DEFAULT NULL COMMENT '自定义数据范围规则',
  `sort` int DEFAULT 0 COMMENT '显示顺序',
  `status` char(1) DEFAULT '1' COMMENT '角色状态(1正常0停用)',
  `is_default` tinyint(1) DEFAULT 0 COMMENT '是否默认角色',
  `is_system` tinyint(1) DEFAULT 0 COMMENT '是否系统角色',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_role_key` (`tenant_id`, `role_key`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- 用户角色关联表 (增强)
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `org_id` bigint(20) DEFAULT NULL COMMENT '组织ID(组织内角色)',
  `assign_type` varchar(20) DEFAULT 'MANUAL' COMMENT '分配方式(MANUAL/AUTO/INHERIT)',
  `assign_by` bigint(20) DEFAULT NULL COMMENT '分配人',
  `assign_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `status` char(1) DEFAULT '1' COMMENT '状态(1正常0停用)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role_org` (`user_id`, `role_id`, `org_id`),
  KEY `idx_tenant_user` (`tenant_id`, `user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 菜单权限表
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int DEFAULT 1 COMMENT '是否为外链(0是1否)',
  `is_cache` int DEFAULT 0 COMMENT '是否缓存(0缓存1不缓存)',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型(M目录C菜单F按钮)',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态(0显示1隐藏)',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态(0正常1停用)',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID(NULL表示系统菜单)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 角色菜单关联表
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- =============================================
-- 审计和日志表
-- =============================================

-- 登录日志表
CREATE TABLE `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `trace_id` varchar(64) DEFAULT NULL COMMENT '链路追踪ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `login_method` varchar(20) NOT NULL COMMENT '登录方式(PASSWORD/SMS/SOCIAL/SSO)',
  `social_type` varchar(20) DEFAULT NULL COMMENT '社交类型',
  `login_result` varchar(20) NOT NULL COMMENT '登录结果(SUCCESS/FAILURE)',
  `failure_reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `client_id` varchar(100) DEFAULT NULL COMMENT 'OAuth2客户端ID',
  `client_ip` varchar(45) DEFAULT NULL COMMENT '客户端IP',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `device_type` varchar(50) DEFAULT NULL COMMENT '设备类型',
  `browser_type` varchar(50) DEFAULT NULL COMMENT '浏览器类型',
  `os_type` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `login_location` varchar(100) DEFAULT NULL COMMENT '登录地点',
  `session_id` varchar(100) DEFAULT NULL COMMENT '会话ID',
  `access_token_jti` varchar(100) DEFAULT NULL COMMENT '访问令牌JTI',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `logout_time` datetime DEFAULT NULL COMMENT '注销时间',
  `session_duration` bigint DEFAULT NULL COMMENT '会话时长(秒)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_login_time` (`login_time`),
  KEY `idx_client_ip` (`client_ip`),
  KEY `idx_login_method` (`login_method`),
  KEY `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 权限操作日志表
CREATE TABLE `permission_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '操作ID',
  `trace_id` varchar(64) DEFAULT NULL COMMENT '链路追踪ID',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) NOT NULL COMMENT '操作人姓名',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `target_type` varchar(50) NOT NULL COMMENT '目标类型(USER/ROLE/ORG/PERMISSION)',
  `target_id` bigint(20) DEFAULT NULL COMMENT '目标ID',
  `target_name` varchar(100) DEFAULT NULL COMMENT '目标名称',
  `operation_desc` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `before_data` json DEFAULT NULL COMMENT '操作前数据',
  `after_data` json DEFAULT NULL COMMENT '操作后数据',
  `client_ip` varchar(45) DEFAULT NULL COMMENT '客户端IP',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `operation_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_operation_time` (`operation_time`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限操作日志表';

-- JWT令牌黑名单表
CREATE TABLE `jwt_token_blacklist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '黑名单ID',
  `jti` varchar(100) NOT NULL COMMENT 'JWT令牌ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `token_type` varchar(20) DEFAULT 'ACCESS' COMMENT '令牌类型(ACCESS/REFRESH)',
  `revoke_reason` varchar(100) DEFAULT NULL COMMENT '撤销原因',
  `revoke_by` bigint(20) DEFAULT NULL COMMENT '撤销人',
  `revoke_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '撤销时间',
  `expire_time` datetime NOT NULL COMMENT '令牌原始过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_jti` (`jti`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='JWT令牌黑名单表';

-- =============================================
-- 初始化数据
-- =============================================

-- 初始化OAuth2.1客户端
INSERT INTO `oauth2_registered_client` VALUES 
('web-admin-client-id', 'web-admin-client', NOW(), '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', NULL, 'Web管理端', 'client_secret_basic', 'authorization_code,refresh_token,password,sms_code,social_login', 'http://localhost:5666/auth/callback', 'http://localhost:5666/logout', 'openid,profile,email,admin,user,tenant:read,tenant:write,org:read,org:write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",604800.000000000]}', NOW(), NOW());

INSERT INTO `oauth2_registered_client` VALUES 
('mobile-client-id', 'mobile-client', NOW(), NULL, NULL, '移动应用', 'none', 'authorization_code,refresh_token,sms_code', 'com.yourcompany.app://oauth/callback', 'com.yourcompany.app://logout', 'openid,profile,user,tenant:read', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.access-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.refresh-token-time-to-live":["java.time.Duration",604800.000000000]}', NOW(), NOW());

-- 初始化系统租户
INSERT INTO `sys_tenant` VALUES 
(1, '系统租户', 'system', 'SYSTEM', NULL, 0, 'ACTIVE', NULL, NULL, '系统管理员', '13800138000', 'admin@system.com', 'IT', 'LARGE', 'Beijing', 10000, 1073741824000, NULL, NULL, '{"userManagement":true,"roleManagement":true,"orgManagement":true,"systemConfig":true}', '{"primaryColor":"#1890ff","theme":"light"}', '{"passwordComplexity":true,"sessionTimeout":120,"maxLoginAttempts":5}', 'system', NOW(), 'system', NOW(), 0, '系统默认租户');

INSERT INTO `sys_tenant` VALUES 
(2, '演示企业', 'demo', 'ENTERPRISE', NULL, 1, 'ACTIVE', 'demo.yourdomain.com', NULL, '张三', '13800138001', 'demo@company.com', '制造业', 'MEDIUM', 'Shanghai', 500, 5368709120, NULL, NULL, '{"userManagement":true,"roleManagement":true,"orgManagement":true}', '{"primaryColor":"#52c41a","theme":"light"}', '{"passwordComplexity":true,"sessionTimeout":60,"maxLoginAttempts":3}', 'admin', NOW(), 'admin', NOW(), 0, '演示企业租户');

-- 初始化组织结构
INSERT INTO `sys_organization` VALUES 
(1, 1, 0, '0', '系统总部', 'SYS_HQ', 'COMPANY', 1, 0, 1, '系统管理员', '13800138000', 'admin@system.com', '北京市海淀区', '系统管理总部', '1', 'system', NOW(), 'system', NOW(), 0, '系统总部组织'),
(2, 2, 0, '0', '演示公司', 'DEMO_COMPANY', 'COMPANY', 1, 0, 2, '张三', '13800138001', 'demo@company.com', '上海市浦东新区', '演示公司总部', '1', 'admin', NOW(), 'admin', NOW(), 0, '演示公司'),
(3, 2, 2, '0,2', '技术部', 'TECH_DEPT', 'DEPT', 2, 1, 3, '李四', '13800138002', 'tech@company.com', '上海市浦东新区', '技术研发部门', '1', 'admin', NOW(), 'admin', NOW(), 0, '技术部'),
(4, 2, 2, '0,2', '销售部', 'SALES_DEPT', 'DEPT', 2, 2, 4, '王五', '13800138003', 'sales@company.com', '上海市浦东新区', '销售业务部门', '1', 'admin', NOW(), 'admin', NOW(), 0, '销售部');

-- 初始化系统角色
INSERT INTO `sys_role` VALUES 
(1, NULL, '超级管理员', 'SUPER_ADMIN', 'SYSTEM', 'ALL', NULL, 1, '1', 1, 1, 'system', NOW(), 'system', NOW(), 0, '超级管理员角色'),
(2, 1, '系统管理员', 'SYSTEM_ADMIN', 'SYSTEM', 'TENANT', NULL, 2, '1', 0, 1, 'system', NOW(), 'system', NOW(), 0, '系统管理员角色'),
(3, 2, '租户管理员', 'TENANT_ADMIN', 'TENANT', 'TENANT', NULL, 1, '1', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '租户管理员角色'),
(4, 2, '组织管理员', 'ORG_ADMIN', 'CUSTOM', 'ORG', NULL, 2, '1', 0, 0, 'admin', NOW(), 'admin', NOW(), 0, '组织管理员角色'),
(5, 2, '部门管理员', 'DEPT_ADMIN', 'CUSTOM', 'DEPT', NULL, 3, '1', 0, 0, 'admin', NOW(), 'admin', NOW(), 0, '部门管理员角色'),
(6, 2, '普通用户', 'USER', 'CUSTOM', 'SELF', NULL, 4, '1', 1, 0, 'admin', NOW(), 'admin', NOW(), 0, '普通用户角色');

-- 初始化用户
INSERT INTO `sys_user` VALUES 
(1, 1, 'admin', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', '超级管理员', '超级管理员', NULL, 'admin@system.com', '13800138000', 0, NULL, 'SUPER_ADMIN', 'SA001', 1, NULL, 0, NOW(), '127.0.0.1', NOW(), NULL, 'system', NOW(), 'system', NOW(), 0, '超级管理员用户'),
(2, 2, 'demo_admin', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', '演示管理员', '张三', NULL, 'demo@company.com', '13800138001', 1, '1990-01-01', 'ADMIN', 'EMP001', 2, NULL, 0, NULL, NULL, NOW(), NULL, 'admin', NOW(), 'admin', NOW(), 0, '演示企业管理员'),
(3, 2, 'demo_user', '{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', '演示用户', '李四', NULL, 'user@company.com', '13800138002', 1, '1992-05-15', 'EMPLOYEE', 'EMP002', 3, 2, 0, NULL, NULL, NOW(), NULL, 'admin', NOW(), 'admin', NOW(), 0, '演示普通用户');

-- 初始化用户角色关联
INSERT INTO `sys_user_role` VALUES 
(1, 1, 1, 1, NULL, 'MANUAL', 1, NOW(), NULL, '1', NOW()),
(2, 2, 2, 3, 2, 'MANUAL', 1, NOW(), NULL, '1', NOW()),
(3, 2, 3, 6, 3, 'MANUAL', 2, NOW(), NULL, '1', NOW());

-- 初始化用户组织关联
INSERT INTO `sys_user_organization` VALUES 
(1, 1, 1, 1, '系统架构师', 'P10', 1, 1, NOW(), NULL, '1', NOW(), NOW()),
(2, 2, 2, 2, '技术总监', 'M3', 1, 1, NOW(), NULL, '1', NOW(), NOW()),
(3, 2, 3, 3, '高级开发工程师', 'P7', 1, 0, NOW(), NULL, '1', NOW(), NOW());

-- 初始化系统菜单
INSERT INTO `sys_menu` VALUES 
(1, '系统管理', 0, 1, '/system', 'Layout', '', 1, 0, 'M', '0', '0', '', 'system', NULL, 'system', NOW(), '', NOW(), '系统管理目录'),
(2, '用户管理', 1, 1, '/system/user', '/system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', NULL, 'system', NOW(), '', NOW(), '用户管理菜单'),
(3, '角色管理', 1, 2, '/system/role', '/system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', NULL, 'system', NOW(), '', NOW(), '角色管理菜单'),
(4, '组织管理', 1, 3, '/system/org', '/system/org/index', '', 1, 0, 'C', '0', '0', 'system:org:list', 'tree', NULL, 'system', NOW(), '', NOW(), '组织管理菜单'),
(5, '租户管理', 1, 4, '/system/tenant', '/system/tenant/index', '', 1, 0, 'C', '0', '0', 'system:tenant:list', 'company', NULL, 'system', NOW(), '', NOW(), '租户管理菜单');

-- 初始化角色菜单关联
INSERT INTO `sys_role_menu` VALUES 
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), -- 超级管理员拥有所有菜单
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), -- 系统管理员拥有所有菜单
(3, 1), (3, 2), (3, 3), (3, 4),         -- 租户管理员拥有基础管理菜单
(4, 1), (4, 2), (4, 4),                 -- 组织管理员拥有用户和组织管理
(5, 1), (5, 2),                         -- 部门管理员拥有用户管理
(6, 1);                                 -- 普通用户只能查看系统菜单

-- 创建索引优化
CREATE INDEX idx_oauth2_auth_principal_client ON oauth2_authorization(principal_name, registered_client_id);
CREATE INDEX idx_oauth2_auth_access_token_expires ON oauth2_authorization(access_token_expires_at);
CREATE INDEX idx_oauth2_auth_refresh_token_expires ON oauth2_authorization(refresh_token_expires_at);

-- 创建视图简化查询
CREATE VIEW `v_user_permission_summary` AS
SELECT 
    u.id as user_id,
    u.username,
    u.tenant_id,
    t.name as tenant_name,
    GROUP_CONCAT(DISTINCT r.role_name) as roles,
    GROUP_CONCAT(DISTINCT m.perms) as permissions,
    MAX(r.data_scope) as data_scope
FROM sys_user u
LEFT JOIN sys_tenant t ON u.tenant_id = t.id
LEFT JOIN sys_user_role ur ON u.id = ur.user_id AND ur.status = '1'
LEFT JOIN sys_role r ON ur.role_id = r.id AND r.status = '1'
LEFT JOIN sys_role_menu rm ON r.id = rm.role_id
LEFT JOIN sys_menu m ON rm.menu_id = m.id AND m.status = '0'
WHERE u.del_flag = 0 AND u.status = 0
GROUP BY u.id, u.username, u.tenant_id, t.name;
