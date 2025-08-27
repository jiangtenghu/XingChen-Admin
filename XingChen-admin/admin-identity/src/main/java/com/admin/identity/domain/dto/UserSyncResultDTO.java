package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户同步结果DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "UserSyncResultDTO", description = "用户同步结果对象")
public class UserSyncResultDTO {

    @Schema(description = "同步批次ID", example = "sync_20240826_001")
    private String syncBatchId;

    @Schema(description = "同步源", example = "LDAP", allowableValues = {"LDAP", "AD", "API", "DATABASE"})
    private String syncSource;

    @Schema(description = "同步类型", example = "FULL", allowableValues = {"FULL", "INCREMENTAL"})
    private String syncType;

    @Schema(description = "同步状态", example = "COMPLETED", allowableValues = {"PROCESSING", "COMPLETED", "FAILED", "CANCELLED"})
    private String syncStatus;

    @Schema(description = "同步开始时间", example = "2024-08-26T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "同步结束时间", example = "2024-08-26T10:15:00")
    private LocalDateTime endTime;

    @Schema(description = "同步耗时（秒）", example = "900")
    private Long duration;

    @Schema(description = "源系统用户总数", example = "1200")
    private Integer sourceUserCount;

    @Schema(description = "本系统用户总数", example = "1000")
    private Integer localUserCount;

    @Schema(description = "新增用户数", example = "50")
    private Integer addedUserCount;

    @Schema(description = "更新用户数", example = "100")
    private Integer updatedUserCount;

    @Schema(description = "删除用户数", example = "10")
    private Integer deletedUserCount;

    @Schema(description = "跳过用户数", example = "40")
    private Integer skippedUserCount;

    @Schema(description = "失败用户数", example = "5")
    private Integer failedUserCount;

    @Schema(description = "新增用户列表")
    private List<SyncedUser> addedUsers;

    @Schema(description = "更新用户列表")
    private List<SyncedUser> updatedUsers;

    @Schema(description = "删除用户列表")
    private List<SyncedUser> deletedUsers;

    @Schema(description = "失败记录列表")
    private List<SyncFailedRecord> failedRecords;

    @Schema(description = "同步配置信息")
    private SyncConfiguration syncConfig;

    @Schema(description = "同步统计信息")
    private SyncStatistics syncStatistics;

    @Schema(description = "错误信息", example = "部分用户同步失败")
    private String errorMessage;

    @Schema(description = "同步日志文件路径", example = "/logs/sync_20240826_001.log")
    private String logFilePath;

    @Schema(description = "操作人", example = "admin")
    private String operateBy;

    @Schema(description = "备注", example = "定时同步任务")
    private String remark;

    /**
     * 同步用户信息
     */
    @Data
    @Schema(description = "同步用户信息")
    public static class SyncedUser {
        @Schema(description = "本地用户ID", example = "1001")
        private Long localUserId;

        @Schema(description = "源系统用户ID", example = "ldap_1001")
        private String sourceUserId;

        @Schema(description = "用户名", example = "zhangsan")
        private String username;

        @Schema(description = "真实姓名", example = "张三")
        private String realName;

        @Schema(description = "邮箱", example = "zhangsan@example.com")
        private String email;

        @Schema(description = "部门", example = "技术部")
        private String department;

        @Schema(description = "职位", example = "软件工程师")
        private String position;

        @Schema(description = "操作类型", example = "ADD", allowableValues = {"ADD", "UPDATE", "DELETE"})
        private String operation;

        @Schema(description = "同步时间", example = "2024-08-26T10:05:00")
        private LocalDateTime syncTime;

        @Schema(description = "变更字段", example = "[\"email\", \"department\"]")
        private List<String> changedFields;
    }

    /**
     * 同步失败记录
     */
    @Data
    @Schema(description = "同步失败记录")
    public static class SyncFailedRecord {
        @Schema(description = "源系统用户ID", example = "ldap_1002")
        private String sourceUserId;

        @Schema(description = "用户名", example = "lisi")
        private String username;

        @Schema(description = "失败原因", example = "VALIDATION_ERROR")
        private String failureReason;

        @Schema(description = "错误消息", example = "邮箱格式不正确")
        private String errorMessage;

        @Schema(description = "失败时间", example = "2024-08-26T10:08:00")
        private LocalDateTime failureTime;

        @Schema(description = "重试次数", example = "3")
        private Integer retryCount;
    }

    /**
     * 同步配置
     */
    @Data
    @Schema(description = "同步配置")
    public static class SyncConfiguration {
        @Schema(description = "同步服务器", example = "ldap://192.168.1.100:389")
        private String syncServer;

        @Schema(description = "同步基础DN", example = "ou=users,dc=company,dc=com")
        private String baseDn;

        @Schema(description = "同步过滤器", example = "(objectClass=person)")
        private String filter;

        @Schema(description = "字段映射配置")
        private FieldMapping fieldMapping;

        @Schema(description = "是否自动创建用户", example = "true")
        private Boolean autoCreateUser;

        @Schema(description = "是否自动更新用户", example = "true")
        private Boolean autoUpdateUser;

        @Schema(description = "是否自动删除用户", example = "false")
        private Boolean autoDeleteUser;

        @Schema(description = "同步间隔（分钟）", example = "60")
        private Integer syncInterval;
    }

    /**
     * 字段映射
     */
    @Data
    @Schema(description = "字段映射")
    public static class FieldMapping {
        @Schema(description = "用户名字段", example = "sAMAccountName")
        private String usernameField;

        @Schema(description = "真实姓名字段", example = "displayName")
        private String realNameField;

        @Schema(description = "邮箱字段", example = "mail")
        private String emailField;

        @Schema(description = "手机字段", example = "mobile")
        private String phoneField;

        @Schema(description = "部门字段", example = "department")
        private String departmentField;

        @Schema(description = "职位字段", example = "title")
        private String positionField;
    }

    /**
     * 同步统计
     */
    @Data
    @Schema(description = "同步统计")
    public static class SyncStatistics {
        @Schema(description = "按部门统计新增用户")
        private List<DepartmentAddCount> departmentAddCounts;

        @Schema(description = "按用户类型统计")
        private List<UserTypeSyncCount> userTypeSyncCounts;

        @Schema(description = "错误类型统计")
        private List<ErrorTypeSyncCount> errorTypeCounts;

        @Schema(description = "同步性能指标")
        private SyncPerformance performance;
    }

    @Data
    @Schema(description = "部门新增用户统计")
    public static class DepartmentAddCount {
        @Schema(description = "部门名称", example = "技术部")
        private String departmentName;

        @Schema(description = "新增用户数", example = "15")
        private Integer addCount;
    }

    @Data
    @Schema(description = "用户类型同步统计")
    public static class UserTypeSyncCount {
        @Schema(description = "用户类型", example = "NORMAL")
        private String userType;

        @Schema(description = "同步数量", example = "120")
        private Integer syncCount;
    }

    @Data
    @Schema(description = "错误类型统计")
    public static class ErrorTypeSyncCount {
        @Schema(description = "错误类型", example = "NETWORK_ERROR")
        private String errorType;

        @Schema(description = "错误次数", example = "3")
        private Integer errorCount;
    }

    @Data
    @Schema(description = "同步性能指标")
    public static class SyncPerformance {
        @Schema(description = "平均处理速度（用户/秒）", example = "1.33")
        private Double avgProcessingSpeed;

        @Schema(description = "峰值处理速度（用户/秒）", example = "2.5")
        private Double peakProcessingSpeed;

        @Schema(description = "网络延迟（毫秒）", example = "50")
        private Integer networkLatency;

        @Schema(description = "数据库操作耗时（毫秒）", example = "200")
        private Integer dbOperationTime;
    }
}

