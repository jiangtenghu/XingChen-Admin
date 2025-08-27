package com.admin.auth.service.impl;

import com.admin.auth.domain.dto.UserRegisterDTO;
import com.admin.auth.domain.dto.UserRegisterResultDTO;
import com.admin.auth.domain.dto.UserLoginRequestDTO;
import com.admin.auth.domain.dto.UserLoginResponseDTO;
import com.admin.auth.service.AuthService;
import com.admin.auth.feign.UserServiceClient;
import com.admin.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 * 
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration:86400}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-expiration:604800}")
    private Long refreshTokenExpiration;

    @Override
    public UserRegisterResultDTO register(UserRegisterDTO registerDTO) {
        // 1. 验证密码匹配
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 2. 检查用户名是否已存在
        Map<String, Object> checkResult = userServiceClient.checkUsername(registerDTO.getUsername());
        if ((Boolean) checkResult.get("data")) {
            throw new RuntimeException("用户名已存在");
        }

        // 3. 检查邮箱是否已存在
        Map<String, Object> emailCheckResult = userServiceClient.checkEmail(registerDTO.getEmail());
        if ((Boolean) emailCheckResult.get("data")) {
            throw new RuntimeException("邮箱已被使用");
        }

        // 4. 加密密码
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());

        // 5. 构建用户创建请求
        Map<String, Object> createUserRequest = new HashMap<>();
        createUserRequest.put("tenantId", registerDTO.getTenantId());
        createUserRequest.put("username", registerDTO.getUsername());
        createUserRequest.put("password", encodedPassword);
        createUserRequest.put("email", registerDTO.getEmail());
        createUserRequest.put("phone", registerDTO.getPhone());
        createUserRequest.put("realName", registerDTO.getRealName());
        createUserRequest.put("nickname", registerDTO.getNickname());
        createUserRequest.put("sex", registerDTO.getSex());
        createUserRequest.put("userType", registerDTO.getUserType());

        // 6. 调用用户服务创建用户
        Map<String, Object> createResult = userServiceClient.createUser(createUserRequest);
        Long userId = Long.valueOf(createResult.get("data").toString());

        // 7. 构建注册结果
        UserRegisterResultDTO result = new UserRegisterResultDTO();
        result.setUserId(userId);
        result.setUsername(registerDTO.getUsername());
        result.setEmail(registerDTO.getEmail());
        result.setNeedEmailVerification(false);
        result.setNeedPhoneVerification(false);
        result.setMessage("注册成功，欢迎使用XingChen管理系统！");
        result.setRegisterTime(LocalDateTime.now());
        result.setAccountStatus("ACTIVE");
        result.setNextStepTip("请使用用户名和密码登录系统");

        return result;
    }

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO loginRequest) {
        // 1. 验证用户名和密码
        Map<String, Object> userResult = userServiceClient.getUserByUsername(loginRequest.getUsername());
        if (userResult.get("data") == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> userData = (Map<String, Object>) userResult.get("data");

        // 2. 验证密码
        String storedPassword = (String) userData.get("password");
        if (!passwordEncoder.matches(loginRequest.getPassword(), storedPassword)) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 3. 检查用户状态
        Integer status = (Integer) userData.get("status");
        if (status != null && status != 0) {
            throw new RuntimeException("账户已被禁用");
        }

        // 4. 生成Token
        Long userId = Long.valueOf(userData.get("id").toString());
        String username = (String) userData.get("username");
        
        String accessToken = generateAccessToken(userData);
        String refreshToken = generateRefreshToken(userId, username);

        // 5. 缓存用户登录信息
        cacheUserLoginInfo(userId, accessToken, loginRequest.getLoginIp());

        // 6. 构建登录响应
        UserLoginResponseDTO response = new UserLoginResponseDTO();
        response.setUserId(userId);
        response.setUsername(username);
        response.setNickname((String) userData.get("nickname"));
        response.setRealName((String) userData.get("realName"));
        response.setEmail((String) userData.get("email"));
        response.setPhone((String) userData.get("phone"));
        response.setSex((String) userData.get("sex"));
        response.setUserType((String) userData.get("userType"));
        response.setTenantId(Long.valueOf(userData.get("tenantId").toString()));
        response.setLoginTime(LocalDateTime.now());
        response.setLoginIp(loginRequest.getLoginIp());
        response.setMessage("登录成功，欢迎回来！");
        response.setAccountStatus("ACTIVE");
        response.setLoginSuccess(true);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setTokenType("Bearer");
        response.setExpiresIn(accessTokenExpiration);

        // 获取用户角色
        try {
            Map<String, Object> rolesResult = userServiceClient.getUserRoles(userId);
            @SuppressWarnings("unchecked")
            Map<String, Object> rolesData = (Map<String, Object>) rolesResult.get("data");
            if (rolesData != null) {
                @SuppressWarnings("unchecked")
                java.util.List<String> roles = (java.util.List<String>) rolesData.get("roles");
                response.setRoles(roles != null ? roles : java.util.Arrays.asList("USER"));
            } else {
                response.setRoles(java.util.Arrays.asList("USER"));
            }
        } catch (Exception e) {
            log.warn("获取用户角色失败: {}", e.getMessage());
            response.setRoles(java.util.Arrays.asList("USER"));
        }

        return response;
    }

    @Override
    public Map<String, Object> validateToken(String token) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean isValid = JwtUtil.validateToken(token);
            result.put("valid", isValid);
            
            if (isValid) {
                String userId = JwtUtil.getUserIdFromToken(token);
                String username = JwtUtil.getUsernameFromToken(token);
                result.put("userId", userId);
                result.put("username", username);
            }
        } catch (Exception e) {
            log.error("Token验证失败", e);
            result.put("valid", false);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> refreshToken(String refreshToken) {
        try {
            // 验证刷新令牌
            if (!JwtUtil.validateToken(refreshToken)) {
                throw new RuntimeException("刷新令牌无效或已过期");
            }

            String userId = JwtUtil.getUserIdFromToken(refreshToken);
            String username = JwtUtil.getUsernameFromToken(refreshToken);

            // 获取用户信息生成新的访问令牌
            Map<String, Object> userResult = userServiceClient.getUserByUsername(username);
            if (userResult.get("data") == null) {
                throw new RuntimeException("用户不存在");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> userData = (Map<String, Object>) userResult.get("data");
            String newAccessToken = generateAccessToken(userData);

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", newAccessToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", accessTokenExpiration);
            result.put("message", "Token刷新成功");

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Token刷新失败：" + e.getMessage());
        }
    }

    @Override
    public UserLoginResponseDTO getCurrentUser(String token) {
        // 验证Token
        if (!JwtUtil.validateToken(token)) {
            throw new RuntimeException("Token无效或已过期");
        }

        // 获取用户信息
        String userId = JwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new RuntimeException("无法从Token中获取用户信息");
        }

        Map<String, Object> userResult = userServiceClient.getUserById(Long.valueOf(userId));
        if (userResult.get("data") == null) {
            throw new RuntimeException("用户不存在");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> userData = (Map<String, Object>) userResult.get("data");

        // 构建响应
        UserLoginResponseDTO response = new UserLoginResponseDTO();
        response.setUserId(Long.valueOf(userData.get("id").toString()));
        response.setUsername((String) userData.get("username"));
        response.setNickname((String) userData.get("nickname"));
        response.setRealName((String) userData.get("realName"));
        response.setEmail((String) userData.get("email"));
        response.setPhone((String) userData.get("phone"));
        response.setSex((String) userData.get("sex"));
        response.setUserType((String) userData.get("userType"));
        response.setTenantId(Long.valueOf(userData.get("tenantId").toString()));
        response.setAccountStatus("ACTIVE");

        // 获取用户角色
        try {
            Map<String, Object> rolesResult = userServiceClient.getUserRoles(Long.valueOf(userId));
            @SuppressWarnings("unchecked")
            Map<String, Object> rolesData = (Map<String, Object>) rolesResult.get("data");
            if (rolesData != null) {
                @SuppressWarnings("unchecked")
                java.util.List<String> roles = (java.util.List<String>) rolesData.get("roles");
                response.setRoles(roles != null ? roles : java.util.Arrays.asList("USER"));
            } else {
                response.setRoles(java.util.Arrays.asList("USER"));
            }
        } catch (Exception e) {
            log.warn("获取用户角色失败: {}", e.getMessage());
            response.setRoles(java.util.Arrays.asList("USER"));
        }

        return response;
    }

    @Override
    public void logout(String token) {
        try {
            // 将Token加入黑名单
            String blacklistKey = "auth:blacklist:" + token;
            redisTemplate.opsForValue().set(blacklistKey, "1", accessTokenExpiration, TimeUnit.SECONDS);
            
            // 清除用户登录缓存
            String userId = JwtUtil.getUserIdFromToken(token);
            if (userId != null) {
                String loginKey = "auth:login:" + userId;
                redisTemplate.delete(loginKey);
            }
        } catch (Exception e) {
            log.error("登出失败", e);
            throw new RuntimeException("登出失败：" + e.getMessage());
        }
    }

    /**
     * 生成访问令牌
     */
    private String generateAccessToken(Map<String, Object> userData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userData.get("id"));
        claims.put("username", userData.get("username"));
        claims.put("tenantId", userData.get("tenantId"));
        claims.put("userType", userData.get("userType"));
        claims.put("email", userData.get("email"));
        claims.put("tokenType", "access");

        return JwtUtil.generateToken(userData.get("username").toString(), userData.get("id").toString());
    }

    /**
     * 生成刷新令牌
     */
    private String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("tokenType", "refresh");

        return JwtUtil.generateToken(username, userId.toString());
    }

    /**
     * 缓存用户登录信息
     */
    private void cacheUserLoginInfo(Long userId, String token, String loginIp) {
        try {
            String loginKey = "auth:login:" + userId;
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("token", token);
            loginInfo.put("loginTime", LocalDateTime.now().toString());
            loginInfo.put("loginIp", loginIp);
            
            redisTemplate.opsForValue().set(loginKey, loginInfo, accessTokenExpiration, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("缓存用户登录信息失败: {}", e.getMessage());
        }
    }
}