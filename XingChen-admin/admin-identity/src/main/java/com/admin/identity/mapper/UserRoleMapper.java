package com.admin.identity.mapper;

import com.admin.identity.domain.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 *
 * @author admin
 * @since 2024-01-15
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除用户角色关联
     *
     * @param roleId 角色ID
     * @return 删除数量
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入用户角色关联
     *
     * @param userRoles 用户角色关联列表
     * @return 插入数量
     */
    int batchInsert(@Param("userRoles") List<UserRole> userRoles);

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询用户ID列表
     *
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 检查用户是否拥有指定角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否拥有
     */
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
