package com.admin.identity.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色权限关联实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@TableName("sys_role_permission")
public class RolePermission {

    /**
     * 关联ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限ID
     */
    private Long permissionId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 角色信息（关联查询）
     */
    @TableField(exist = false)
    private Role role;

    /**
     * 权限信息（关联查询）
     */
    @TableField(exist = false)
    private Permission permission;
}
