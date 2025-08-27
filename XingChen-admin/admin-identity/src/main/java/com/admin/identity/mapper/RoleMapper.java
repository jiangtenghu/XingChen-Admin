package com.admin.identity.mapper;

import com.admin.identity.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper接口
 *
 * @author admin
 * @since 2024-01-15
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据租户ID分页查询角色
     *
     * @param page 分页参数
     * @param tenantId 租户ID
     * @param roleName 角色名称（可选）
     * @param status 状态（可选）
     * @return 角色分页列表
     */
    IPage<Role> selectRolePageByTenant(Page<Role> page, 
                                      @Param("tenantId") Long tenantId,
                                      @Param("roleName") String roleName,
                                      @Param("status") String status);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据角色Key查询角色
     *
     * @param roleKey 角色标识
     * @param tenantId 租户ID
     * @return 角色信息
     */
    Role selectByRoleKey(@Param("roleKey") String roleKey, @Param("tenantId") Long tenantId);

    /**
     * 检查角色Key是否存在
     *
     * @param roleKey 角色标识
     * @param tenantId 租户ID
     * @param excludeId 排除的角色ID
     * @return 是否存在
     */
    boolean existsByRoleKey(@Param("roleKey") String roleKey, 
                           @Param("tenantId") Long tenantId,
                           @Param("excludeId") Long excludeId);

    /**
     * 根据租户ID查询角色数量
     *
     * @param tenantId 租户ID
     * @return 角色数量
     */
    int countByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色ID列表
     * @return 删除数量
     */
    int deleteByIds(@Param("roleIds") List<Long> roleIds);
}
