package com.admin.identity.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Map;

/**
 * 组织更新DTO
 *
 * @author admin
 * @since 2024-01-15
 */
@Data
public class OrganizationUpdateDTO {

    /**
     * 组织ID
     */
    @NotNull(message = "组织ID不能为空")
    private Long id;

    /**
     * 组织名称
     */
    @Length(max = 64, message = "组织名称长度不能超过64个字符")
    private String orgName;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sortOrder;

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
     * 组织状态
     */
    @Pattern(regexp = "^(NORMAL|DISABLED)?$", message = "组织状态只能是NORMAL、DISABLED之一")
    private String status;

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
