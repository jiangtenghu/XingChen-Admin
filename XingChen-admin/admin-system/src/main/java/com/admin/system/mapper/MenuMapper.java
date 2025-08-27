package com.admin.system.mapper;

import com.admin.system.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单数据访问层
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询菜单列表
     */
    List<Menu> selectMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询用户拥有的菜单树
     */
    List<Menu> selectMenuTreeByUserId(@Param("userId") Long userId);
}
