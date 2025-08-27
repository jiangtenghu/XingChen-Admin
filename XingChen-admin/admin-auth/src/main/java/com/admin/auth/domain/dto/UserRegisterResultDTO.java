package com.admin.auth.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户注册结果DTO
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-27
 */
@Data
public class UserRegisterResultDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否需要邮箱验证
     */
    private Boolean needEmailVerification;

    /**
     * 是否需要手机验证
     */
    private Boolean needPhoneVerification;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 下一步提示
     */
    private String nextStepTip;
}
