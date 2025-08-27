package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 用户统计DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "UserStatisticsDTO", description = "用户统计信息")
public class UserStatisticsDTO {

    @Schema(description = "总用户数", example = "1000")
    private Integer totalUsers;

    @Schema(description = "活跃用户数", example = "800")
    private Integer activeUsers;

    @Schema(description = "禁用用户数", example = "50")
    private Integer inactiveUsers;

    @Schema(description = "锁定用户数", example = "10")
    private Integer lockedUsers;

    @Schema(description = "今日新增用户数", example = "5")
    private Integer todayNewUsers;

    @Schema(description = "本周新增用户数", example = "25")
    private Integer weekNewUsers;

    @Schema(description = "本月新增用户数", example = "100")
    private Integer monthNewUsers;

    @Schema(description = "在线用户数", example = "150")
    private Integer onlineUsers;

    @Schema(description = "按用户类型统计")
    private List<UserTypeCount> userTypeCounts;

    @Schema(description = "按部门统计")
    private List<DepartmentCount> departmentCounts;

    @Schema(description = "按角色统计")
    private List<RoleCount> roleCounts;

    @Schema(description = "按性别统计")
    private List<GenderCount> genderCounts;

    @Schema(description = "按年龄段统计")
    private List<AgeRangeCount> ageRangeCounts;

    @Schema(description = "按入职年份统计")
    private List<HireYearCount> hireYearCounts;

    @Schema(description = "用户增长趋势（最近12个月）")
    private List<UserGrowthTrend> growthTrends;

    @Schema(description = "用户活跃度统计")
    private UserActivityStats activityStats;

    /**
     * 用户类型统计
     */
    @Data
    @Schema(description = "用户类型统计")
    public static class UserTypeCount {
        @Schema(description = "用户类型", example = "NORMAL")
        private String userType;

        @Schema(description = "用户类型名称", example = "普通用户")
        private String userTypeName;

        @Schema(description = "用户数量", example = "800")
        private Integer count;

        @Schema(description = "占比", example = "80.0")
        private Double percentage;
    }

    /**
     * 部门统计
     */
    @Data
    @Schema(description = "部门统计")
    public static class DepartmentCount {
        @Schema(description = "部门ID", example = "1")
        private Long departmentId;

        @Schema(description = "部门名称", example = "技术部")
        private String departmentName;

        @Schema(description = "用户数量", example = "120")
        private Integer count;

        @Schema(description = "占比", example = "12.0")
        private Double percentage;
    }

    /**
     * 角色统计
     */
    @Data
    @Schema(description = "角色统计")
    public static class RoleCount {
        @Schema(description = "角色ID", example = "1")
        private Long roleId;

        @Schema(description = "角色名称", example = "普通员工")
        private String roleName;

        @Schema(description = "用户数量", example = "500")
        private Integer count;

        @Schema(description = "占比", example = "50.0")
        private Double percentage;
    }

    /**
     * 性别统计
     */
    @Data
    @Schema(description = "性别统计")
    public static class GenderCount {
        @Schema(description = "性别", example = "MALE")
        private String gender;

        @Schema(description = "性别名称", example = "男")
        private String genderName;

        @Schema(description = "用户数量", example = "600")
        private Integer count;

        @Schema(description = "占比", example = "60.0")
        private Double percentage;
    }

    /**
     * 年龄段统计
     */
    @Data
    @Schema(description = "年龄段统计")
    public static class AgeRangeCount {
        @Schema(description = "年龄段", example = "25-30")
        private String ageRange;

        @Schema(description = "用户数量", example = "300")
        private Integer count;

        @Schema(description = "占比", example = "30.0")
        private Double percentage;
    }

    /**
     * 入职年份统计
     */
    @Data
    @Schema(description = "入职年份统计")
    public static class HireYearCount {
        @Schema(description = "入职年份", example = "2024")
        private String hireYear;

        @Schema(description = "用户数量", example = "150")
        private Integer count;

        @Schema(description = "占比", example = "15.0")
        private Double percentage;
    }

    /**
     * 用户增长趋势
     */
    @Data
    @Schema(description = "用户增长趋势")
    public static class UserGrowthTrend {
        @Schema(description = "月份", example = "2024-01")
        private String month;

        @Schema(description = "新增用户数", example = "50")
        private Integer newUsers;

        @Schema(description = "累计用户数", example = "950")
        private Integer totalUsers;

        @Schema(description = "增长率", example = "5.56")
        private Double growthRate;
    }

    /**
     * 用户活跃度统计
     */
    @Data
    @Schema(description = "用户活跃度统计")
    public static class UserActivityStats {
        @Schema(description = "今日活跃用户", example = "200")
        private Integer todayActiveUsers;

        @Schema(description = "本周活跃用户", example = "500")
        private Integer weekActiveUsers;

        @Schema(description = "本月活跃用户", example = "800")
        private Integer monthActiveUsers;

        @Schema(description = "平均在线时长（分钟）", example = "240")
        private Integer avgOnlineMinutes;

        @Schema(description = "活跃度排名前10的用户")
        private List<TopActiveUser> topActiveUsers;
    }

    /**
     * 最活跃用户
     */
    @Data
    @Schema(description = "最活跃用户")
    public static class TopActiveUser {
        @Schema(description = "用户ID", example = "1")
        private Long userId;

        @Schema(description = "用户名", example = "zhangsan")
        private String username;

        @Schema(description = "真实姓名", example = "张三")
        private String realName;

        @Schema(description = "部门", example = "技术部")
        private String department;

        @Schema(description = "本月在线时长（分钟）", example = "1200")
        private Integer monthlyOnlineMinutes;

        @Schema(description = "登录次数", example = "45")
        private Integer loginCount;
    }
}

