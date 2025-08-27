package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 权限查询DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionQueryDTO", description = "权限查询请求对象")
public class PermissionQueryDTO {

    @Schema(description = "权限名称（模糊查询）", example = "用户")
    private String permissionName;

    @Schema(description = "权限标识（模糊查询）", example = "user")
    private String permissionKey;

    @Schema(description = "权限类型", example = "MENU", allowableValues = {"MENU", "BUTTON", "API", "DATA"})
    private String permissionType;

    @Schema(description = "父权限ID", example = "1")
    private Long parentId;

    @Schema(description = "权限状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;

    @Schema(description = "是否为系统权限", example = "false")
    private Boolean isSystem;

    @Schema(description = "资源路径（模糊查询）", example = "/user")
    private String resourcePath;

    @Schema(description = "HTTP方法", example = "GET", allowableValues = {"GET", "POST", "PUT", "DELETE", "PATCH"})
    private String httpMethod;

    @Schema(description = "是否显示", example = "true")
    private Boolean visible;

    @Schema(description = "是否外链", example = "false")
    private Boolean isFrame;

    @Schema(description = "是否返回树形结构", example = "true")
    private Boolean returnTree = false;

    @Schema(description = "是否包含按钮权限", example = "true")
    private Boolean includeButtons = true;

    @Schema(description = "角色ID（查询角色关联的权限）", example = "1")
    private Long roleId;

    @Schema(description = "用户ID（查询用户关联的权限）", example = "1")
    private Long userId;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段", example = "orderNum")
    private String orderBy = "orderNum";

    @Schema(description = "排序方向", example = "asc", allowableValues = {"asc", "desc"})
    private String orderDirection = "asc";
}

