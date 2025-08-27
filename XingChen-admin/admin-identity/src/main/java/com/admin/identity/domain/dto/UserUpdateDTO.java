package com.admin.identity.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 用户更新DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long id;

    /**
     * 用户昵称
     */
    @Length(max = 64, message = "用户昵称长度不能超过64个字符")
    private String nickname;

    /**
     * 真实姓名
     */
    @Length(max = 64, message = "真实姓名长度不能超过64个字符")
    private String realName;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Length(max = 64, message = "邮箱长度不能超过64个字符")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    /**
     * 用户性别
     */
    @Pattern(regexp = "^[012]$", message = "性别只能是0(男)、1(女)、2(未知)")
    private String sex;

    /**
     * 头像地址
     */
    @Length(max = 255, message = "头像地址长度不能超过255个字符")
    private String avatar;

    /**
     * 生日
     */
    @Past(message = "生日必须是过去的日期")
    private LocalDate birthday;

    /**
     * 地址
     */
    @Length(max = 255, message = "地址长度不能超过255个字符")
    private String address;

    /**
     * 员工工号
     */
    @Length(max = 32, message = "员工工号长度不能超过32个字符")
    private String employeeNo;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 职位
     */
    @Length(max = 64, message = "职位长度不能超过64个字符")
    private String position;

    /**
     * 直属上级ID
     */
    private Long superiorId;

    /**
     * 用户类型
     */
    @Pattern(regexp = "^(ADMIN|NORMAL|GUEST)$", message = "用户类型只能是ADMIN、NORMAL、GUEST之一")
    private String userType;

    /**
     * 用户标签
     */
    private String userTags;

    /**
     * 主组织ID
     */
    private Long primaryOrgId;

    /**
     * 组织列表（用户可以属于多个组织）
     */
    private List<UserCreateDTO.UserOrganizationDTO> organizations;

    /**
     * 角色ID列表
     */
    private List<Long> roleIds;

    /**
     * 扩展属性
     */
    private Map<String, Object> extendedAttributes;

    /**
     * 账号状态
     */
    @Pattern(regexp = "^[012]$", message = "账号状态只能是0(正常)、1(停用)、2(锁定)")
    private String status;

    /**
     * 备注
     */
    @Length(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
