package com.admin.identity.mapper;

import com.admin.identity.domain.dto.TenantQueryDTO;
import com.admin.identity.domain.dto.TenantResponseDTO;
import com.admin.identity.domain.entity.Tenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 租户Mapper接口
 *
 * @author admin
 * @since 2024-01-15
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 分页查询租户列表（带统计信息）
     */
    IPage<TenantResponseDTO> selectTenantPage(Page<TenantResponseDTO> page, @Param("query") TenantQueryDTO query);

    /**
     * 根据ID查询租户详情（带统计信息）
     */
    TenantResponseDTO selectTenantDetailById(@Param("id") Long id);

    /**
     * 查询租户的子租户列表
     */
    List<TenantResponseDTO> selectSubTenants(@Param("parentId") Long parentId);

    /**
     * 统计租户的用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE tenant_id = #{tenantId} AND del_flag = 0")
    Integer countUsersByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 统计租户的部门数量
     */
    @Select("SELECT COUNT(*) FROM sys_organization WHERE tenant_id = #{tenantId} AND del_flag = 0")
    Integer countDepartmentsByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 统计租户的角色数量
     */
    @Select("SELECT COUNT(*) FROM sys_role WHERE tenant_id = #{tenantId} AND del_flag = 0")
    Integer countRolesByTenantId(@Param("tenantId") Long tenantId);

    /**
     * 检查租户编码是否存在
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE code = #{code} AND del_flag = 0")
    Integer countByCode(@Param("code") String code);

    /**
     * 检查租户编码是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE code = #{code} AND id != #{excludeId} AND del_flag = 0")
    Integer countByCodeExcludeId(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * 检查域名是否存在
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE domain = #{domain} AND del_flag = 0")
    Integer countByDomain(@Param("domain") String domain);

    /**
     * 检查域名是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM sys_tenant WHERE domain = #{domain} AND id != #{excludeId} AND del_flag = 0")
    Integer countByDomainExcludeId(@Param("domain") String domain, @Param("excludeId") Long excludeId);

    /**
     * 查询即将过期的租户列表
     */
    List<Tenant> selectExpiringTenants(@Param("days") Integer days);

    /**
     * 查询已过期的租户列表
     */
    List<Tenant> selectExpiredTenants();

    /**
     * 批量更新租户状态
     */
    Integer batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status, @Param("updateBy") String updateBy);
}
