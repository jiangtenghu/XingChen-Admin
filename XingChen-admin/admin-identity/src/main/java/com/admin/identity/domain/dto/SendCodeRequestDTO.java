package com.admin.identity.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 发送验证码请求DTO
 *
 * @author admin
 * @since 2024-08-26
 */
@Data
public class SendCodeRequestDTO {

    /**
     * 验证码类型（EMAIL、SMS）
     */
    @NotBlank(message = "验证码类型不能为空")
    @Pattern(regexp = "^(EMAIL|SMS)$", message = "验证码类型只能是EMAIL或SMS")
    private String type;

    /**
     * 目标地址（邮箱或手机号）
     */
    @NotBlank(message = "目标地址不能为空")
    private String target;

    /**
     * 使用场景（REGISTER、LOGIN、RESET_PASSWORD、CHANGE_PHONE、CHANGE_EMAIL）
     */
    @Pattern(regexp = "^(REGISTER|LOGIN|RESET_PASSWORD|CHANGE_PHONE|CHANGE_EMAIL)$", 
             message = "使用场景不正确")
    private String scene;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 客户端IP
     */
    private String clientIp;
}