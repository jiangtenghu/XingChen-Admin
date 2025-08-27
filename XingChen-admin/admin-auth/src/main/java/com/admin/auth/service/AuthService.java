package com.admin.auth.service;

import com.admin.auth.domain.dto.UserRegisterDTO;
import com.admin.auth.domain.dto.UserRegisterResultDTO;
import com.admin.auth.domain.dto.UserLoginRequestDTO;
import com.admin.auth.domain.dto.UserLoginResponseDTO;

import java.util.Map;

/**
 * 认证服务接口
 * 
 * @author admin
 * @since 2024-08-27
 */
public interface AuthService {

    /**
     * 用户注册
     * 
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    UserRegisterResultDTO register(UserRegisterDTO registerDTO);

    /**
     * 用户登录
     * 
     * @param loginRequest 登录信息
     * @return 登录结果
     */
    UserLoginResponseDTO login(UserLoginRequestDTO loginRequest);

    /**
     * 验证Token
     * 
     * @param token 访问令牌
     * @return 验证结果
     */
    Map<String, Object> validateToken(String token);

    /**
     * 刷新Token
     * 
     * @param refreshToken 刷新令牌
     * @return 新的令牌信息
     */
    Map<String, Object> refreshToken(String refreshToken);

    /**
     * 获取当前用户信息
     * 
     * @param token 访问令牌
     * @return 用户信息
     */
    UserLoginResponseDTO getCurrentUser(String token);

    /**
     * 用户登出
     * 
     * @param token 访问令牌
     */
    void logout(String token);
}