package com.admin.system.service;

import com.admin.system.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查询菜单列表
     */
    List<Menu> selectMenuList(Menu menu);

    /**
     * 新增菜单
     */
    boolean insertMenu(Menu menu);

    /**
     * 修改菜单
     */
    boolean updateMenu(Menu menu);

    /**
     * 删除菜单
     */
    boolean deleteMenu(Long menuId);

    /**
     * 构建菜单树
     */
    List<Menu> buildMenuTree();

    /**
     * 根据用户ID查询菜单权限
     */
    List<String> selectMenuPermsByUserId(Long userId);
}
