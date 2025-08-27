package com.admin.identity.domain.dto;

import lombok.Data;

/**
 * 租户配额DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class TenantQuotaDTO {

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 最大用户数
     */
    private Integer maxUsers;

    /**
     * 当前用户数
     */
    private Integer currentUsers;

    /**
     * 用户使用率
     */
    private Double userUsageRate;

    /**
     * 最大存储空间(字节)
     */
    private Long maxStorage;

    /**
     * 当前存储使用量(字节)
     */
    private Long currentStorage;

    /**
     * 存储使用率
     */
    private Double storageUsageRate;

    /**
     * 最大部门数
     */
    private Integer maxDepartments;

    /**
     * 当前部门数
     */
    private Integer currentDepartments;

    /**
     * 部门使用率
     */
    private Double departmentUsageRate;

    /**
     * 最大角色数
     */
    private Integer maxRoles;

    /**
     * 当前角色数
     */
    private Integer currentRoles;

    /**
     * 角色使用率
     */
    private Double roleUsageRate;

    /**
     * 是否用户配额告警
     */
    private Boolean userQuotaWarning;

    /**
     * 是否存储配额告警
     */
    private Boolean storageQuotaWarning;

    /**
     * 是否部门配额告警
     */
    private Boolean departmentQuotaWarning;

    /**
     * 是否角色配额告警
     */
    private Boolean roleQuotaWarning;

    /**
     * 计算使用率
     */
    public void calculateUsageRates() {
        if (maxUsers != null && maxUsers > 0) {
            userUsageRate = (double) currentUsers / maxUsers * 100;
            userQuotaWarning = userUsageRate >= 80; // 80%告警
        }
        
        if (maxStorage != null && maxStorage > 0) {
            storageUsageRate = (double) currentStorage / maxStorage * 100;
            storageQuotaWarning = storageUsageRate >= 80;
        }
        
        if (maxDepartments != null && maxDepartments > 0) {
            departmentUsageRate = (double) currentDepartments / maxDepartments * 100;
            departmentQuotaWarning = departmentUsageRate >= 80;
        }
        
        if (maxRoles != null && maxRoles > 0) {
            roleUsageRate = (double) currentRoles / maxRoles * 100;
            roleQuotaWarning = roleUsageRate >= 80;
        }
    }
}
