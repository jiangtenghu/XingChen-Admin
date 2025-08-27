package com.admin.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.admin.common.core.exception.ServiceException;
import com.admin.system.domain.entity.Menu;
import com.admin.system.mapper.MenuMapper;
import com.admin.system.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.like(StrUtil.isNotEmpty(menu.getMenuName()), Menu::getMenuName, menu.getMenuName())
               .eq(StrUtil.isNotEmpty(menu.getVisible()), Menu::getVisible, menu.getVisible())
               .eq(StrUtil.isNotEmpty(menu.getStatus()), Menu::getStatus, menu.getStatus())
               .orderByAsc(Menu::getParentId)
               .orderByAsc(Menu::getOrderNum);

        return this.list(wrapper);
    }

    @Override
    public boolean insertMenu(Menu menu) {
        return this.save(menu);
    }

    @Override
    public boolean updateMenu(Menu menu) {
        return this.updateById(menu);
    }

    @Override
    public boolean deleteMenu(Long menuId) {
        // 检查是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId);
        long count = this.count(wrapper);
        
        if (count > 0) {
            throw new ServiceException("存在子菜单，不允许删除");
        }

        return this.removeById(menuId);
    }

    @Override
    public List<Menu> buildMenuTree() {
        List<Menu> menuList = this.list();
        return buildTree(menuList, 0L);
    }

    @Override
    public List<String> selectMenuPermsByUserId(Long userId) {
        // 根据用户角色查询权限
        List<Menu> userMenus = this.baseMapper.selectMenusByUserId(userId);
        return userMenus.stream()
                .filter(menu -> StrUtil.isNotEmpty(menu.getPerms()))
                .map(Menu::getPerms)
                .collect(Collectors.toList());
    }

    /**
     * 构建菜单树
     */
    private List<Menu> buildTree(List<Menu> menuList, Long parentId) {
        List<Menu> tree = new ArrayList<>();
        
        for (Menu menu : menuList) {
            if (parentId.equals(menu.getParentId())) {
                tree.add(menu);
            }
        }
        
        return tree;
    }
}
