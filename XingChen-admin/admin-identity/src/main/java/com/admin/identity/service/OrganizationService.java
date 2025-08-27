package com.admin.identity.service;

import com.admin.identity.domain.dto.*;
import com.admin.identity.domain.entity.Organization;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织架构服务接口
 *
 * @author admin
 * @since 2024-01-15
 */
public interface OrganizationService extends IService<Organization> {

    /**
     * 创建组织
     *
     * @param tenantId 租户ID
     * @param createDTO 组织创建DTO
     * @return 组织ID
     */
    Long createOrganization(Long tenantId, OrganizationCreateDTO createDTO);

    /**
     * 更新组织
     *
     * @param tenantId 租户ID
     * @param updateDTO 组织更新DTO
     * @return 是否成功
     */
    Boolean updateOrganization(Long tenantId, OrganizationUpdateDTO updateDTO);

    /**
     * 删除组织
     *
     * @param tenantId 租户ID
     * @param id 组织ID
     * @return 是否成功
     */
    Boolean deleteOrganization(Long tenantId, Long id);

    /**
     * 批量删除组织
     *
     * @param tenantId 租户ID
     * @param ids 组织ID列表
     * @return 是否成功
     */
    Boolean batchDeleteOrganizations(Long tenantId, List<Long> ids);

    /**
     * 分页查询组织列表
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<OrganizationResponseDTO> getOrganizationPage(Long tenantId, OrganizationQueryDTO queryDTO);

    /**
     * 查询组织树
     *
     * @param tenantId 租户ID
     * @param queryDTO 查询条件
     * @return 组织树
     */
    List<OrganizationResponseDTO> getOrganizationTree(Long tenantId, OrganizationQueryDTO queryDTO);

    /**
     * 根据ID查询组织详情
     *
     * @param tenantId 租户ID
     * @param id 组织ID
     * @return 组织详情
     */
    OrganizationResponseDTO getOrganizationDetail(Long tenantId, Long id);

    /**
     * 查询子组织列表
     *
     * @param tenantId 租户ID
     * @param parentId 父组织ID
     * @return 子组织列表
     */
    List<OrganizationResponseDTO> getChildOrganizations(Long tenantId, Long parentId);

    /**
     * 移动组织
     *
     * @param tenantId 租户ID
     * @param orgId 组织ID
     * @param newParentId 新父组织ID
     * @param newSortOrder 新排序号
     * @return 是否成功
     */
    Boolean moveOrganization(Long tenantId, Long orgId, Long newParentId, Integer newSortOrder);

    /**
     * 调整组织排序
     *
     * @param tenantId 租户ID
     * @param orgId 组织ID
     * @param sortOrder 新排序号
     * @return 是否成功
     */
    Boolean updateOrganizationSort(Long tenantId, Long orgId, Integer sortOrder);

    /**
     * 设置组织负责人
     *
     * @param tenantId 租户ID
     * @param orgId 组织ID
     * @param leaderId 负责人ID
     * @param deputyLeaderId 副负责人ID
     * @return 是否成功
     */
    Boolean setOrganizationLeader(Long tenantId, Long orgId, Long leaderId, Long deputyLeaderId);

    /**
     * 检查组织编码是否存在
     *
     * @param tenantId 租户ID
     * @param orgCode 组织编码
     * @return 是否存在
     */
    Boolean existsByOrgCode(Long tenantId, String orgCode);

    /**
     * 检查组织编码是否存在（排除指定ID）
     *
     * @param tenantId 租户ID
     * @param orgCode 组织编码
     * @param excludeId 排除的组织ID
     * @return 是否存在
     */
    Boolean existsByOrgCodeExcludeId(Long tenantId, String orgCode, Long excludeId);

    /**
     * 检查是否存在子组织
     *
     * @param tenantId 租户ID
     * @param parentId 父组织ID
     * @return 是否存在
     */
    Boolean hasChildOrganizations(Long tenantId, Long parentId);

    /**
     * 检查组织是否有成员
     *
     * @param tenantId 租户ID
     * @param orgId 组织ID
     * @param includeSubOrgs 是否包含子组织
     * @return 是否有成员
     */
    Boolean hasMembers(Long tenantId, Long orgId, Boolean includeSubOrgs);

    /**
     * 查询组织的所有下级组织ID列表
     *
     * @param tenantId 租户ID
     * @param parentId 父组织ID
     * @return 下级组织ID列表
     */
    List<Long> getAllSubOrganizationIds(Long tenantId, Long parentId);

    /**
     * 根据负责人查询组织列表
     *
     * @param tenantId 租户ID
     * @param leaderId 负责人ID
     * @return 组织列表
     */
    List<OrganizationResponseDTO> getOrganizationsByLeader(Long tenantId, Long leaderId);

    /**
     * 查询用户所属的组织列表
     *
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @return 组织列表
     */
    List<OrganizationResponseDTO> getUserOrganizations(Long tenantId, Long userId);

    /**
     * 启用组织
     *
     * @param tenantId 租户ID
     * @param id 组织ID
     * @return 是否成功
     */
    Boolean enableOrganization(Long tenantId, Long id);

    /**
     * 禁用组织
     *
     * @param tenantId 租户ID
     * @param id 组织ID
     * @return 是否成功
     */
    Boolean disableOrganization(Long tenantId, Long id);

    /**
     * 批量更新组织状态
     *
     * @param tenantId 租户ID
     * @param ids 组织ID列表
     * @param status 状态
     * @return 是否成功
     */
    Boolean batchUpdateStatus(Long tenantId, List<Long> ids, String status);

    /**
     * 初始化租户根组织
     *
     * @param tenantId 租户ID
     * @param tenantName 租户名称
     * @return 根组织ID
     */
    Long initRootOrganization(Long tenantId, String tenantName);

    /**
     * 导入组织架构
     *
     * @param tenantId 租户ID
     * @param organizations 组织列表
     * @return 导入结果
     */
    OrganizationImportResultDTO importOrganizations(Long tenantId, List<OrganizationCreateDTO> organizations);

    /**
     * 导出组织架构
     *
     * @param tenantId 租户ID
     * @param format 导出格式（excel/json）
     * @return 导出文件路径
     */
    String exportOrganizations(Long tenantId, String format);

    /**
     * 获取组织统计信息
     *
     * @param tenantId 租户ID
     * @return 统计信息
     */
    OrganizationStatisticsDTO getOrganizationStatistics(Long tenantId);
}
