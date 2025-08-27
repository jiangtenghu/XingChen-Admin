package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限响应DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionResponseDTO", description = "权限响应对象")
public class PermissionResponseDTO {

    @Schema(description = "权限ID", example = "1")
    private Long id;

    @Schema(description = "权限名称", example = "用户管理")
    private String permissionName;

    @Schema(description = "权限标识", example = "user:list")
    private String permissionKey;

    @Schema(description = "权限类型", example = "MENU")
    private String permissionType;

    @Schema(description = "权限类型名称", example = "菜单")
    private String permissionTypeName;

    @Schema(description = "父权限ID", example = "1")
    private Long parentId;

    @Schema(description = "父权限名称", example = "系统管理")
    private String parentName;

    @Schema(description = "资源路径", example = "/user/list")
    private String resourcePath;

    @Schema(description = "HTTP方法", example = "GET")
    private String httpMethod;

    @Schema(description = "前端组件路径", example = "/user/UserList.vue")
    private String componentPath;

    @Schema(description = "图标", example = "user")
    private String icon;

    @Schema(description = "权限状态", example = "ACTIVE")
    private String status;

    @Schema(description = "权限状态名称", example = "正常")
    private String statusName;

    @Schema(description = "是否为系统权限", example = "false")
    private Boolean isSystem;

    @Schema(description = "权限描述", example = "用户列表查看权限")
    private String remark;

    @Schema(description = "排序号", example = "1")
    private Integer orderNum;

    @Schema(description = "是否显示", example = "true")
    private Boolean visible;

    @Schema(description = "是否缓存", example = "false")
    private Boolean keepAlive;

    @Schema(description = "是否外链", example = "false")
    private Boolean isFrame;

    @Schema(description = "外链地址", example = "https://example.com")
    private String frameUrl;

    @Schema(description = "子权限列表")
    private List<PermissionResponseDTO> children;

    @Schema(description = "权限层级", example = "1")
    private Integer level;

    @Schema(description = "权限路径", example = "1,2,3")
    private String path;

    @Schema(description = "是否为叶子节点", example = "true")
    private Boolean isLeaf;

    @Schema(description = "关联的角色数量", example = "3")
    private Integer roleCount;

    @Schema(description = "关联的用户数量", example = "15")
    private Integer userCount;

    @Schema(description = "创建人", example = "admin")
    private String createBy;

    @Schema(description = "创建时间", example = "2024-01-01T00:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新人", example = "admin")
    private String updateBy;

    @Schema(description = "更新时间", example = "2024-01-02T00:00:00")
    private LocalDateTime updateTime;
}

