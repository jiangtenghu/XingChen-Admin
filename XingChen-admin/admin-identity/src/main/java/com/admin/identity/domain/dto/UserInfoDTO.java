package com.admin.identity.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户信息DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class UserInfoDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 员工工号
     */
    private String employeeNo;

    /**
     * 职位
     */
    private String position;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 主组织ID
     */
    private Long primaryOrgId;

    /**
     * 主组织名称
     */
    private String primaryOrgName;

    /**
     * 组织列表
     */
    private List<UserOrgInfo> organizations;

    /**
     * 角色列表
     */
    private List<UserRoleInfo> roles;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 扩展属性
     */
    private Map<String, Object> extendedAttributes;

    /**
     * 用户组织信息
     */
    @Data
    public static class UserOrgInfo {
        private Long orgId;
        private String orgName;
        private String orgCode;
        private String position;
        private Boolean isPrimary;
    }

    /**
     * 用户角色信息
     */
    @Data
    public static class UserRoleInfo {
        private Long roleId;
        private String roleName;
        private String roleKey;
        private String dataScope;
    }
}
