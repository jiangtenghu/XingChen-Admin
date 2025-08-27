package com.admin.identity.mapper;

import com.admin.identity.domain.entity.UserOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户组织关联Mapper接口
 *
 * @author admin
 * @since 2024-01-15
 */
@Mapper
public interface UserOrganizationMapper extends BaseMapper<UserOrganization> {

    /**
     * 根据用户ID删除用户组织关联
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据组织ID删除用户组织关联
     *
     * @param organizationId 组织ID
     * @return 删除数量
     */
    int deleteByOrganizationId(@Param("organizationId") Long organizationId);

    /**
     * 批量插入用户组织关联
     *
     * @param userOrganizations 用户组织关联列表
     * @return 插入数量
     */
    int batchInsert(@Param("userOrganizations") List<UserOrganization> userOrganizations);

    /**
     * 根据用户ID查询组织ID列表
     *
     * @param userId 用户ID
     * @return 组织ID列表
     */
    List<Long> selectOrganizationIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据组织ID查询用户ID列表
     *
     * @param organizationId 组织ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByOrganizationId(@Param("organizationId") Long organizationId);

    /**
     * 检查用户是否属于指定组织
     *
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 是否属于
     */
    boolean existsByUserIdAndOrganizationId(@Param("userId") Long userId, @Param("organizationId") Long organizationId);

    /**
     * 根据用户ID查询主要组织
     *
     * @param userId 用户ID
     * @return 主要组织关联
     */
    UserOrganization selectPrimaryByUserId(@Param("userId") Long userId);

    /**
     * 设置用户主要组织
     *
     * @param userId 用户ID
     * @param organizationId 组织ID
     * @return 更新数量
     */
    int updatePrimaryOrganization(@Param("userId") Long userId, @Param("organizationId") Long organizationId);
}
