package com.admin.gateway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 令牌验证结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResult {
    
    /**
     * 是否有效
     */
    private boolean valid;
    
    /**
     * 错误信息
     */
    private String error;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 用户角色
     */
    private List<String> roles;
    
    /**
     * 数据权限范围
     */
    private String dataScope;
    
    /**
     * 用户权限
     */
    private List<String> permissions;
    
    /**
     * 创建成功的验证结果
     */
    public static TokenValidationResult success(String userId, String username, String tenantId, 
                                              List<String> roles, String dataScope, List<String> permissions) {
        return TokenValidationResult.builder()
                .valid(true)
                .userId(userId)
                .username(username)
                .tenantId(tenantId)
                .roles(roles)
                .dataScope(dataScope)
                .permissions(permissions)
                .build();
    }
    
    /**
     * 创建失败的验证结果
     */
    public static TokenValidationResult failure(String error) {
        return TokenValidationResult.builder()
                .valid(false)
                .error(error)
                .build();
    }
}
