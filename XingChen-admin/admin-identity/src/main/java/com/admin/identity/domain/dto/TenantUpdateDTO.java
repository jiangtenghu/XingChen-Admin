package com.admin.identity.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 租户更新DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class TenantUpdateDTO {

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long id;

    /**
     * 租户名称
     */
    @Length(max = 64, message = "租户名称长度不能超过64个字符")
    private String name;

    /**
     * 租户状态
     */
    @Pattern(regexp = "^(ACTIVE|INACTIVE|SUSPENDED|EXPIRED)?$", message = "租户状态只能是ACTIVE、INACTIVE、SUSPENDED、EXPIRED之一")
    private String status;

    /**
     * 联系人
     */
    @Length(max = 32, message = "联系人长度不能超过32个字符")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Email(message = "联系邮箱格式不正确")
    @Length(max = 64, message = "邮箱长度不能超过64个字符")
    private String contactEmail;

    /**
     * 所属行业
     */
    @Length(max = 32, message = "所属行业长度不能超过32个字符")
    private String industry;

    /**
     * 企业规模
     */
    @Pattern(regexp = "^(SMALL|MEDIUM|LARGE|ENTERPRISE)?$", message = "企业规模只能是SMALL、MEDIUM、LARGE、ENTERPRISE之一")
    private String scale;

    /**
     * 所在地区
     */
    @Length(max = 64, message = "所在地区长度不能超过64个字符")
    private String region;

    /**
     * 营业执照号
     */
    @Length(max = 64, message = "营业执照号长度不能超过64个字符")
    private String businessLicense;

    /**
     * 最大用户数
     */
    @Min(value = 1, message = "最大用户数不能小于1")
    @Max(value = 99999, message = "最大用户数不能超过99999")
    private Integer maxUsers;

    /**
     * 最大存储空间(字节)
     */
    @Min(value = 1048576, message = "最大存储空间不能小于1MB")
    private Long maxStorage;

    /**
     * 最大部门数
     */
    @Min(value = 1, message = "最大部门数不能小于1")
    @Max(value = 9999, message = "最大部门数不能超过9999")
    private Integer maxDepartments;

    /**
     * 最大角色数
     */
    @Min(value = 1, message = "最大角色数不能小于1")
    @Max(value = 999, message = "最大角色数不能超过999")
    private Integer maxRoles;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 过期时间
     */
    @Future(message = "过期时间必须是未来时间")
    private LocalDateTime expireTime;

    /**
     * 自定义域名
     */
    @Pattern(regexp = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+(com|cn|org|net|edu|gov|mil|info|biz|name|museum)$", 
            message = "自定义域名格式不正确")
    private String domain;

    /**
     * 租户Logo
     */
    @Length(max = 255, message = "Logo地址长度不能超过255个字符")
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
     * 备注
     */
    @Length(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
