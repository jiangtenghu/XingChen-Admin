package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 权限创建DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionCreateDTO", description = "权限创建请求对象")
public class PermissionCreateDTO {

    @Schema(description = "权限名称", example = "用户管理")
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    private String permissionName;

    @Schema(description = "权限标识", example = "user:list")
    @NotBlank(message = "权限标识不能为空")
    @Pattern(regexp = "^[a-z0-9:_-]+$", message = "权限标识只能包含小写字母、数字、冒号、下划线和横线")
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String permissionKey;

    @Schema(description = "权限类型", example = "MENU", allowableValues = {"MENU", "BUTTON", "API", "DATA"})
    @Pattern(regexp = "^(MENU|BUTTON|API|DATA)$", message = "权限类型必须是MENU、BUTTON、API或DATA")
    private String permissionType = "MENU";

    @Schema(description = "父权限ID", example = "1")
    private Long parentId = 0L;

    @Schema(description = "资源路径", example = "/user/list")
    @Size(max = 200, message = "资源路径长度不能超过200个字符")
    private String resourcePath;

    @Schema(description = "HTTP方法", example = "GET", allowableValues = {"GET", "POST", "PUT", "DELETE", "PATCH"})
    @Pattern(regexp = "^(GET|POST|PUT|DELETE|PATCH)$", message = "HTTP方法必须是GET、POST、PUT、DELETE或PATCH")
    private String httpMethod;

    @Schema(description = "前端组件路径", example = "/user/UserList.vue")
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    private String componentPath;

    @Schema(description = "图标", example = "user")
    @Size(max = 50, message = "图标长度不能超过50个字符")
    private String icon;

    @Schema(description = "权限状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "权限状态必须是ACTIVE或INACTIVE")
    private String status = "ACTIVE";

    @Schema(description = "是否为系统权限", example = "false")
    private Boolean isSystem = false;

    @Schema(description = "权限描述", example = "用户列表查看权限")
    @Size(max = 500, message = "权限描述长度不能超过500个字符")
    private String remark;

    @Schema(description = "排序号", example = "1")
    private Integer orderNum = 999;

    @Schema(description = "是否显示", example = "true")
    private Boolean visible = true;

    @Schema(description = "是否缓存", example = "false")
    private Boolean keepAlive = false;

    @Schema(description = "是否外链", example = "false")
    private Boolean isFrame = false;

    @Schema(description = "外链地址", example = "https://example.com")
    @Size(max = 500, message = "外链地址长度不能超过500个字符")
    private String frameUrl;
}

