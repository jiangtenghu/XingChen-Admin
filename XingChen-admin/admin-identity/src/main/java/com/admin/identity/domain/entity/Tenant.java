package com.admin.identity.domain.entity;

import com.admin.common.web.domain.WebBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 租户实体类
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_tenant", autoResultMap = true)
public class Tenant extends WebBaseEntity {

    /**
     * 租户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户编码（唯一）
     */
    private String code;

    /**
     * 租户类型(ENTERPRISE/STANDARD/PERSONAL/SYSTEM)
     */
    private String type;

    /**
     * 状态(ACTIVE/INACTIVE/SUSPENDED/EXPIRED)
     */
    private String status;

    /**
     * 父租户ID
     */
    private Long parentId;

    /**
     * 租户层级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 所在地区
     */
    private String region;

    /**
     * 营业执照号
     */
    private String businessLicense;

    /**
     * 最大用户数
     */
    private Integer maxUsers;

    /**
     * 最大存储空间(字节)
     */
    private Long maxStorage;

    /**
     * 最大部门数
     */
    private Integer maxDepartments;

    /**
     * 最大角色数
     */
    private Integer maxRoles;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 租户Logo
     */
    private String logo;

    /**
     * 功能配置JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> featureConfig;

    /**
     * 主题配置JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> themeConfig;

    /**
     * 安全配置JSON
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> securityConfig;

    /**
     * 租户类型枚举
     */
    public enum TenantType {
        SYSTEM("系统租户"),
        ENTERPRISE("企业租户"),
        STANDARD("标准租户"),
        PERSONAL("个人租户");

        private final String description;

        TenantType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 租户状态枚举
     */
    public enum TenantStatus {
        ACTIVE("正常"),
        INACTIVE("未激活"),
        SUSPENDED("暂停"),
        EXPIRED("过期");

        private final String description;

        TenantStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
