package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 权限统计DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionStatisticsDTO", description = "权限统计信息")
public class PermissionStatisticsDTO {

    @Schema(description = "总权限数", example = "500")
    private Integer totalPermissions;

    @Schema(description = "菜单权限数", example = "150")
    private Integer menuPermissions;

    @Schema(description = "按钮权限数", example = "200")
    private Integer buttonPermissions;

    @Schema(description = "接口权限数", example = "120")
    private Integer apiPermissions;

    @Schema(description = "数据权限数", example = "30")
    private Integer dataPermissions;

    @Schema(description = "系统权限数", example = "100")
    private Integer systemPermissions;

    @Schema(description = "自定义权限数", example = "400")
    private Integer customPermissions;

    @Schema(description = "启用权限数", example = "450")
    private Integer activePermissions;

    @Schema(description = "禁用权限数", example = "50")
    private Integer inactivePermissions;

    @Schema(description = "按权限类型统计")
    private List<PermissionTypeCount> permissionTypeCounts;

    @Schema(description = "按模块统计")
    private List<ModulePermissionCount> modulePermissionCounts;

    @Schema(description = "权限层级统计")
    private List<PermissionLevelCount> permissionLevelCounts;

    @Schema(description = "权限使用情况统计")
    private List<PermissionUsageCount> permissionUsageCounts;

    @Schema(description = "最常用权限TOP10")
    private List<TopUsedPermission> topUsedPermissions;

    @Schema(description = "未使用权限列表")
    private List<UnusedPermission> unusedPermissions;

    @Schema(description = "权限分配统计")
    private PermissionAssignmentStats assignmentStats;

    /**
     * 权限类型统计
     */
    @Data
    @Schema(description = "权限类型统计")
    public static class PermissionTypeCount {
        @Schema(description = "权限类型", example = "MENU")
        private String permissionType;

        @Schema(description = "权限类型名称", example = "菜单权限")
        private String permissionTypeName;

        @Schema(description = "权限数量", example = "150")
        private Integer count;

        @Schema(description = "占比", example = "30.0")
        private Double percentage;
    }

    /**
     * 模块权限统计
     */
    @Data
    @Schema(description = "模块权限统计")
    public static class ModulePermissionCount {
        @Schema(description = "模块名称", example = "用户管理")
        private String moduleName;

        @Schema(description = "权限数量", example = "25")
        private Integer count;

        @Schema(description = "菜单数量", example = "5")
        private Integer menuCount;

        @Schema(description = "按钮数量", example = "15")
        private Integer buttonCount;

        @Schema(description = "接口数量", example = "5")
        private Integer apiCount;
    }

    /**
     * 权限层级统计
     */
    @Data
    @Schema(description = "权限层级统计")
    public static class PermissionLevelCount {
        @Schema(description = "层级", example = "1")
        private Integer level;

        @Schema(description = "层级名称", example = "一级菜单")
        private String levelName;

        @Schema(description = "权限数量", example = "10")
        private Integer count;

        @Schema(description = "最大深度", example = "5")
        private Integer maxDepth;
    }

    /**
     * 权限使用情况统计
     */
    @Data
    @Schema(description = "权限使用情况统计")
    public static class PermissionUsageCount {
        @Schema(description = "使用情况", example = "FREQUENTLY_USED")
        private String usageLevel;

        @Schema(description = "使用情况名称", example = "经常使用")
        private String usageLevelName;

        @Schema(description = "权限数量", example = "80")
        private Integer count;

        @Schema(description = "使用次数阈值", example = "100")
        private Integer usageThreshold;
    }

    /**
     * 最常用权限
     */
    @Data
    @Schema(description = "最常用权限")
    public static class TopUsedPermission {
        @Schema(description = "权限ID", example = "1")
        private Long permissionId;

        @Schema(description = "权限名称", example = "用户列表")
        private String permissionName;

        @Schema(description = "权限标识", example = "user:list")
        private String permissionKey;

        @Schema(description = "权限类型", example = "MENU")
        private String permissionType;

        @Schema(description = "使用次数", example = "1500")
        private Integer usageCount;

        @Schema(description = "分配给角色数", example = "10")
        private Integer roleCount;

        @Schema(description = "分配给用户数", example = "200")
        private Integer userCount;
    }

    /**
     * 未使用权限
     */
    @Data
    @Schema(description = "未使用权限")
    public static class UnusedPermission {
        @Schema(description = "权限ID", example = "500")
        private Long permissionId;

        @Schema(description = "权限名称", example = "废弃功能")
        private String permissionName;

        @Schema(description = "权限标识", example = "deprecated:function")
        private String permissionKey;

        @Schema(description = "权限类型", example = "BUTTON")
        private String permissionType;

        @Schema(description = "创建时间", example = "2023-01-01")
        private String createTime;

        @Schema(description = "最后使用时间", example = "从未使用")
        private String lastUsedTime;

        @Schema(description = "建议操作", example = "DELETE")
        private String suggestedAction;
    }

    /**
     * 权限分配统计
     */
    @Data
    @Schema(description = "权限分配统计")
    public static class PermissionAssignmentStats {
        @Schema(description = "已分配权限数", example = "400")
        private Integer assignedPermissions;

        @Schema(description = "未分配权限数", example = "100")
        private Integer unassignedPermissions;

        @Schema(description = "平均每个角色权限数", example = "25")
        private Double avgPermissionsPerRole;

        @Schema(description = "平均每个用户权限数", example = "50")
        private Double avgPermissionsPerUser;

        @Schema(description = "权限分配密度", example = "80.0")
        private Double assignmentDensity;

        @Schema(description = "权限冗余度", example = "5.0")
        private Double redundancyRate;

        @Schema(description = "按角色分配统计")
        private List<RolePermissionAssignment> roleAssignments;

        @Schema(description = "按用户分配统计")
        private List<UserPermissionAssignment> userAssignments;
    }

    /**
     * 角色权限分配
     */
    @Data
    @Schema(description = "角色权限分配")
    public static class RolePermissionAssignment {
        @Schema(description = "角色ID", example = "1")
        private Long roleId;

        @Schema(description = "角色名称", example = "系统管理员")
        private String roleName;

        @Schema(description = "分配权限数", example = "150")
        private Integer assignedPermissionCount;

        @Schema(description = "用户数量", example = "5")
        private Integer userCount;

        @Schema(description = "权限覆盖率", example = "30.0")
        private Double permissionCoverageRate;
    }

    /**
     * 用户权限分配
     */
    @Data
    @Schema(description = "用户权限分配")
    public static class UserPermissionAssignment {
        @Schema(description = "用户ID", example = "1")
        private Long userId;

        @Schema(description = "用户名", example = "admin")
        private String username;

        @Schema(description = "真实姓名", example = "管理员")
        private String realName;

        @Schema(description = "直接分配权限数", example = "10")
        private Integer directPermissionCount;

        @Schema(description = "角色继承权限数", example = "40")
        private Integer inheritedPermissionCount;

        @Schema(description = "总权限数", example = "50")
        private Integer totalPermissionCount;

        @Schema(description = "权限获取方式分布")
        private PermissionSourceDistribution sourceDistribution;
    }

    /**
     * 权限来源分布
     */
    @Data
    @Schema(description = "权限来源分布")
    public static class PermissionSourceDistribution {
        @Schema(description = "直接分配权限占比", example = "20.0")
        private Double directAssignmentRate;

        @Schema(description = "角色继承权限占比", example = "80.0")
        private Double roleInheritanceRate;

        @Schema(description = "权限重叠数", example = "5")
        private Integer overlappingPermissions;
    }
}

