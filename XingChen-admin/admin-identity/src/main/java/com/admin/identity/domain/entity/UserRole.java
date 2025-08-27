package com.admin.identity.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@TableName("sys_user_role")
public class UserRole {

    /**
     * 关联ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 分配时间
     */
    private LocalDateTime assignTime;

    /**
     * 分配人
     */
    private String assignBy;

    /**
     * 分配原因
     */
    private String assignReason;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 分配状态(0-正常,1-停用,2-过期)
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户信息（关联查询）
     */
    @TableField(exist = false)
    private User user;

    /**
     * 角色信息（关联查询）
     */
    @TableField(exist = false)
    private Role role;

    /**
     * 分配状态枚举
     */
    public enum AssignmentStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用"),
        EXPIRED("2", "过期");

        private final String code;
        private final String description;

        AssignmentStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
