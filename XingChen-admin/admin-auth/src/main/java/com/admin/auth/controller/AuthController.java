package com.admin.auth.controller;

import com.admin.common.core.domain.Result;
import com.admin.auth.domain.dto.UserRegisterDTO;
import com.admin.auth.domain.dto.UserRegisterResultDTO;
import com.admin.auth.domain.dto.UserLoginRequestDTO;
import com.admin.auth.domain.dto.UserLoginResponseDTO;
import com.admin.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理用户注册、登录、Token验证等认证相关接口
 *
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "用户认证", description = "用户注册、登录、Token验证等认证相关接口")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口，包含完整的数据验证和密码加密")
    public Result<UserRegisterResultDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            log.info("开始用户注册，用户名: {}, 邮箱: {}", registerDTO.getUsername(), registerDTO.getEmail());
            
            // 调用认证服务层注册方法
            UserRegisterResultDTO result = authService.register(registerDTO);
            
            log.info("用户注册成功，用户ID: {}, 用户名: {}", result.getUserId(), registerDTO.getUsername());
            return Result.success("注册成功", result);
            
        } catch (Exception e) {
            log.error("用户注册失败，用户名: {}, 错误信息: {}", registerDTO.getUsername(), e.getMessage(), e);
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户账号密码登录接口，验证用户身份并返回用户信息")
    public Result<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO loginRequest, 
                                             HttpServletRequest request) {
        try {
            log.info("用户登录请求，用户名: {}", loginRequest.getUsername());
            
            // 获取客户端IP地址
            String clientIp = getClientIpAddress(request);
            loginRequest.setLoginIp(clientIp);
            
            // 调用认证服务层登录方法
            UserLoginResponseDTO response = authService.login(loginRequest);
            
            log.info("用户登录成功，用户ID: {}, 用户名: {}", response.getUserId(), response.getUsername());
            return Result.success("登录成功", response);
            
        } catch (Exception e) {
            log.error("用户登录失败，用户名: {}, 错误信息: {}", loginRequest.getUsername(), e.getMessage(), e);
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * Token验证接口
     */
    @PostMapping("/validate-token")
    @Operation(summary = "验证Token", description = "验证JWT访问令牌是否有效")
    public Result<Map<String, Object>> validateToken(@Parameter(description = "访问令牌") @RequestParam String token) {
        try {
            Map<String, Object> result = authService.validateToken(token);
            
            if ((Boolean) result.get("valid")) {
                return Result.success("Token验证成功", result);
            } else {
                return Result.error("Token无效或已过期");
            }
        } catch (Exception e) {
            log.error("Token验证失败", e);
            return Result.error("Token验证失败：" + e.getMessage());
        }
    }

    /**
     * Token刷新接口
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新Token", description = "使用刷新令牌获取新的访问令牌")
    public Result<Map<String, Object>> refreshToken(@Parameter(description = "刷新令牌") @RequestParam String refreshToken) {
        try {
            Map<String, Object> result = authService.refreshToken(refreshToken);
            return Result.success("Token刷新成功", result);
        } catch (Exception e) {
            log.error("Token刷新失败", e);
            return Result.error("Token刷新失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息接口（需要Token）
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "通过Token获取当前登录用户信息")
    public Result<UserLoginResponseDTO> getCurrentUser(@Parameter(description = "访问令牌") @RequestParam String token) {
        try {
            UserLoginResponseDTO response = authService.getCurrentUser(token);
            return Result.success("获取用户信息成功", response);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    /**
     * 用户登出接口
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出，使Token失效")
    public Result<String> logout(@Parameter(description = "访问令牌") @RequestParam String token) {
        try {
            authService.logout(token);
            return Result.success("登出成功", "OK");
        } catch (Exception e) {
            log.error("用户登出失败", e);
            return Result.error("登出失败：" + e.getMessage());
        }
    }

    /**
     * 服务健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查认证服务是否正常运行")
    public Result<String> health() {
        return Result.success("认证服务运行正常", "OK");
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}