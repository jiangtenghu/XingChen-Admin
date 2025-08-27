package com.admin.identity.domain.entity;

import com.admin.common.web.domain.WebBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends WebBaseEntity {

    /**
     * 角色ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 显示顺序
     */
    private Integer roleSort;

    /**
     * 数据范围(1-全部,2-租户,3-本组织,4-本组织及以下,5-仅本人,6-自定义)
     */
    private String dataScope;

    /**
     * 数据范围组织ID集
     */
    private String dataScopeOrgIds;

    /**
     * 角色类型(SYSTEM/TENANT/CUSTOM/TEMPLATE)
     */
    private String roleType;

    /**
     * 角色状态(0-正常,1-停用)
     */
    private String status;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 权限列表（关联查询）
     */
    @TableField(exist = false)
    private List<Permission> permissions;

    /**
     * 权限ID列表（用于角色权限分配）
     */
    @TableField(exist = false)
    private List<Long> permissionIds;

    /**
     * 数据权限范围组织ID列表
     */
    @TableField(exist = false)
    private List<Long> dataScopeOrgIdList;

    /**
     * 数据范围枚举
     */
    public enum DataScope {
        ALL("1", "全部数据权限"),
        TENANT("2", "租户数据权限"),
        ORG("3", "本组织数据权限"),
        ORG_AND_SUB("4", "本组织及以下数据权限"),
        SELF("5", "仅本人数据权限"),
        CUSTOM("6", "自定义数据权限");

        private final String code;
        private final String description;

        DataScope(String code, String description) {
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

    /**
     * 角色类型枚举
     */
    public enum RoleType {
        SYSTEM("系统角色"),
        TENANT("租户角色"),
        CUSTOM("自定义角色"),
        TEMPLATE("模板角色");

        private final String description;

        RoleType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 角色状态枚举
     */
    public enum RoleStatus {
        NORMAL("0", "正常"),
        DISABLED("1", "停用");

        private final String code;
        private final String description;

        RoleStatus(String code, String description) {
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
