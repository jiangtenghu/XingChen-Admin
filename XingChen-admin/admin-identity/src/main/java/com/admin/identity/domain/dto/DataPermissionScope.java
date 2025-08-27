package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 数据权限范围DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "DataPermissionScope", description = "数据权限范围")
public class DataPermissionScope {

    @Schema(description = "数据权限类型", example = "DEPARTMENT", allowableValues = {
        "ALL", "DEPARTMENT", "DEPARTMENT_AND_SUB", "ORGANIZATION", "SELF_ONLY", "CUSTOM"
    })
    private String scopeType;

    @Schema(description = "数据权限类型名称", example = "部门数据权限")
    private String scopeTypeName;

    @Schema(description = "权限范围ID列表", example = "[1, 2, 3]")
    private List<Long> scopeIds;

    @Schema(description = "权限范围名称列表", example = "[\"技术部\", \"产品部\", \"运营部\"]")
    private List<String> scopeNames;

    @Schema(description = "是否包含子级", example = "true")
    private Boolean includeSubLevels;

    @Schema(description = "权限等级", example = "2")
    private Integer permissionLevel;

    @Schema(description = "自定义SQL条件", example = "dept_id IN (1,2,3)")
    private String customSqlCondition;

    @Schema(description = "权限描述", example = "可以查看技术部、产品部和运营部的数据")
    private String description;

    @Schema(description = "数据字段限制")
    private List<FieldRestriction> fieldRestrictions;

    @Schema(description = "时间范围限制")
    private TimeRangeRestriction timeRestriction;

    @Schema(description = "行级权限规则")
    private List<RowLevelRule> rowLevelRules;

    /**
     * 字段权限限制
     */
    @Data
    @Schema(description = "字段权限限制")
    public static class FieldRestriction {
        @Schema(description = "表名", example = "sys_user")
        private String tableName;

        @Schema(description = "字段名", example = "phone")
        private String fieldName;

        @Schema(description = "权限类型", example = "READ", allowableValues = {"READ", "WRITE", "MASK", "DENY"})
        private String permissionType;

        @Schema(description = "掩码规则", example = "1****5678")
        private String maskRule;

        @Schema(description = "条件表达式", example = "user_level >= 3")
        private String conditionExpression;
    }

    /**
     * 时间范围限制
     */
    @Data
    @Schema(description = "时间范围限制")
    public static class TimeRangeRestriction {
        @Schema(description = "是否启用", example = "true")
        private Boolean enabled;

        @Schema(description = "时间字段", example = "create_time")
        private String timeField;

        @Schema(description = "时间范围类型", example = "RECENT_DAYS", allowableValues = {
            "RECENT_DAYS", "RECENT_MONTHS", "CURRENT_YEAR", "CUSTOM_RANGE"
        })
        private String rangeType;

        @Schema(description = "时间值", example = "30")
        private Integer timeValue;

        @Schema(description = "开始时间", example = "2024-01-01")
        private String startTime;

        @Schema(description = "结束时间", example = "2024-12-31")
        private String endTime;
    }

    /**
     * 行级权限规则
     */
    @Data
    @Schema(description = "行级权限规则")
    public static class RowLevelRule {
        @Schema(description = "规则名称", example = "部门数据权限")
        private String ruleName;

        @Schema(description = "表名", example = "sys_user")
        private String tableName;

        @Schema(description = "权限字段", example = "dept_id")
        private String permissionField;

        @Schema(description = "操作类型", example = "SELECT", allowableValues = {"SELECT", "INSERT", "UPDATE", "DELETE"})
        private String operationType;

        @Schema(description = "条件表达式", example = "dept_id IN ({userDeptIds})")
        private String conditionExpression;

        @Schema(description = "是否启用", example = "true")
        private Boolean enabled;

        @Schema(description = "优先级", example = "10")
        private Integer priority;
    }
}

