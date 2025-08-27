package com.admin.auth.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户登录响应DTO
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-27
 */
@Data
public class UserLoginResponseDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 账户状态
     */
    private String accountStatus;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 登录是否成功
     */
    private Boolean loginSuccess;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 令牌过期时间（秒）
     */
    private Long expiresIn;
}
