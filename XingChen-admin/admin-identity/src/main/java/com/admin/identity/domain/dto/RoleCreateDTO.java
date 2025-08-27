package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色创建DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "RoleCreateDTO", description = "角色创建请求对象")
public class RoleCreateDTO {

    @Schema(description = "租户ID", example = "1")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "角色名称", example = "系统管理员")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String roleName;

    @Schema(description = "角色标识", example = "ROLE_ADMIN")
    @NotBlank(message = "角色标识不能为空")
    @Pattern(regexp = "^[A-Z_][A-Z0-9_]*$", message = "角色标识只能包含大写字母、数字和下划线，且以大写字母或下划线开头")
    @Size(max = 100, message = "角色标识长度不能超过100个字符")
    private String roleKey;

    @Schema(description = "数据权限范围", example = "1", allowableValues = {"1", "2", "3", "4", "5"})
    private String dataScope = "5"; // 默认仅本人数据权限

    @Schema(description = "数据权限关联的组织ID列表", example = "[1, 2, 3]")
    private String dataScopeOrgIds;

    @Schema(description = "角色类型", example = "BUSINESS", allowableValues = {"SYSTEM", "BUSINESS", "CUSTOM"})
    @Pattern(regexp = "^(SYSTEM|BUSINESS|CUSTOM)$", message = "角色类型必须是SYSTEM、BUSINESS或CUSTOM")
    private String roleType = "BUSINESS";

    @Schema(description = "角色状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "角色状态必须是ACTIVE或INACTIVE")
    private String status = "ACTIVE";

    @Schema(description = "生效时间", example = "2024-01-01T00:00:00")
    private LocalDateTime effectiveTime;

    @Schema(description = "过期时间", example = "2025-12-31T23:59:59")
    private LocalDateTime expireTime;

    @Schema(description = "角色描述", example = "系统超级管理员，拥有所有权限")
    @Size(max = 500, message = "角色描述长度不能超过500个字符")
    private String remark;

    @Schema(description = "权限ID列表", example = "[1, 2, 3, 4, 5]")
    private List<Long> permissionIds;

    @Schema(description = "角色排序", example = "1")
    private Integer orderNum = 999;
}

