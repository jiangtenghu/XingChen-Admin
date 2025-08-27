package com.admin.identity.mapper;

import com.admin.identity.domain.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper接口
 *
 * @author admin
 * @since 2024-01-15
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 分页查询权限
     *
     * @param page 分页参数
     * @param permissionName 权限名称（可选）
     * @param permissionType 权限类型（可选）
     * @param status 状态（可选）
     * @return 权限分页列表
     */
    IPage<Permission> selectPermissionPage(Page<Permission> page,
                                         @Param("permissionName") String permissionName,
                                         @Param("permissionType") String permissionType,
                                         @Param("status") String status);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据权限Key查询权限
     *
     * @param permissionKey 权限标识
     * @return 权限信息
     */
    Permission selectByPermissionKey(@Param("permissionKey") String permissionKey);

    /**
     * 检查权限Key是否存在
     *
     * @param permissionKey 权限标识
     * @param excludeId 排除的权限ID
     * @return 是否存在
     */
    boolean existsByPermissionKey(@Param("permissionKey") String permissionKey,
                                 @Param("excludeId") Long excludeId);

    /**
     * 根据父权限ID查询子权限
     *
     * @param parentId 父权限ID
     * @return 子权限列表
     */
    List<Permission> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 查询菜单类型权限树
     *
     * @return 菜单权限列表
     */
    List<Permission> selectMenuPermissions();

    /**
     * 批量删除权限
     *
     * @param permissionIds 权限ID列表
     * @return 删除数量
     */
    int deleteByIds(@Param("permissionIds") List<Long> permissionIds);
}
