package com.admin.identity.service;

import com.admin.identity.domain.dto.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Set;

/**
 * 权限管理服务接口
 *
 * @author admin
 * @since 2024-01-15
 */
public interface PermissionManagementService {

    // ===== 角色管理 =====

    /**
     * 创建角色
     *
     * @param tenantId 租户ID
     * @param createDTO 角色创建DTO
     * @return 角色ID
     */
    Long createRole(Long tenantId, RoleCreateDTO createDTO);

    /**
     * 更新角色
     *
     * @param tenantId 租户ID
     * @param updateDTO 角色更新DTO
     * @return 是否成功
     */
    Boolean updateRole(Long tenantId, RoleUpdateDTO updateDTO);

    /**
     * 删除角色
     *
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    Boolean deleteRole(Long tenantId, Long roleId);

    /**
     * 分页查询角色列表
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<RoleResponseDTO> getRolePage(Long tenantId, RoleQueryDTO queryDTO);

    /**
     * 查询角色详情
     *
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @return 角色详情
     */
    RoleDetailDTO getRoleDetail(Long tenantId, Long roleId);

    /**
     * 分配权限给角色
     *
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    Boolean assignPermissionsToRole(Long tenantId, Long roleId, List<Long> permissionIds);

    /**
     * 移除角色权限
     *
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    Boolean removePermissionsFromRole(Long tenantId, Long roleId, List<Long> permissionIds);

    /**
     * 查询角色权限列表
     *
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionResponseDTO> getRolePermissions(Long tenantId, Long roleId);

    /**
     * 查询角色权限树
     *
     * @param tenantId 租户ID
     * @param roleId 角色ID
     * @return 权限树
     */
    List<PermissionResponseDTO> getRolePermissionTree(Long tenantId, Long roleId);

    // ===== 权限管理 =====

    /**
     * 创建权限
     *
     * @param createDTO 权限创建DTO
     * @return 权限ID
     */
    Long createPermission(PermissionCreateDTO createDTO);

    /**
     * 更新权限
     *
     * @param updateDTO 权限更新DTO
     * @return 是否成功
     */
    Boolean updatePermission(PermissionUpdateDTO updateDTO);

    /**
     * 删除权限
     *
     * @param permissionId 权限ID
     * @return 是否成功
     */
    Boolean deletePermission(Long permissionId);

    /**
     * 分页查询权限列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<PermissionResponseDTO> getPermissionPage(PermissionQueryDTO queryDTO);

    /**
     * 查询权限树
     *
     * @param queryDTO 查询条件
     * @return 权限树
     */
    List<PermissionResponseDTO> getPermissionTree(PermissionQueryDTO queryDTO);

    /**
     * 查询权限详情
     *
     * @param permissionId 权限ID
     * @return 权限详情
     */
    PermissionResponseDTO getPermissionDetail(Long permissionId);

    // ===== 用户权限管理 =====

    /**
     * 获取用户的所有权限
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 权限集合
     */
    Set<String> getUserPermissions(Long tenantId, Long userId);

    /**
     * 获取用户的权限树
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 权限树
     */
    List<PermissionResponseDTO> getUserPermissionTree(Long tenantId, Long userId);

    /**
     * 检查用户是否有指定权限
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param permission 权限标识
     * @return 是否有权限
     */
    Boolean hasPermission(Long tenantId, Long userId, String permission);

    /**
     * 检查用户是否有任一权限
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param permissions 权限标识列表
     * @return 是否有权限
     */
    Boolean hasAnyPermission(Long tenantId, Long userId, List<String> permissions);

    /**
     * 检查用户是否有所有权限
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param permissions 权限标识列表
     * @return 是否有权限
     */
    Boolean hasAllPermissions(Long tenantId, Long userId, List<String> permissions);

    /**
     * 直接授权权限给用户
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param permissionIds 权限ID列表
     * @param expireTime 过期时间
     * @param reason 授权原因
     * @return 是否成功
     */
    Boolean grantPermissionsToUser(Long tenantId, Long userId, List<Long> permissionIds, 
                                  java.time.LocalDateTime expireTime, String reason);

    /**
     * 撤销用户的直接权限
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param permissionIds 权限ID列表
     * @return 是否成功
     */
    Boolean revokePermissionsFromUser(Long tenantId, Long userId, List<Long> permissionIds);

    // ===== 数据权限管理 =====

    /**
     * 检查用户的数据权限
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param resource 资源标识
     * @param resourceId 资源ID
     * @return 是否有权限
     */
    Boolean hasDataPermission(Long tenantId, Long userId, String resource, Long resourceId);

    /**
     * 获取用户的数据权限范围
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param resource 资源标识
     * @return 数据权限范围
     */
    DataPermissionScope getDataPermissionScope(Long tenantId, Long userId, String resource);

    /**
     * 构建数据权限SQL过滤条件
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param resource 资源标识
     * @param tableAlias 表别名
     * @return SQL过滤条件
     */
    String buildDataPermissionFilter(Long tenantId, Long userId, String resource, String tableAlias);

    // ===== 权限审计和统计 =====

    /**
     * 获取权限统计信息
     *
     * @param tenantId 租户ID
     * @return 统计信息
     */
    PermissionStatisticsDTO getPermissionStatistics(Long tenantId);

    /**
     * 权限审计
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询条件
     * @return 审计结果
     */
    IPage<PermissionAuditDTO> auditPermissions(Long tenantId, PermissionAuditQueryDTO queryDTO);

    /**
     * 权限使用分析
     *
     * @param tenantId 租户ID
     * @param days 分析天数
     * @return 分析结果
     */
    List<PermissionUsageDTO> analyzePermissionUsage(Long tenantId, Integer days);

    // ===== 权限模板管理 =====

    /**
     * 创建权限模板
     *
     * @param tenantId 租户ID
     * @param templateDTO 模板DTO
     * @return 模板ID
     */
    Long createPermissionTemplate(Long tenantId, PermissionTemplateDTO templateDTO);

    /**
     * 应用权限模板
     *
     * @param tenantId 租户ID
     * @param templateId 模板ID
     * @param targetType 目标类型（user/role）
     * @param targetIds 目标ID列表
     * @return 是否成功
     */
    Boolean applyPermissionTemplate(Long tenantId, Long templateId, String targetType, List<Long> targetIds);

    /**
     * 权限继承处理
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 继承的权限列表
     */
    List<String> getInheritedPermissions(Long tenantId, Long userId);
}
