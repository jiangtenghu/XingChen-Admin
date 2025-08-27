package com.admin.identity.controller;

import com.admin.common.core.domain.Result;
import com.admin.identity.domain.entity.Permission;
import com.admin.identity.service.PermissionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/identity/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "权限的增删改查等管理功能")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 分页查询权限列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询权限", description = "根据条件分页查询权限列表")
    public Result<IPage<Permission>> pagePermissions(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "权限名称") @RequestParam(required = false) String permissionName,
            @Parameter(description = "权限类型") @RequestParam(required = false) String permissionType,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        Page<Permission> page = new Page<>(current, size);
        IPage<Permission> result = permissionService.pagePermissions(page, permissionName, permissionType, status);
        return Result.success("查询成功", result);
    }

    /**
     * 查询权限树
     */
    @GetMapping("/tree")
    @Operation(summary = "查询权限树", description = "查询菜单类型的权限树结构")
    public Result<List<Permission>> getPermissionTree() {
        List<Permission> tree = permissionService.getMenuTree();
        return Result.success("查询成功", tree);
    }

    /**
     * 根据ID查询权限详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询权限详情", description = "根据权限ID查询权限详细信息")
    public Result<Permission> getPermissionById(@Parameter(description = "权限ID") @PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        if (permission == null || permission.getDelFlag() == 1) {
            return Result.error("权限不存在");
        }
        return Result.success("查询成功", permission);
    }

    /**
     * 创建权限
     */
    @PostMapping
    @Operation(summary = "创建权限", description = "创建新权限")
    public Result<Permission> createPermission(@RequestBody Permission permission) {
        try {
            Permission createdPermission = permissionService.createPermission(permission);
            return Result.success("创建成功", createdPermission);
        } catch (Exception e) {
            log.error("创建权限失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新权限", description = "根据ID更新权限信息")
    public Result<Permission> updatePermission(@Parameter(description = "权限ID") @PathVariable Long id, 
                                              @RequestBody Permission permission) {
        try {
            permission.setId(id);
            Permission updatedPermission = permissionService.updatePermission(permission);
            return Result.success("更新成功", updatedPermission);
        } catch (Exception e) {
            log.error("更新权限失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限", description = "根据ID删除权限")
    public Result<Void> deletePermission(@Parameter(description = "权限ID") @PathVariable Long id) {
        try {
            boolean deleted = permissionService.deletePermission(id);
            if (deleted) {
                return Result.success("删除成功", null);
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除权限失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除权限
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除权限", description = "根据ID列表批量删除权限")
    public Result<Void> deleteBatch(@RequestBody List<Long> permissionIds) {
        try {
            boolean deleted = permissionService.deleteBatch(permissionIds);
            if (deleted) {
                return Result.success("批量删除成功", null);
            }
            return Result.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除权限失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据角色ID查询权限列表
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "查询角色权限", description = "根据角色ID查询其拥有的权限列表")
    public Result<List<Permission>> getPermissionsByRoleId(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<Permission> permissions = permissionService.listByRoleId(roleId);
        return Result.success("查询成功", permissions);
    }

    /**
     * 根据用户ID查询权限列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户权限", description = "根据用户ID查询其拥有的权限列表")
    public Result<List<Permission>> getPermissionsByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<Permission> permissions = permissionService.listByUserId(userId);
        return Result.success("查询成功", permissions);
    }

    /**
     * 根据用户ID查询权限标识列表
     */
    @GetMapping("/user/{userId}/keys")
    @Operation(summary = "查询用户权限标识", description = "根据用户ID查询其拥有的权限标识列表")
    public Result<List<String>> getPermissionKeysByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<String> permissionKeys = permissionService.getPermissionKeysByUserId(userId);
        return Result.success("查询成功", permissionKeys);
    }

    /**
     * 更改权限状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改权限状态", description = "启用或禁用权限")
    public Result<Void> changeStatus(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam String status) {
        try {
            boolean changed = permissionService.changeStatus(id, status);
            if (changed) {
                return Result.success("状态更改成功", null);
            }
            return Result.error("状态更改失败");
        } catch (Exception e) {
            log.error("状态更改失败", e);
            return Result.error("状态更改失败：" + e.getMessage());
        }
    }
}
