package com.admin.identity.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;
import java.util.Map;

/**
 * 租户创建DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class TenantCreateDTO {

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    @Length(max = 64, message = "租户名称长度不能超过64个字符")
    private String name;

    /**
     * 租户编码（唯一）
     */
    @NotBlank(message = "租户编码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,32}$", message = "租户编码只能包含字母、数字、下划线、连字符，长度2-32位")
    private String code;

    /**
     * 租户类型
     */
    @NotBlank(message = "租户类型不能为空")
    @Pattern(regexp = "^(ENTERPRISE|STANDARD|PERSONAL)$", message = "租户类型只能是ENTERPRISE、STANDARD、PERSONAL之一")
    private String type;

    /**
     * 父租户ID
     */
    private Long parentId;

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
     * 套餐ID
     */
    private Long packageId;

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
