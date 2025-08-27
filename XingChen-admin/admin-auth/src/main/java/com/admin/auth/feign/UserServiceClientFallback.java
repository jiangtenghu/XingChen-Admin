package com.admin.auth.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务Feign客户端降级处理
 * 
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public Map<String, Object> checkUsername(String username) {
        log.error("调用用户服务检查用户名失败，使用降级处理");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "用户服务不可用，请稍后重试");
        result.put("data", false);
        return result;
    }

    @Override
    public Map<String, Object> checkEmail(String email) {
        log.error("调用用户服务检查邮箱失败，使用降级处理");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "用户服务不可用，请稍后重试");
        result.put("data", false);
        return result;
    }

    @Override
    public Map<String, Object> createUser(Map<String, Object> userRequest) {
        log.error("调用用户服务创建用户失败，使用降级处理");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "用户服务不可用，请稍后重试");
        result.put("data", null);
        return result;
    }

    @Override
    public Map<String, Object> getUserByUsername(String username) {
        log.error("调用用户服务根据用户名获取用户失败，使用降级处理");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "用户服务不可用，请稍后重试");
        result.put("data", null);
        return result;
    }

    @Override
    public Map<String, Object> getUserById(Long id) {
        log.error("调用用户服务根据ID获取用户失败，使用降级处理");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "用户服务不可用，请稍后重试");
        result.put("data", null);
        return result;
    }

    @Override
    public Map<String, Object> getUserRoles(Long userId) {
        log.error("调用用户服务获取用户角色失败，使用降级处理");
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "用户服务不可用，请稍后重试");
        result.put("data", null);
        return result;
    }
}
