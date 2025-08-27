package com.admin.auth.domain.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Token验证结果DTO
 *
 * @author admin
 * @since 2024-08-27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResult {

    /**
     * 验证是否通过
     */
    private boolean valid;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 用户上下文
     */
    private UserContext userContext;

    /**
     * Token剩余有效时间（秒）
     */
    private Long remainingTime;

    /**
     * 验证时间
     */
    private LocalDateTime validationTime;

    /**
     * 用户上下文信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserContext {
        /**
         * 用户ID
         */
        private String userId;

        /**
         * 用户名
         */
        private String username;

        /**
         * 昵称
         */
        private String nickname;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 手机号
         */
        private String phone;

        /**
         * 租户ID
         */
        private String tenantId;

        /**
         * 用户类型
         */
        private String userType;

        /**
         * 用户角色
         */
        private List<String> roles;

        /**
         * 权限列表
         */
        private List<String> permissions;

        /**
         * 登录IP
         */
        private String loginIp;

        /**
         * 登录时间
         */
        private LocalDateTime loginTime;
    }

    /**
     * 创建成功的验证结果
     */
    public static TokenValidationResult success(UserContext userContext, Long remainingTime) {
        return TokenValidationResult.builder()
                .valid(true)
                .userContext(userContext)
                .remainingTime(remainingTime)
                .validationTime(LocalDateTime.now())
                .build();
    }

    /**
     * 创建失败的验证结果
     */
    public static TokenValidationResult failure(String errorMessage) {
        return TokenValidationResult.builder()
                .valid(false)
                .errorMessage(errorMessage)
                .validationTime(LocalDateTime.now())
                .build();
    }
}
