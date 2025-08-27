package com.admin.system.controller;

import com.admin.common.core.domain.Result;
import com.admin.system.domain.entity.Menu;
import com.admin.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 */
@Tag(name = "菜单管理", description = "菜单增删改查等管理接口")
@RestController
@RequestMapping("/api/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Operation(summary = "查询菜单列表", description = "查询系统菜单列表")
    @GetMapping("/list")
    public Result<List<Menu>> list(Menu menu) {
        List<Menu> menuList = menuService.selectMenuList(menu);
        return Result.success(menuList);
    }

    @Operation(summary = "根据ID查询菜单", description = "根据菜单ID查询菜单详细信息")
    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @Operation(summary = "新增菜单", description = "新增系统菜单")
    @PostMapping
    public Result<String> add(@RequestBody Menu menu) {
        menuService.insertMenu(menu);
        return Result.success("新增成功");
    }

    @Operation(summary = "修改菜单", description = "修改系统菜单")
    @PutMapping
    public Result<String> update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success("修改成功");
    }

    @Operation(summary = "删除菜单", description = "根据菜单ID删除菜单")
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "获取菜单树", description = "获取菜单树结构")
    @GetMapping("/tree")
    public Result<List<Menu>> tree() {
        List<Menu> menuTree = menuService.buildMenuTree();
        return Result.success(menuTree);
    }
}
