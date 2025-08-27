package com.admin.identity.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户响应DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class UserResponseDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

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
     * 性别描述
     */
    private String sexDesc;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 地址
     */
    private String address;

    /**
     * 员工工号
     */
    private String employeeNo;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 职位
     */
    private String position;

    /**
     * 直属上级ID
     */
    private Long superiorId;

    /**
     * 直属上级姓名
     */
    private String superiorName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户类型描述
     */
    private String userTypeDesc;

    /**
     * 用户来源
     */
    private String userSource;

    /**
     * 用户来源描述
     */
    private String userSourceDesc;

    /**
     * 用户标签
     */
    private String userTags;

    /**
     * 用户标签列表
     */
    private List<String> userTagList;

    /**
     * 账号状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 密码更新时间
     */
    private LocalDateTime passwordUpdateTime;

    /**
     * 锁定时间
     */
    private LocalDateTime lockTime;

    /**
     * 解锁时间
     */
    private LocalDateTime unlockTime;

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
    private List<UserOrganizationInfo> organizations;

    /**
     * 角色列表
     */
    private List<UserRoleInfo> roles;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 扩展属性
     */
    private Map<String, Object> extendedAttributes;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户组织信息
     */
    @Data
    public static class UserOrganizationInfo {
        private Long orgId;
        private String orgName;
        private String orgCode;
        private String position;
        private Boolean isPrimary;
        private LocalDateTime joinTime;
        private String status;
    }

    /**
     * 用户角色信息
     */
    @Data
    public static class UserRoleInfo {
        private Long roleId;
        private String roleName;
        private String roleKey;
        private Long orgId;
        private String orgName;
        private LocalDateTime effectiveTime;
        private LocalDateTime expireTime;
        private String status;
    }
}
