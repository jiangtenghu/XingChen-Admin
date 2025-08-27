-- =============================================
-- 数据库迁移指南
-- 从分散式架构迁移到统一身份管理架构
-- 版本: 2.0.0
-- 创建时间: 2024-01-15
-- =============================================

-- 注意事项：
-- 1. 请在执行前备份现有数据库
-- 2. 建议在测试环境先执行验证
-- 3. 执行顺序不能颠倒

-- =============================================
-- 步骤1: 备份现有数据库
-- =============================================

-- 备份命令 (在Shell中执行):
-- mysqldump -u root -p admin_user > backup/admin_user_backup_$(date +%Y%m%d_%H%M%S).sql
-- mysqldump -u root -p admin_system > backup/admin_system_backup_$(date +%Y%m%d_%H%M%S).sql

-- =============================================
-- 步骤2: 创建新的统一身份管理数据库
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS admin_identity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =============================================
-- 步骤3: 数据迁移 (如果有现有数据)
-- =============================================

-- 3.1 从 admin_user 迁移用户数据
INSERT INTO admin_identity.sys_user (
    id, tenant_id, username, password, nickname, real_name, email, phone,
    sex, avatar, birthday, address, employee_no, entry_date, position, superior_id,
    user_type, status, login_date, login_ip, create_by, create_time, update_by, update_time, del_flag, remark
)
SELECT 
    id, tenant_id, username, password, nickname, real_name, email, phone,
    sex, avatar, birthday, address, employee_no, entry_date, position, superior_id,
    user_type, status, login_date, login_ip, create_by, create_time, update_by, update_time, del_flag, remark
FROM admin_user.sys_user 
WHERE del_flag = 0;

-- 3.2 从 admin_user 迁移租户数据 (如果存在)
INSERT INTO admin_identity.sys_tenant (
    id, name, code, type, status, parent_id, level, sort_order,
    contact_person, contact_phone, contact_email, industry, scale, region, business_license,
    max_users, max_storage, max_departments, max_roles, package_id, expire_time, domain, logo,
    create_by, create_time, update_by, update_time, del_flag, remark
)
SELECT 
    id, name, code, type, status, parent_id, level, sort_order,
    contact_person, contact_phone, contact_email, industry, scale, region, business_license,
    max_users, max_storage, max_departments, max_roles, package_id, expire_time, domain, logo,
    create_by, create_time, update_by, update_time, del_flag, remark
FROM admin_user.sys_tenant 
WHERE del_flag = 0;

-- 3.3 从 admin_system 迁移角色数据
INSERT INTO admin_identity.sys_role (
    id, tenant_id, role_name, role_key, role_sort, data_scope, data_scope_org_ids,
    role_type, status, effective_time, expire_time, create_by, create_time, update_by, update_time, del_flag, remark
)
SELECT 
    id, 1 as tenant_id, role_name, role_key, sort as role_sort, data_scope, '' as data_scope_org_ids,
    'CUSTOM' as role_type, status, NULL as effective_time, NULL as expire_time,
    create_by, create_time, update_by, update_time, del_flag, remark
FROM admin_system.sys_role 
WHERE del_flag = 0;

-- 3.4 迁移权限数据 (从菜单表提取权限)
INSERT INTO admin_identity.sys_permission (
    id, permission_name, permission_key, permission_type, parent_id, level, sort_order,
    resource_path, http_method, component_path, icon, status, is_system,
    create_by, create_time, update_by, update_time, del_flag, remark
)
SELECT 
    id, menu_name as permission_name, perms as permission_key, 
    CASE menu_type 
        WHEN 'M' THEN 'MENU'
        WHEN 'C' THEN 'MENU' 
        WHEN 'F' THEN 'BUTTON'
        ELSE 'MENU'
    END as permission_type,
    parent_id, 1 as level, order_num as sort_order,
    path as resource_path, 'GET' as http_method, component as component_path, icon,
    status, 1 as is_system, create_by, create_time, update_by, update_time, 0 as del_flag, remark
FROM admin_system.sys_menu 
WHERE perms IS NOT NULL AND perms != '';

-- 3.5 迁移用户角色关联数据 (如果存在)
INSERT INTO admin_identity.sys_user_role (
    tenant_id, user_id, role_id, assign_time, assign_by, assign_reason,
    effective_time, expire_time, status, create_time, update_time
)
SELECT 
    1 as tenant_id, user_id, role_id, NOW() as assign_time, 'system' as assign_by, '系统迁移' as assign_reason,
    NOW() as effective_time, NULL as expire_time, '0' as status, NOW() as create_time, NOW() as update_time
FROM admin_system.sys_user_role;

-- 3.6 迁移角色权限关联数据 (如果存在)
INSERT INTO admin_identity.sys_role_permission (
    role_id, permission_id, create_time
)
SELECT DISTINCT
    rm.role_id, p.id as permission_id, NOW() as create_time
FROM admin_system.sys_role_menu rm
INNER JOIN admin_system.sys_menu m ON rm.menu_id = m.id
INNER JOIN admin_identity.sys_permission p ON p.permission_key = m.perms
WHERE m.perms IS NOT NULL AND m.perms != '';

-- =============================================
-- 步骤4: 数据验证
-- =============================================

-- 验证数据迁移结果
SELECT '=== 数据迁移验证结果 ===' as message;

SELECT 
    '租户数据' as table_name,
    COUNT(*) as record_count
FROM admin_identity.sys_tenant
UNION ALL
SELECT 
    '用户数据' as table_name,
    COUNT(*) as record_count  
FROM admin_identity.sys_user
UNION ALL
SELECT 
    '角色数据' as table_name,
    COUNT(*) as record_count
FROM admin_identity.sys_role
UNION ALL
SELECT 
    '权限数据' as table_name, 
    COUNT(*) as record_count
FROM admin_identity.sys_permission
UNION ALL
SELECT 
    '用户角色关联' as table_name,
    COUNT(*) as record_count
FROM admin_identity.sys_user_role
UNION ALL
SELECT 
    '角色权限关联' as table_name,
    COUNT(*) as record_count
FROM admin_identity.sys_role_permission;

-- =============================================
-- 步骤5: 清理说明
-- =============================================

-- 迁移完成后的清理步骤:
-- 1. 验证新系统功能正常
-- 2. 运行一段时间确保稳定
-- 3. 可选择保留旧数据库作为备份
-- 4. 更新应用配置指向新数据库

-- 旧数据库备份保留建议:
-- DROP DATABASE admin_user;    -- 谨慎执行
-- DROP DATABASE admin_system;  -- 谨慎执行 (保留菜单数据)

SELECT '数据库迁移脚本执行完成！' as message;
SELECT '请验证数据完整性后再继续下一步操作。' as next_step;
