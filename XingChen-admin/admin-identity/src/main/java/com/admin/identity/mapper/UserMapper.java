package com.admin.identity.mapper;

import com.admin.identity.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询角色
     */
    @Select("SELECT r.role_key FROM sys_user_role ur " +
            "LEFT JOIN sys_role r ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId} AND ur.status = '0' AND r.status = '0'")
    List<String> selectRolesByUserId(Long userId);
}
