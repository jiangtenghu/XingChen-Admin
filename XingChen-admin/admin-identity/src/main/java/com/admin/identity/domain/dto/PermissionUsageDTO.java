package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限使用情况DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionUsageDTO", description = "权限使用情况")
public class PermissionUsageDTO {

    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    @Schema(description = "权限名称", example = "用户列表")
    private String permissionName;

    @Schema(description = "权限标识", example = "user:list")
    private String permissionKey;

    @Schema(description = "权限类型", example = "MENU")
    private String permissionType;

    @Schema(description = "资源路径", example = "/user/list")
    private String resourcePath;

    @Schema(description = "总使用次数", example = "1500")
    private Integer totalUsageCount;

    @Schema(description = "今日使用次数", example = "25")
    private Integer todayUsageCount;

    @Schema(description = "本周使用次数", example = "180")
    private Integer weekUsageCount;

    @Schema(description = "本月使用次数", example = "720")
    private Integer monthUsageCount;

    @Schema(description = "最后使用时间", example = "2024-08-26T10:30:00")
    private LocalDateTime lastUsedTime;

    @Schema(description = "最后使用用户", example = "admin")
    private String lastUsedBy;

    @Schema(description = "平均每日使用次数", example = "12.5")
    private Double avgDailyUsage;

    @Schema(description = "使用频率等级", example = "HIGH", allowableValues = {"HIGH", "MEDIUM", "LOW", "NEVER"})
    private String usageFrequency;

    @Schema(description = "分配给角色数", example = "5")
    private Integer assignedRoleCount;

    @Schema(description = "分配给用户数", example = "150")
    private Integer assignedUserCount;

    @Schema(description = "实际使用用户数", example = "120")
    private Integer actualUserCount;

    @Schema(description = "使用率", example = "80.0")
    private Double usageRate;

    @Schema(description = "权限热度评分", example = "85.5")
    private Double heatScore;

    @Schema(description = "使用趋势", example = "INCREASING", allowableValues = {"INCREASING", "STABLE", "DECREASING"})
    private String usageTrend;

    @Schema(description = "分配的角色列表")
    private List<AssignedRole> assignedRoles;

    @Schema(description = "最近使用记录")
    private List<RecentUsageRecord> recentUsageRecords;

    @Schema(description = "按时间段使用统计")
    private List<UsageByTimeSlot> usageByTimeSlots;

    @Schema(description = "按用户类型使用统计")
    private List<UsageByUserType> usageByUserTypes;

    @Schema(description = "使用地理分布")
    private List<UsageByLocation> usageByLocations;

    @Schema(description = "异常使用检测")
    private AnomalyDetection anomalyDetection;

    /**
     * 分配的角色
     */
    @Data
    @Schema(description = "分配的角色")
    public static class AssignedRole {
        @Schema(description = "角色ID", example = "1")
        private Long roleId;

        @Schema(description = "角色名称", example = "系统管理员")
        private String roleName;

        @Schema(description = "角色用户数", example = "5")
        private Integer userCount;

        @Schema(description = "该角色使用次数", example = "800")
        private Integer usageCount;

        @Schema(description = "使用占比", example = "53.3")
        private Double usagePercentage;
    }

    /**
     * 最近使用记录
     */
    @Data
    @Schema(description = "最近使用记录")
    public static class RecentUsageRecord {
        @Schema(description = "使用时间", example = "2024-08-26T10:30:00")
        private LocalDateTime usageTime;

        @Schema(description = "使用用户ID", example = "1")
        private Long userId;

        @Schema(description = "使用用户名", example = "admin")
        private String username;

        @Schema(description = "用户真实姓名", example = "管理员")
        private String realName;

        @Schema(description = "使用IP", example = "192.168.1.100")
        private String usageIp;

        @Schema(description = "使用地点", example = "北京")
        private String usageLocation;

        @Schema(description = "用户代理", example = "Chrome/119.0")
        private String userAgent;

        @Schema(description = "响应时间（毫秒）", example = "150")
        private Integer responseTime;

        @Schema(description = "请求状态", example = "SUCCESS")
        private String requestStatus;
    }

    /**
     * 按时间段使用统计
     */
    @Data
    @Schema(description = "按时间段使用统计")
    public static class UsageByTimeSlot {
        @Schema(description = "时间段", example = "09:00-10:00")
        private String timeSlot;

        @Schema(description = "使用次数", example = "120")
        private Integer usageCount;

        @Schema(description = "独立用户数", example = "25")
        private Integer uniqueUserCount;

        @Schema(description = "平均响应时间（毫秒）", example = "180")
        private Integer avgResponseTime;

        @Schema(description = "占比", example = "8.0")
        private Double percentage;
    }

    /**
     * 按用户类型使用统计
     */
    @Data
    @Schema(description = "按用户类型使用统计")
    public static class UsageByUserType {
        @Schema(description = "用户类型", example = "ADMIN")
        private String userType;

        @Schema(description = "用户类型名称", example = "管理员")
        private String userTypeName;

        @Schema(description = "使用次数", example = "900")
        private Integer usageCount;

        @Schema(description = "用户数", example = "3")
        private Integer userCount;

        @Schema(description = "人均使用次数", example = "300")
        private Double avgUsagePerUser;

        @Schema(description = "占比", example = "60.0")
        private Double percentage;
    }

    /**
     * 按地理位置使用统计
     */
    @Data
    @Schema(description = "按地理位置使用统计")
    public static class UsageByLocation {
        @Schema(description = "地理位置", example = "北京")
        private String location;

        @Schema(description = "使用次数", example = "800")
        private Integer usageCount;

        @Schema(description = "独立用户数", example = "50")
        private Integer uniqueUserCount;

        @Schema(description = "占比", example = "53.3")
        private Double percentage;

        @Schema(description = "平均访问时长（分钟）", example = "15")
        private Integer avgSessionDuration;
    }

    /**
     * 异常使用检测
     */
    @Data
    @Schema(description = "异常使用检测")
    public static class AnomalyDetection {
        @Schema(description = "是否检测到异常", example = "false")
        private Boolean hasAnomaly;

        @Schema(description = "异常类型", example = "UNUSUAL_FREQUENCY")
        private String anomalyType;

        @Schema(description = "异常描述", example = "使用频率异常增高")
        private String anomalyDescription;

        @Schema(description = "异常严重程度", example = "MEDIUM", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
        private String severity;

        @Schema(description = "异常开始时间", example = "2024-08-26T08:00:00")
        private LocalDateTime anomalyStartTime;

        @Schema(description = "建议处理方案", example = "检查是否有自动化脚本在频繁访问")
        private String recommendedAction;

        @Schema(description = "相关用户列表")
        private List<String> relatedUsers;

        @Schema(description = "风险评分", example = "3.5")
        private Double riskScore;
    }
}

