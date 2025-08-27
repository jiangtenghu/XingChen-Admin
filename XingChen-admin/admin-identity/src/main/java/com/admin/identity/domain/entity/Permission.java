package com.admin.identity.domain.entity;

import com.admin.common.web.domain.WebBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 权限实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class Permission extends WebBaseEntity {

    /**
     * 权限ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限标识
     */
    private String permissionKey;

    /**
     * 权限类型(MENU/BUTTON/API/DATA)
     */
    private String permissionType;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限层级
     */
    private Integer level;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

    /**
     * 资源路径
     */
    private String resourcePath;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限状态(0-正常,1-停用)
     */
    private String status;

    /**
     * 是否系统权限
     */
    private Boolean isSystem;

    /**
     * 子权限列表（用于树形结构）
     */
    @TableField(exist = false)
    private List<Permission> children;

    /**
     * 权限类型枚举
     */
    public enum PermissionType {
        MENU("菜单权限"),
        BUTTON("按钮权限"),
        API("接口权限"),
        DATA("数据权限");

        private final String description;

        PermissionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 权限状态枚举
     */
    public enum PermissionStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String description;

        PermissionStatus(String code, String description) {
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
