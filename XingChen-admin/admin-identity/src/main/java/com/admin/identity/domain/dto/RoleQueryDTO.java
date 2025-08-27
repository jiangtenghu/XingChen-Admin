package com.admin.identity.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 角色查询DTO
 *
 * @author XingChen
 * @date 2024-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "RoleQueryDTO", description = "角色查询请求对象")
public class RoleQueryDTO {

    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @Schema(description = "角色名称（模糊查询）", example = "管理员")
    private String roleName;

    @Schema(description = "角色标识（模糊查询）", example = "ADMIN")
    private String roleKey;

    @Schema(description = "角色类型", example = "BUSINESS", allowableValues = {"SYSTEM", "BUSINESS", "CUSTOM"})
    private String roleType;

    @Schema(description = "角色状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
    private String status;

    @Schema(description = "创建时间开始", example = "2024-01-01T00:00:00")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束", example = "2024-12-31T23:59:59")
    private LocalDateTime createTimeEnd;

    @Schema(description = "数据权限范围", example = "1", allowableValues = {"1", "2", "3", "4", "5"})
    private String dataScope;

    @Schema(description = "是否包含已过期角色", example = "false")
    private Boolean includeExpired = false;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "排序字段", example = "createTime")
    private String orderBy = "createTime";

    @Schema(description = "排序方向", example = "desc", allowableValues = {"asc", "desc"})
    private String orderDirection = "desc";
}

