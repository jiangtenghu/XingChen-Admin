package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色详情DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "RoleDetailDTO", description = "角色详情对象")
public class RoleDetailDTO {

    @Schema(description = "角色ID", example = "1")
    private Long id;

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "租户信息")
    private TenantResponseDTO tenant;

    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    @Schema(description = "角色标识", example = "ROLE_ADMIN")
    private String roleKey;

    @Schema(description = "数据权限范围", example = "1")
    private String dataScope;

    @Schema(description = "数据权限关联的组织ID列表")
    private String dataScopeOrgIds;

    @Schema(description = "数据权限关联的组织列表")
    private List<OrganizationResponseDTO> dataScopeOrganizations;

    @Schema(description = "角色类型", example = "BUSINESS")
    private String roleType;

    @Schema(description = "角色状态", example = "ACTIVE")
    private String status;

    @Schema(description = "生效时间", example = "2024-01-01T00:00:00")
    private LocalDateTime effectiveTime;

    @Schema(description = "过期时间", example = "2025-12-31T23:59:59")
    private LocalDateTime expireTime;

    @Schema(description = "角色描述", example = "系统超级管理员，拥有所有权限")
    private String remark;

    @Schema(description = "角色排序", example = "1")
    private Integer orderNum;

    @Schema(description = "权限树结构")
    private List<PermissionResponseDTO> permissionTree;

    @Schema(description = "已分配的权限ID列表", example = "[1, 2, 3, 4, 5]")
    private List<Long> assignedPermissionIds;

    @Schema(description = "拥有该角色的用户列表")
    private List<UserResponseDTO> users;

    @Schema(description = "用户数量统计", example = "5")
    private Integer userCount;

    @Schema(description = "是否为系统内置角色", example = "false")
    private Boolean isSystem;

    @Schema(description = "是否已过期", example = "false")
    private Boolean isExpired;

    @Schema(description = "权限统计信息")
    private PermissionStatistics permissionStatistics;

    @Schema(description = "操作日志")
    private List<RoleOperationLog> operationLogs;

    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Schema(description = "更新时间", example = "2024-01-02T00:00:00")
    private LocalDateTime updateTime;

    /**
     * 权限统计信息
     */
    @Data
    @Schema(description = "权限统计信息")
    public static class PermissionStatistics {
        @Schema(description = "菜单权限数量", example = "10")
        private Integer menuCount;

        @Schema(description = "按钮权限数量", example = "25")
        private Integer buttonCount;

        @Schema(description = "接口权限数量", example = "50")
        private Integer apiCount;

        @Schema(description = "数据权限数量", example = "5")
        private Integer dataCount;

        @Schema(description = "总权限数量", example = "90")
        private Integer totalCount;
    }

    /**
     * 角色操作日志
     */
    @Data
    @Schema(description = "角色操作日志")
    public static class RoleOperationLog {
        @Schema(description = "操作类型", example = "CREATE")
        private String operationType;

        @Schema(description = "操作描述", example = "创建角色")
        private String operationDesc;

        @Schema(description = "操作人", example = "admin")
        private String operateBy;

        @Schema(description = "操作时间", example = "2024-01-01T00:00:00")
        private LocalDateTime operateTime;

        @Schema(description = "操作IP", example = "192.168.1.100")
        private String operateIp;
    }
}

