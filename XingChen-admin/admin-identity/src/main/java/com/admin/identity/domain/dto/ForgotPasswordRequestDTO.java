package com.admin.identity.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 找回密码请求DTO
 *
 * @author admin
 * @since 2024-08-26
 */
@Data
public class ForgotPasswordRequestDTO {

    /**
     * 登录账号（用户名或邮箱）
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 找回方式（EMAIL、SMS）
     */
    @NotBlank(message = "找回方式不能为空")
    private String method;

    /**
     * 验证码（如果通过验证码找回）
     */
    private String verificationCode;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 客户端IP
     */
    private String clientIp;
}