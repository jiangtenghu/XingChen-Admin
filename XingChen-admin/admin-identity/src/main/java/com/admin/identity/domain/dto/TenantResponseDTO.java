package com.admin.identity.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 租户响应DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class TenantResponseDTO {

    /**
     * 租户ID
     */
    private Long id;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 租户编码
     */
    private String code;

    /**
     * 租户类型
     */
    private String type;

    /**
     * 租户类型描述
     */
    private String typeDesc;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 父租户ID
     */
    private Long parentId;

    /**
     * 父租户名称
     */
    private String parentName;

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
     * 企业规模描述
     */
    private String scaleDesc;

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
     * 当前用户数
     */
    private Integer currentUsers;

    /**
     * 最大存储空间(字节)
     */
    private Long maxStorage;

    /**
     * 当前存储使用量(字节)
     */
    private Long currentStorage;

    /**
     * 最大部门数
     */
    private Integer maxDepartments;

    /**
     * 当前部门数
     */
    private Integer currentDepartments;

    /**
     * 最大角色数
     */
    private Integer maxRoles;

    /**
     * 当前角色数
     */
    private Integer currentRoles;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 剩余天数
     */
    private Long remainingDays;

    /**
     * 自定义域名
     */
    private String domain;

    /**
     * 租户Logo
     */
    private String logo;

    /**
     * 功能配置
     */
    private Map<String, Object> featureConfig;

    /**
     * 主题配置
     */
    private Map<String, Object> themeConfig;

    /**
     * 安全配置
     */
    private Map<String, Object> securityConfig;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
