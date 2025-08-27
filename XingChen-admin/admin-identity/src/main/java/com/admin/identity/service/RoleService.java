package com.admin.identity.service;

import com.admin.identity.domain.entity.Role;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author admin
 * @since 2024-01-15
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据租户ID分页查询角色
     *
     * @param page 分页参数
     * @param tenantId 租户ID
     * @param roleName 角色名称（可选）
     * @param status 状态（可选）
     * @return 角色分页列表
     */
    IPage<Role> pageByTenant(Page<Role> page, Long tenantId, String roleName, String status);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> listByUserId(Long userId);

    /**
     * 根据角色Key查询角色
     *
     * @param roleKey 角色标识
     * @param tenantId 租户ID
     * @return 角色信息
     */
    Role getByRoleKey(String roleKey, Long tenantId);

    /**
     * 检查角色Key是否存在
     *
     * @param roleKey 角色标识
     * @param tenantId 租户ID
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsByRoleKey(String roleKey, Long tenantId, Long excludeId);

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 创建成功的角色
     */
    Role createRole(Role role);

    /**
     * 更新角色
     *
     * @param role 角色信息
     * @return 更新成功的角色
     */
    Role updateRole(Role role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 是否删除成功
     */
    boolean deleteRole(Long roleId);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色ID列表
     * @return 是否删除成功
     */
    boolean deleteBatch(List<Long> roleIds);

    /**
     * 分配角色权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 是否分配成功
     */
    boolean assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 根据租户ID统计角色数量
     *
     * @param tenantId 租户ID
     * @return 角色数量
     */
    int countByTenantId(Long tenantId);

    /**
     * 启用/禁用角色
     *
     * @param roleId 角色ID
     * @param status 状态（0-正常 1-停用）
     * @return 是否操作成功
     */
    boolean changeStatus(Long roleId, String status);
}
