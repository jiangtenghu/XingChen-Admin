package com.admin.identity.controller;

import com.admin.common.core.domain.Result;
import com.admin.identity.domain.entity.Role;
import com.admin.identity.service.RoleService;
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
 * 角色管理控制器
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/identity/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色的增删改查等管理功能")
public class RoleController {

    private final RoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询角色", description = "根据租户ID和条件分页查询角色列表")
    public Result<IPage<Role>> pageRoles(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "租户ID") @RequestParam Long tenantId,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        Page<Role> page = new Page<>(current, size);
        IPage<Role> result = roleService.pageByTenant(page, tenantId, roleName, status);
        return Result.success("查询成功", result);
    }

    /**
     * 根据ID查询角色详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情", description = "根据角色ID查询角色详细信息")
    public Result<Role> getRoleById(@Parameter(description = "角色ID") @PathVariable Long id) {
        Role role = roleService.getById(id);
        if (role == null || role.getDelFlag() == 1) {
            return Result.error("角色不存在");
        }
        return Result.success("查询成功", role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    @Operation(summary = "创建角色", description = "创建新角色")
    public Result<Role> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return Result.success("创建成功", createdRole);
        } catch (Exception e) {
            log.error("创建角色失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色", description = "根据ID更新角色信息")
    public Result<Role> updateRole(@Parameter(description = "角色ID") @PathVariable Long id, 
                                  @RequestBody Role role) {
        try {
            role.setId(id);
            Role updatedRole = roleService.updateRole(role);
            return Result.success("更新成功", updatedRole);
        } catch (Exception e) {
            log.error("更新角色失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据ID删除角色")
    public Result<Void> deleteRole(@Parameter(description = "角色ID") @PathVariable Long id) {
        try {
            boolean deleted = roleService.deleteRole(id);
            if (deleted) {
                return Result.success("删除成功", null);
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "根据ID列表批量删除角色")
    public Result<Void> deleteBatch(@RequestBody List<Long> roleIds) {
        try {
            boolean deleted = roleService.deleteBatch(roleIds);
            if (deleted) {
                return Result.success("批量删除成功", null);
            }
            return Result.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除角色失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 分配权限
     */
    @PostMapping("/{id}/permissions")
    @Operation(summary = "分配权限", description = "为角色分配权限")
    public Result<Void> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        try {
            boolean assigned = roleService.assignPermissions(id, permissionIds);
            if (assigned) {
                return Result.success("权限分配成功", null);
            }
            return Result.error("权限分配失败");
        } catch (Exception e) {
            log.error("权限分配失败", e);
            return Result.error("权限分配失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID查询角色列表
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "查询用户角色", description = "根据用户ID查询其拥有的角色列表")
    public Result<List<Role>> getRolesByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<Role> roles = roleService.listByUserId(userId);
        return Result.success("查询成功", roles);
    }

    /**
     * 更改角色状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改角色状态", description = "启用或禁用角色")
    public Result<Void> changeStatus(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam String status) {
        try {
            boolean changed = roleService.changeStatus(id, status);
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
