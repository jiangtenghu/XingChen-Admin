package com.admin.auth.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户注册DTO
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-27
 */
@Data
public class UserRegisterDTO {

    /**
     * 租户ID（可选，默认为1）
     */
    private Long tenantId = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 30, message = "用户名长度必须在3-30字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20字符之间")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$", 
             message = "密码必须包含至少一个字母和一个数字")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50字符")
    private String realName;

    /**
     * 昵称
     */
    @Size(max = 30, message = "昵称长度不能超过30字符")
    private String nickname;

    /**
     * 性别（0=男，1=女，2=未知）
     */
    private String sex = "2";

    /**
     * 用户类型（ADMIN=管理员，NORMAL=普通用户）
     */
    private String userType = "NORMAL";
}
