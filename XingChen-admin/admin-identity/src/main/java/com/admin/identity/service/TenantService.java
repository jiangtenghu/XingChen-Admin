package com.admin.identity.service;

import com.admin.identity.domain.dto.*;
import com.admin.identity.domain.entity.Tenant;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 租户服务接口
 *
 * @author admin
 * @since 2024-01-15
 */
public interface TenantService extends IService<Tenant> {

    /**
     * 创建租户
     *
     * @param createDTO 租户创建DTO
     * @return 租户ID
     */
    Long createTenant(TenantCreateDTO createDTO);

    /**
     * 更新租户
     *
     * @param updateDTO 租户更新DTO
     * @return 是否成功
     */
    Boolean updateTenant(TenantUpdateDTO updateDTO);

    /**
     * 删除租户
     *
     * @param id 租户ID
     * @return 是否成功
     */
    Boolean deleteTenant(Long id);

    /**
     * 批量删除租户
     *
     * @param ids 租户ID列表
     * @return 是否成功
     */
    Boolean batchDeleteTenants(List<Long> ids);

    /**
     * 分页查询租户列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<TenantResponseDTO> getTenantPage(TenantQueryDTO queryDTO);

    /**
     * 根据ID查询租户详情
     *
     * @param id 租户ID
     * @return 租户详情
     */
    TenantResponseDTO getTenantDetail(Long id);

    /**
     * 查询租户的子租户列表
     *
     * @param parentId 父租户ID
     * @return 子租户列表
     */
    List<TenantResponseDTO> getSubTenants(Long parentId);

    /**
     * 激活租户
     *
     * @param id 租户ID
     * @return 是否成功
     */
    Boolean activateTenant(Long id);

    /**
     * 暂停租户
     *
     * @param id 租户ID
     * @param reason 暂停原因
     * @return 是否成功
     */
    Boolean suspendTenant(Long id, String reason);

    /**
     * 恢复租户
     *
     * @param id 租户ID
     * @return 是否成功
     */
    Boolean resumeTenant(Long id);

    /**
     * 续费租户
     *
     * @param id 租户ID
     * @param months 续费月数
     * @return 是否成功
     */
    Boolean renewTenant(Long id, Integer months);

    /**
     * 检查租户编码是否存在
     *
     * @param code 租户编码
     * @return 是否存在
     */
    Boolean existsByCode(String code);

    /**
     * 检查租户编码是否存在（排除指定ID）
     *
     * @param code 租户编码
     * @param excludeId 排除的租户ID
     * @return 是否存在
     */
    Boolean existsByCodeExcludeId(String code, Long excludeId);

    /**
     * 检查域名是否存在
     *
     * @param domain 域名
     * @return 是否存在
     */
    Boolean existsByDomain(String domain);

    /**
     * 检查域名是否存在（排除指定ID）
     *
     * @param domain 域名
     * @param excludeId 排除的租户ID
     * @return 是否存在
     */
    Boolean existsByDomainExcludeId(String domain, Long excludeId);

    /**
     * 获取租户的配额使用情况
     *
     * @param tenantId 租户ID
     * @return 配额使用情况
     */
    TenantQuotaDTO getTenantQuota(Long tenantId);

    /**
     * 检查租户配额是否超限
     *
     * @param tenantId 租户ID
     * @param quotaType 配额类型（users/departments/roles/storage）
     * @param increment 增量
     * @return 是否超限
     */
    Boolean checkQuotaLimit(Long tenantId, String quotaType, Integer increment);

    /**
     * 处理即将过期的租户
     *
     * @param days 提前天数
     * @return 处理数量
     */
    Integer handleExpiringTenants(Integer days);

    /**
     * 处理已过期的租户
     *
     * @return 处理数量
     */
    Integer handleExpiredTenants();

    /**
     * 批量更新租户状态
     *
     * @param ids 租户ID列表
     * @param status 状态
     * @return 是否成功
     */
    Boolean batchUpdateStatus(List<Long> ids, String status);

    /**
     * 获取租户统计信息
     *
     * @return 统计信息
     */
    TenantStatisticsDTO getTenantStatistics();
}
