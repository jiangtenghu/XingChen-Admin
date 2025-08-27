package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限模板DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionTemplateDTO", description = "权限模板")
public class PermissionTemplateDTO {

    @Schema(description = "模板ID", example = "1")
    private Long templateId;

    @Schema(description = "模板名称", example = "基础员工权限模板")
    private String templateName;

    @Schema(description = "模板代码", example = "BASIC_EMPLOYEE")
    private String templateCode;

    @Schema(description = "模板描述", example = "适用于普通员工的基础权限模板")
    private String templateDescription;

    @Schema(description = "模板类型", example = "ROLE_TEMPLATE", allowableValues = {"ROLE_TEMPLATE", "PERMISSION_TEMPLATE", "DEPARTMENT_TEMPLATE"})
    private String templateType;

    @Schema(description = "适用范围", example = "ALL_DEPARTMENTS", allowableValues = {"ALL_DEPARTMENTS", "SPECIFIC_DEPARTMENTS", "ALL_POSITIONS", "SPECIFIC_POSITIONS"})
    private String applicableScope;

    @Schema(description = "模板状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "DRAFT"})
    private String templateStatus;

    @Schema(description = "是否系统模板", example = "false")
    private Boolean isSystemTemplate;

    @Schema(description = "优先级", example = "10")
    private Integer priority;

    @Schema(description = "版本号", example = "1.0")
    private String version;

    @Schema(description = "权限列表")
    private List<TemplatePermission> permissions;

    @Schema(description = "角色列表")
    private List<TemplateRole> roles;

    @Schema(description = "应用条件")
    private List<ApplicationCondition> applicationConditions;

    @Schema(description = "使用统计")
    private TemplateUsageStats usageStats;

    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Schema(description = "创建时间", example = "2024-08-26T10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Schema(description = "更新时间", example = "2024-08-26T11:00:00")
    private LocalDateTime updateTime;

    /**
     * 模板权限
     */
    @Data
    @Schema(description = "模板权限")
    public static class TemplatePermission {
        @Schema(description = "权限ID", example = "1")
        private Long permissionId;

        @Schema(description = "权限名称", example = "用户查看")
        private String permissionName;

        @Schema(description = "权限标识", example = "user:view")
        private String permissionKey;

        @Schema(description = "权限类型", example = "MENU")
        private String permissionType;

        @Schema(description = "是否必需", example = "true")
        private Boolean isRequired;

        @Schema(description = "是否默认选中", example = "true")
        private Boolean isDefaultSelected;

        @Schema(description = "权限分组", example = "用户管理")
        private String permissionGroup;

        @Schema(description = "排序号", example = "1")
        private Integer sortOrder;
    }

    /**
     * 模板角色
     */
    @Data
    @Schema(description = "模板角色")
    public static class TemplateRole {
        @Schema(description = "角色ID", example = "1")
        private Long roleId;

        @Schema(description = "角色名称", example = "普通用户")
        private String roleName;

        @Schema(description = "角色标识", example = "ROLE_USER")
        private String roleKey;

        @Schema(description = "是否必需", example = "true")
        private Boolean isRequired;

        @Schema(description = "是否默认分配", example = "true")
        private Boolean isDefaultAssigned;

        @Schema(description = "角色描述", example = "系统普通用户角色")
        private String roleDescription;
    }

    /**
     * 应用条件
     */
    @Data
    @Schema(description = "应用条件")
    public static class ApplicationCondition {
        @Schema(description = "条件ID", example = "1")
        private Long conditionId;

        @Schema(description = "条件类型", example = "DEPARTMENT", allowableValues = {"DEPARTMENT", "POSITION", "USER_TYPE", "HIRE_DATE", "CUSTOM"})
        private String conditionType;

        @Schema(description = "条件操作", example = "EQUALS", allowableValues = {"EQUALS", "NOT_EQUALS", "IN", "NOT_IN", "CONTAINS", "GREATER_THAN", "LESS_THAN"})
        private String conditionOperator;

        @Schema(description = "条件值", example = "技术部")
        private String conditionValue;

        @Schema(description = "条件值列表", example = "[\"技术部\", \"产品部\"]")
        private List<String> conditionValues;

        @Schema(description = "条件描述", example = "适用于技术部员工")
        private String conditionDescription;

        @Schema(description = "是否启用", example = "true")
        private Boolean isEnabled;
    }

    /**
     * 模板使用统计
     */
    @Data
    @Schema(description = "模板使用统计")
    public static class TemplateUsageStats {
        @Schema(description = "总应用次数", example = "150")
        private Integer totalApplications;

        @Schema(description = "成功应用次数", example = "145")
        private Integer successfulApplications;

        @Schema(description = "失败应用次数", example = "5")
        private Integer failedApplications;

        @Schema(description = "当前使用该模板的用户数", example = "120")
        private Integer currentUsers;

        @Schema(description = "最近应用时间", example = "2024-08-26T09:00:00")
        private LocalDateTime lastApplicationTime;

        @Schema(description = "最近应用用户", example = "admin")
        private String lastApplicationBy;

        @Schema(description = "平均应用时间（秒）", example = "5.2")
        private Double avgApplicationTime;

        @Schema(description = "应用成功率", example = "96.7")
        private Double successRate;
    }
}

