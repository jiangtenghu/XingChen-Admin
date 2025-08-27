package com.admin.identity.service;

import com.admin.identity.domain.entity.Permission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author admin
 * @since 2024-01-15
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 分页查询权限
     *
     * @param page 分页参数
     * @param permissionName 权限名称（可选）
     * @param permissionType 权限类型（可选）
     * @param status 状态（可选）
     * @return 权限分页列表
     */
    IPage<Permission> pagePermissions(Page<Permission> page, String permissionName, String permissionType, String status);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> listByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> listByUserId(Long userId);

    /**
     * 根据权限Key查询权限
     *
     * @param permissionKey 权限标识
     * @return 权限信息
     */
    Permission getByPermissionKey(String permissionKey);

    /**
     * 检查权限Key是否存在
     *
     * @param permissionKey 权限标识
     * @param excludeId 排除的权限ID
     * @return 是否存在
     */
    boolean existsByPermissionKey(String permissionKey, Long excludeId);

    /**
     * 创建权限
     *
     * @param permission 权限信息
     * @return 创建成功的权限
     */
    Permission createPermission(Permission permission);

    /**
     * 更新权限
     *
     * @param permission 权限信息
     * @return 更新成功的权限
     */
    Permission updatePermission(Permission permission);

    /**
     * 删除权限
     *
     * @param permissionId 权限ID
     * @return 是否删除成功
     */
    boolean deletePermission(Long permissionId);

    /**
     * 批量删除权限
     *
     * @param permissionIds 权限ID列表
     * @return 是否删除成功
     */
    boolean deleteBatch(List<Long> permissionIds);

    /**
     * 根据父权限ID查询子权限
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> listByParentId(Long parentId);

    /**
     * 查询菜单类型权限树
     *
     * @return 菜单权限树
     */
    List<Permission> getMenuTree();

    /**
     * 构建权限树
     *
     * @param permissions 权限列表
     * @return 权限树
     */
    List<Permission> buildPermissionTree(List<Permission> permissions);

    /**
     * 启用/禁用权限
     *
     * @param permissionId 权限ID
     * @param status 状态（0-正常 1-停用）
     * @return 是否操作成功
     */
    boolean changeStatus(Long permissionId, String status);

    /**
     * 根据用户ID获取权限标识列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getPermissionKeysByUserId(Long userId);
}
