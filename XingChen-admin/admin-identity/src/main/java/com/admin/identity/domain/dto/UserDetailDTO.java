package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "UserDetailDTO", description = "用户详情对象")
public class UserDetailDTO {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "租户信息")
    private TenantResponseDTO tenant;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "性别", example = "MALE")
    private String gender;

    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    @Schema(description = "用户状态", example = "ACTIVE")
    private String status;

    @Schema(description = "用户类型", example = "NORMAL")
    private String userType;

    @Schema(description = "最后登录时间", example = "2024-01-01T00:00:00")
    private LocalDateTime loginDate;

    @Schema(description = "最后登录IP", example = "192.168.1.100")
    private String loginIp;

    @Schema(description = "个人简介", example = "系统管理员")
    private String profile;

    @Schema(description = "工号", example = "E001")
    private String employeeId;

    @Schema(description = "职位", example = "系统管理员")
    private String position;

    @Schema(description = "部门", example = "技术部")
    private String department;

    @Schema(description = "直属上级", example = "李四")
    private String supervisor;

    @Schema(description = "入职日期", example = "2024-01-01")
    private LocalDate hireDate;

    @Schema(description = "办公地点", example = "北京")
    private String officeLocation;

    @Schema(description = "紧急联系人", example = "王五")
    private String emergencyContact;

    @Schema(description = "紧急联系电话", example = "13900139000")
    private String emergencyPhone;

    @Schema(description = "地址", example = "北京市朝阳区")
    private String address;

    @Schema(description = "身份证号", example = "110101199001011234")
    private String idCard;

    @Schema(description = "学历", example = "本科")
    private String education;

    @Schema(description = "毕业院校", example = "北京大学")
    private String graduateSchool;

    @Schema(description = "专业", example = "计算机科学与技术")
    private String major;

    @Schema(description = "角色列表")
    private List<RoleResponseDTO> roles;

    @Schema(description = "组织列表")
    private List<OrganizationResponseDTO> organizations;

    @Schema(description = "权限列表")
    private List<PermissionResponseDTO> permissions;

    @Schema(description = "用户标签", example = "[\"技术专家\", \"团队领导\"]")
    private List<String> tags;

    @Schema(description = "扩展属性")
    private UserExtension extension;

    @Schema(description = "账户安全信息")
    private AccountSecurity security;

    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Schema(description = "更新时间", example = "2024-01-02T00:00:00")
    private LocalDateTime updateTime;

    /**
     * 用户扩展信息
     */
    @Data
    @Schema(description = "用户扩展信息")
    public static class UserExtension {
        @Schema(description = "技能标签", example = "[\"Java\", \"Spring\", \"Vue\"]")
        private List<String> skills;

        @Schema(description = "语言能力", example = "[\"中文\", \"英语\"]")
        private List<String> languages;

        @Schema(description = "工作经验年限", example = "5")
        private Integer workExperience;

        @Schema(description = "个人兴趣", example = "[\"编程\", \"阅读\", \"旅游\"]")
        private List<String> interests;

        @Schema(description = "社交账号")
        private SocialAccounts socialAccounts;
    }

    /**
     * 社交账号
     */
    @Data
    @Schema(description = "社交账号")
    public static class SocialAccounts {
        @Schema(description = "微信号", example = "wechat_123")
        private String wechat;

        @Schema(description = "QQ号", example = "123456789")
        private String qq;

        @Schema(description = "钉钉号", example = "dingtalk_123")
        private String dingtalk;

        @Schema(description = "GitHub", example = "github_username")
        private String github;

        @Schema(description = "LinkedIn", example = "linkedin_profile")
        private String linkedin;
    }

    /**
     * 账户安全信息
     */
    @Data
    @Schema(description = "账户安全信息")
    public static class AccountSecurity {
        @Schema(description = "是否启用双因子认证", example = "true")
        private Boolean mfaEnabled;

        @Schema(description = "密码最后修改时间", example = "2024-01-01T00:00:00")
        private LocalDateTime passwordChangedAt;

        @Schema(description = "登录失败次数", example = "0")
        private Integer failedLoginAttempts;

        @Schema(description = "账户锁定到期时间", example = "2024-01-01T00:00:00")
        private LocalDateTime lockoutEndTime;

        @Schema(description = "最近登录记录")
        private List<LoginRecord> recentLogins;
    }

    /**
     * 登录记录
     */
    @Data
    @Schema(description = "登录记录")
    public static class LoginRecord {
        @Schema(description = "登录时间", example = "2024-01-01T00:00:00")
        private LocalDateTime loginTime;

        @Schema(description = "登录IP", example = "192.168.1.100")
        private String loginIp;

        @Schema(description = "登录地点", example = "北京市")
        private String loginLocation;

        @Schema(description = "登录设备", example = "Chrome/Windows")
        private String loginDevice;

        @Schema(description = "登录状态", example = "SUCCESS")
        private String loginStatus;
    }
}

