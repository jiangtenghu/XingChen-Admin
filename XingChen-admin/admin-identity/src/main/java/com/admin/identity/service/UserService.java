package com.admin.identity.service;

import com.admin.identity.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * 用户数据服务接口
 * 专注于用户数据的CRUD操作，不包含认证逻辑
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-26
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据用户ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Long id);

    /**
     * 根据用户ID获取用户角色
     * 
     * @param id 用户ID
     * @return 角色列表
     */
    List<String> getUserRoles(Long id);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 更新用户最后登录信息
     * 
     * @param userId 用户ID
     * @param loginIp 登录IP
     */
    void updateLastLoginInfo(Long userId, String loginIp);
}
