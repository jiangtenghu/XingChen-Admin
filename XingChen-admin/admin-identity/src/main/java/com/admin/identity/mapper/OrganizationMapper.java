package com.admin.identity.mapper;

import com.admin.identity.domain.dto.OrganizationQueryDTO;
import com.admin.identity.domain.dto.OrganizationResponseDTO;
import com.admin.identity.domain.entity.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 组织架构Mapper接口
 *
 * @author admin
 * @since 2024-01-15
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<Organization> {

    /**
     * 分页查询组织列表（带统计信息）
     */
    IPage<OrganizationResponseDTO> selectOrganizationPage(Page<OrganizationResponseDTO> page, @Param("query") OrganizationQueryDTO query);

    /**
     * 根据ID查询组织详情（带统计信息）
     */
    OrganizationResponseDTO selectOrganizationDetailById(@Param("tenantId") Long tenantId, @Param("id") Long id);

    /**
     * 查询租户的组织树（不分页）
     */
    List<OrganizationResponseDTO> selectOrganizationTree(@Param("tenantId") Long tenantId, @Param("query") OrganizationQueryDTO query);

    /**
     * 查询指定组织的子组织列表
     */
    List<OrganizationResponseDTO> selectChildOrganizations(@Param("tenantId") Long tenantId, @Param("parentId") Long parentId);

    /**
     * 查询指定组织的所有下级组织ID列表（递归）
     */
    List<Long> selectAllSubOrganizationIds(@Param("tenantId") Long tenantId, @Param("parentId") Long parentId);

    /**
     * 统计组织的成员数量
     */
    @Select("SELECT COUNT(*) FROM sys_user_organization uo " +
            "INNER JOIN sys_user u ON uo.user_id = u.id " +
            "WHERE uo.org_id = #{orgId} AND uo.status = 'ACTIVE' AND u.del_flag = 0")
    Integer countMembersByOrgId(@Param("orgId") Long orgId);

    /**
     * 统计组织的直接子组织数量
     */
    @Select("SELECT COUNT(*) FROM sys_organization WHERE tenant_id = #{tenantId} AND parent_id = #{parentId} AND del_flag = 0")
    Integer countChildrenByParentId(@Param("tenantId") Long tenantId, @Param("parentId") Long parentId);

    /**
     * 检查组织编码是否存在
     */
    @Select("SELECT COUNT(*) FROM sys_organization WHERE tenant_id = #{tenantId} AND org_code = #{orgCode} AND del_flag = 0")
    Integer countByTenantIdAndOrgCode(@Param("tenantId") Long tenantId, @Param("orgCode") String orgCode);

    /**
     * 检查组织编码是否存在（排除指定ID）
     */
    @Select("SELECT COUNT(*) FROM sys_organization WHERE tenant_id = #{tenantId} AND org_code = #{orgCode} AND id != #{excludeId} AND del_flag = 0")
    Integer countByTenantIdAndOrgCodeExcludeId(@Param("tenantId") Long tenantId, @Param("orgCode") String orgCode, @Param("excludeId") Long excludeId);

    /**
     * 检查是否存在子组织
     */
    @Select("SELECT COUNT(*) FROM sys_organization WHERE tenant_id = #{tenantId} AND parent_id = #{parentId} AND del_flag = 0")
    Integer countByTenantIdAndParentId(@Param("tenantId") Long tenantId, @Param("parentId") Long parentId);

    /**
     * 根据负责人ID查询组织列表
     */
    List<OrganizationResponseDTO> selectOrganizationsByLeaderId(@Param("tenantId") Long tenantId, @Param("leaderId") Long leaderId);

    /**
     * 批量更新组织状态
     */
    Integer batchUpdateStatus(@Param("tenantId") Long tenantId, @Param("ids") List<Long> ids, @Param("status") String status, @Param("updateBy") String updateBy);

    /**
     * 更新组织的祖级信息
     */
    Integer updateAncestors(@Param("tenantId") Long tenantId, @Param("id") Long id, @Param("ancestors") String ancestors);

    /**
     * 批量更新子组织的祖级信息
     */
    Integer batchUpdateChildrenAncestors(@Param("tenantId") Long tenantId, @Param("oldAncestors") String oldAncestors, @Param("newAncestors") String newAncestors);

    /**
     * 查询组织的最大排序号
     */
    @Select("SELECT COALESCE(MAX(sort_order), 0) FROM sys_organization WHERE tenant_id = #{tenantId} AND parent_id = #{parentId} AND del_flag = 0")
    Integer selectMaxSortOrderByParent(@Param("tenantId") Long tenantId, @Param("parentId") Long parentId);

    /**
     * 查询用户所属的组织列表
     */
    List<OrganizationResponseDTO> selectUserOrganizations(@Param("tenantId") Long tenantId, @Param("userId") Long userId);
}
