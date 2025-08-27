package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户导入结果DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "UserImportResultDTO", description = "用户导入结果对象")
public class UserImportResultDTO {

    @Schema(description = "导入批次ID", example = "batch_20240826_001")
    private String batchId;

    @Schema(description = "总记录数", example = "100")
    private Integer totalCount;

    @Schema(description = "成功导入数", example = "85")
    private Integer successCount;

    @Schema(description = "失败数", example = "15")
    private Integer failureCount;

    @Schema(description = "跳过数", example = "5")
    private Integer skipCount;

    @Schema(description = "导入开始时间", example = "2024-08-26T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "导入结束时间", example = "2024-08-26T10:05:00")
    private LocalDateTime endTime;

    @Schema(description = "导入耗时（秒）", example = "300")
    private Long duration;

    @Schema(description = "导入状态", example = "COMPLETED", allowableValues = {"PROCESSING", "COMPLETED", "FAILED", "CANCELLED"})
    private String status;

    @Schema(description = "导入模式", example = "INSERT_ONLY", allowableValues = {"INSERT_ONLY", "UPDATE_ONLY", "INSERT_OR_UPDATE"})
    private String importMode;

    @Schema(description = "成功导入的用户列表")
    private List<ImportedUser> successUsers;

    @Schema(description = "失败的记录列表")
    private List<FailedRecord> failedRecords;

    @Schema(description = "跳过的记录列表")
    private List<SkippedRecord> skippedRecords;

    @Schema(description = "导入统计信息")
    private ImportStatistics statistics;

    @Schema(description = "导入日志文件路径", example = "/logs/import_20240826_001.log")
    private String logFilePath;

    @Schema(description = "错误报告文件路径", example = "/reports/error_20240826_001.xlsx")
    private String errorReportPath;

    @Schema(description = "操作人", example = "admin")
    private String operateBy;

    @Schema(description = "备注", example = "批量导入新员工信息")
    private String remark;

    /**
     * 成功导入的用户
     */
    @Data
    @Schema(description = "成功导入的用户")
    public static class ImportedUser {
        @Schema(description = "行号", example = "2")
        private Integer rowNumber;

        @Schema(description = "用户ID", example = "1001")
        private Long userId;

        @Schema(description = "用户名", example = "zhangsan")
        private String username;

        @Schema(description = "真实姓名", example = "张三")
        private String realName;

        @Schema(description = "邮箱", example = "zhangsan@example.com")
        private String email;

        @Schema(description = "操作类型", example = "INSERT", allowableValues = {"INSERT", "UPDATE"})
        private String operation;

        @Schema(description = "导入时间", example = "2024-08-26T10:01:00")
        private LocalDateTime importTime;
    }

    /**
     * 失败的记录
     */
    @Data
    @Schema(description = "失败的记录")
    public static class FailedRecord {
        @Schema(description = "行号", example = "5")
        private Integer rowNumber;

        @Schema(description = "原始数据")
        private String originalData;

        @Schema(description = "错误类型", example = "VALIDATION_ERROR")
        private String errorType;

        @Schema(description = "错误消息", example = "邮箱格式不正确")
        private String errorMessage;

        @Schema(description = "错误字段", example = "email")
        private String errorField;

        @Schema(description = "错误值", example = "invalid-email")
        private String errorValue;

        @Schema(description = "建议解决方案", example = "请检查邮箱格式是否正确")
        private String suggestion;
    }

    /**
     * 跳过的记录
     */
    @Data
    @Schema(description = "跳过的记录")
    public static class SkippedRecord {
        @Schema(description = "行号", example = "3")
        private Integer rowNumber;

        @Schema(description = "跳过原因", example = "DUPLICATE_USERNAME")
        private String skipReason;

        @Schema(description = "跳过说明", example = "用户名已存在")
        private String skipMessage;

        @Schema(description = "现有用户ID", example = "100")
        private Long existingUserId;

        @Schema(description = "用户名", example = "existuser")
        private String username;
    }

    /**
     * 导入统计信息
     */
    @Data
    @Schema(description = "导入统计信息")
    public static class ImportStatistics {
        @Schema(description = "按部门统计")
        private List<DepartmentStatistic> departmentStats;

        @Schema(description = "按角色统计")
        private List<RoleStatistic> roleStats;

        @Schema(description = "按用户类型统计")
        private List<UserTypeStatistic> userTypeStats;

        @Schema(description = "错误类型统计")
        private List<ErrorTypeStatistic> errorTypeStats;
    }

    @Data
    @Schema(description = "部门统计")
    public static class DepartmentStatistic {
        @Schema(description = "部门名称", example = "技术部")
        private String departmentName;

        @Schema(description = "导入数量", example = "25")
        private Integer importCount;

        @Schema(description = "成功数量", example = "23")
        private Integer successCount;

        @Schema(description = "失败数量", example = "2")
        private Integer failureCount;
    }

    @Data
    @Schema(description = "角色统计")
    public static class RoleStatistic {
        @Schema(description = "角色名称", example = "普通员工")
        private String roleName;

        @Schema(description = "分配数量", example = "80")
        private Integer assignCount;
    }

    @Data
    @Schema(description = "用户类型统计")
    public static class UserTypeStatistic {
        @Schema(description = "用户类型", example = "NORMAL")
        private String userType;

        @Schema(description = "数量", example = "90")
        private Integer count;
    }

    @Data
    @Schema(description = "错误类型统计")
    public static class ErrorTypeStatistic {
        @Schema(description = "错误类型", example = "VALIDATION_ERROR")
        private String errorType;

        @Schema(description = "错误数量", example = "10")
        private Integer errorCount;

        @Schema(description = "错误描述", example = "数据验证失败")
        private String errorDescription;
    }
}

