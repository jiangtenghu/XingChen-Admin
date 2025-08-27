-- 租户管理相关表结构
-- 使用用户数据库
USE `admin_user`;

-- 租户表
CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL COMMENT '租户ID',
  `name` varchar(50) NOT NULL COMMENT '租户名称',
  `code` varchar(50) NOT NULL COMMENT '租户编码',
  `status` varchar(10) DEFAULT 'ON' COMMENT '租户状态（ON启用 OFF禁用）',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `package_id` bigint(20) DEFAULT NULL COMMENT '套餐ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标志（0代表存在 1代表删除）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户信息表';

-- 设置字符集
SET NAMES utf8mb4;

-- 清空现有数据
DELETE FROM `sys_tenant`;

-- 插入初始租户数据
INSERT INTO `sys_tenant` VALUES (1, '默认租户', 'default', 'ON', '管理员', '13800138000', 'admin@example.com', NULL, NULL, 'admin', NOW(), '', NOW(), 0, '系统默认租户');
INSERT INTO `sys_tenant` VALUES (2, '测试租户', 'test', 'ON', '测试用户', '13800138001', 'test@example.com', NULL, NULL, 'admin', NOW(), '', NOW(), 0, '测试租户');
