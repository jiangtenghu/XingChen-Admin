package com.admin.identity.service.impl;

import com.admin.identity.domain.entity.User;
import com.admin.identity.mapper.UserMapper;
import com.admin.identity.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 用户数据服务实现
 * 专注于用户数据的CRUD操作，不包含认证逻辑
 * 
 * @author admin
 * @version 1.0
 * @since 2024-08-26
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                   .eq(User::getDelFlag, 0);
        return this.getOne(queryWrapper);
    }

    @Override
    public User getUserById(Long id) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id)
                   .eq(User::getDelFlag, 0);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<String> getUserRoles(Long id) {
        try {
            List<String> roles = baseMapper.selectRolesByUserId(id);
            return roles != null && !roles.isEmpty() ? roles : Arrays.asList("USER");
        } catch (Exception e) {
            log.warn("获取用户角色失败，返回默认角色: {}", e.getMessage());
            return Arrays.asList("USER");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username)
                   .eq(User::getDelFlag, 0);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email)
                   .eq(User::getDelFlag, 0);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public void updateLastLoginInfo(Long userId, String loginIp) {
        try {
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getId, userId)
                        .set(User::getLoginDate, LocalDateTime.now())
                        .set(User::getLoginIp, loginIp)
                        .set(User::getUpdateTime, LocalDateTime.now());
            
            this.update(updateWrapper);
            log.info("用户登录信息更新成功，用户ID: {}, 登录IP: {}", userId, loginIp);
        } catch (Exception e) {
            log.error("更新用户登录信息失败，用户ID: {}", userId, e);
        }
    }
}