package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色响应DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "RoleResponseDTO", description = "角色响应对象")
public class RoleResponseDTO {

    @Schema(description = "角色ID", example = "1")
    private Long id;

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "租户名称", example = "默认租户")
    private String tenantName;

    @Schema(description = "角色名称", example = "系统管理员")
    private String roleName;

    @Schema(description = "角色标识", example = "ROLE_ADMIN")
    private String roleKey;

    @Schema(description = "数据权限范围", example = "1")
    private String dataScope;

    @Schema(description = "数据权限范围名称", example = "全部数据权限")
    private String dataScopeName;

    @Schema(description = "数据权限关联的组织ID列表")
    private String dataScopeOrgIds;

    @Schema(description = "数据权限关联的组织名称列表")
    private List<String> dataScopeOrgNames;

    @Schema(description = "角色类型", example = "BUSINESS")
    private String roleType;

    @Schema(description = "角色类型名称", example = "业务角色")
    private String roleTypeName;

    @Schema(description = "角色状态", example = "ACTIVE")
    private String status;

    @Schema(description = "角色状态名称", example = "正常")
    private String statusName;

    @Schema(description = "生效时间", example = "2024-01-01T00:00:00")
    private LocalDateTime effectiveTime;

    @Schema(description = "过期时间", example = "2025-12-31T23:59:59")
    private LocalDateTime expireTime;

    @Schema(description = "角色描述", example = "系统超级管理员，拥有所有权限")
    private String remark;

    @Schema(description = "角色排序", example = "1")
    private Integer orderNum;

    @Schema(description = "权限列表")
    private List<PermissionResponseDTO> permissions;

    @Schema(description = "权限ID列表", example = "[1, 2, 3, 4, 5]")
    private List<Long> permissionIds;

    @Schema(description = "用户数量", example = "5")
    private Integer userCount;

    @Schema(description = "是否为系统内置角色", example = "false")
    private Boolean isSystem;

    @Schema(description = "是否已过期", example = "false")
    private Boolean isExpired;

    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Schema(description = "更新时间", example = "2024-01-02T00:00:00")
    private LocalDateTime updateTime;
}

