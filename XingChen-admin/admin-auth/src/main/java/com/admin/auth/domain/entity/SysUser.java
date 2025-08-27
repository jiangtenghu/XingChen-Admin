package com.admin.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 认证服务用户实体
 * 
 * @author admin
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 状态(0正常 1禁用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否启用
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 账户未过期
     */
    @TableField("account_non_expired")
    private Boolean accountNonExpired;

    /**
     * 账户未锁定
     */
    @TableField("account_non_locked")
    private Boolean accountNonLocked;

    /**
     * 密码未过期
     */
    @TableField("credentials_non_expired")
    private Boolean credentialsNonExpired;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ================================ 工具方法 ================================

    /**
     * 构造默认的启用用户
     */
    public static SysUser createEnabledUser(String username, String password) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus(0);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setLoginCount(0);
        return user;
    }

    /**
     * 更新最后登录信息
     */
    public void updateLastLogin() {
        this.lastLoginTime = LocalDateTime.now();
        this.loginCount = (this.loginCount == null ? 0 : this.loginCount) + 1;
    }

    /**
     * 检查账户是否可用
     */
    public boolean isAccountAvailable() {
        return enabled != null && enabled
                && status != null && status == 0
                && accountNonExpired != null && accountNonExpired
                && accountNonLocked != null && accountNonLocked
                && credentialsNonExpired != null && credentialsNonExpired;
    }
}