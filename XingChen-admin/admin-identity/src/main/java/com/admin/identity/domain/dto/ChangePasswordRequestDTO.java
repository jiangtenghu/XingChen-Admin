package com.admin.identity.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改密码请求DTO
 *
 * @author admin
 * @since 2024-08-26
 */
@Data
public class ChangePasswordRequestDTO {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在6-32字符之间")
    private String newPassword;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    /**
     * 验证码（如果开启了二次验证）
     */
    private String verificationCode;

    /**
     * 客户端IP
     */
    private String clientIp;
}