package com.admin.identity.domain.dto;

import lombok.Data;

import java.util.Map;

/**
 * 租户统计DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class TenantStatisticsDTO {

    /**
     * 租户总数
     */
    private Long totalTenants;

    /**
     * 活跃租户数
     */
    private Long activeTenants;

    /**
     * 暂停租户数
     */
    private Long suspendedTenants;

    /**
     * 过期租户数
     */
    private Long expiredTenants;

    /**
     * 今日新增租户数
     */
    private Long todayNewTenants;

    /**
     * 本月新增租户数
     */
    private Long monthNewTenants;

    /**
     * 按类型统计
     */
    private Map<String, Long> tenantsByType;

    /**
     * 按套餐统计
     */
    private Map<String, Long> tenantsByPackage;

    /**
     * 按地区统计
     */
    private Map<String, Long> tenantsByRegion;

    /**
     * 按行业统计
     */
    private Map<String, Long> tenantsByIndustry;

    /**
     * 即将过期租户数（7天内）
     */
    private Long expiringTenants;

    /**
     * 配额告警租户数
     */
    private Long quotaWarningTenants;

    /**
     * 平均用户数
     */
    private Double averageUsers;

    /**
     * 平均部门数
     */
    private Double averageDepartments;

    /**
     * 总用户数
     */
    private Long totalUsers;

    /**
     * 总部门数
     */
    private Long totalDepartments;

    /**
     * 总角色数
     */
    private Long totalRoles;

    /**
     * 总存储使用量(字节)
     */
    private Long totalStorageUsed;
}
