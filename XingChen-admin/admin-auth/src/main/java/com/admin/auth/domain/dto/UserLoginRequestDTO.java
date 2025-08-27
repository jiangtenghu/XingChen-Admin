package com.admin.auth.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录请求DTO
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-27
 */
@Data
public class UserLoginRequestDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 登录IP（由控制器设置）
     */
    private String loginIp;

    /**
     * 验证码（可选）
     */
    private String captcha;

    /**
     * 验证码key（可选）
     */
    private String captchaKey;

    /**
     * 记住我（可选）
     */
    private Boolean rememberMe = false;
}
