package com.admin.identity.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Map;

/**
 * 组织创建DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class OrganizationCreateDTO {

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
    @Length(max = 64, message = "组织名称长度不能超过64个字符")
    private String orgName;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{2,32}$", message = "组织编码只能包含字母、数字、下划线、连字符，长度2-32位")
    private String orgCode;

    /**
     * 组织类型
     */
    @NotBlank(message = "组织类型不能为空")
    @Pattern(regexp = "^(COMPANY|DEPT|TEAM|GROUP)$", message = "组织类型只能是COMPANY、DEPT、TEAM、GROUP之一")
    private String orgType;

    /**
     * 父组织ID（0表示顶级）
     */
    private Long parentId = 0L;

    /**
     * 显示顺序
     */
    private Integer sortOrder = 0;

    /**
     * 负责人ID
     */
    private Long leaderId;

    /**
     * 副负责人ID
     */
    private Long deputyLeaderId;

    /**
     * 联系电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    @Length(max = 64, message = "邮箱长度不能超过64个字符")
    private String email;

    /**
     * 地址
     */
    @Length(max = 255, message = "地址长度不能超过255个字符")
    private String address;

    /**
     * 组织配置
     */
    private Map<String, Object> orgConfig;

    /**
     * 备注
     */
    @Length(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
