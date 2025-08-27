package com.admin.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户服务Feign客户端
 * 
 * @author admin
 * @since 2024-08-27
 */
@FeignClient(name = "admin-identity", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/api/identity/check-username")
    Map<String, Object> checkUsername(@RequestParam("username") String username);

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/api/identity/check-email")
    Map<String, Object> checkEmail(@RequestParam("email") String email);

    /**
     * 创建用户
     */
    @PostMapping("/api/identity/users")
    Map<String, Object> createUser(@RequestBody Map<String, Object> userRequest);

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/api/identity/users/username/{username}")
    Map<String, Object> getUserByUsername(@PathVariable("username") String username);

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/api/identity/users/{id}")
    Map<String, Object> getUserById(@PathVariable("id") Long id);

    /**
     * 获取用户角色
     */
    @GetMapping("/api/identity/users/{userId}/roles")
    Map<String, Object> getUserRoles(@PathVariable("userId") Long userId);
}