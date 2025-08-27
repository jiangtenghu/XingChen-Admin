package com.admin.auth.mapper;

import com.admin.auth.domain.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 认证服务用户Mapper
 * 
 * @author admin
 * @since 2024-01-01
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);

    /**
     * 更新最后登录信息
     */
    @Update("UPDATE sys_user SET last_login_time = NOW(), login_count = login_count + 1 WHERE id = #{userId}")
    int updateLastLoginInfo(@Param("userId") Long userId);

    /**
     * 更新用户密码
     */
    @Update("UPDATE sys_user SET password = #{password}, update_time = NOW() WHERE id = #{userId}")
    int updatePassword(@Param("userId") Long userId, @Param("password") String password);

    /**
     * 更新用户状态
     */
    @Update("UPDATE sys_user SET status = #{status}, update_time = NOW() WHERE id = #{userId}")
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);
}
