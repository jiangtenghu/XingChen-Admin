package com.admin.identity.controller;

import com.admin.common.core.domain.Result;

import com.admin.identity.domain.entity.User;
import com.admin.identity.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户数据管理控制器
 * 专注于用户数据的CRUD操作，不包含认证逻辑
 *
 * @author admin
 * @since 2024-01-15
 */
@Slf4j
@RestController
@RequestMapping("/api/identity")
@RequiredArgsConstructor
@Tag(name = "用户数据管理", description = "用户数据CRUD操作相关接口，纯数据操作")
public class UserController {

    private final UserService userService;

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username")
    @Operation(summary = "检查用户名", description = "检查用户名是否已存在，返回true表示已存在")
    public Result<Boolean> checkUsername(@Parameter(description = "用户名") @RequestParam String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            String message = exists ? "用户名已存在" : "用户名可用";
            return Result.success(message, exists);
        } catch (Exception e) {
            log.error("检查用户名失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email")
    @Operation(summary = "检查邮箱", description = "检查邮箱是否已存在，返回true表示已存在")
    public Result<Boolean> checkEmail(@Parameter(description = "邮箱") @RequestParam String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            String message = exists ? "邮箱已被使用" : "邮箱可用";
            return Result.success(message, exists);
        } catch (Exception e) {
            log.error("检查邮箱失败", e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/users/username/{username}")
    @Operation(summary = "根据用户名获取用户", description = "根据用户名获取用户详细信息")
    public Result<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return Result.error("用户不存在");
            }
            return Result.success("获取用户信息成功", user);
        } catch (Exception e) {
            log.error("根据用户名获取用户失败", e);
            return Result.error("获取用户失败：" + e.getMessage());
        }
    }

    /**
     * 根据用户ID获取用户角色
     */
    @GetMapping("/users/{userId}/roles")
    @Operation(summary = "获取用户角色", description = "获取指定用户的角色列表")
    public Result<Map<String, Object>> getUserRoles(@PathVariable Long userId) {
        try {
            java.util.List<String> roles = userService.getUserRoles(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("roles", roles);
            return Result.success("获取用户角色成功", result);
        } catch (Exception e) {
            log.error("获取用户角色失败", e);
            return Result.error("获取角色失败：" + e.getMessage());
        }
    }

    /**
     * 测试用户接口
     */
    @GetMapping("/test")
    @Operation(summary = "测试接口", description = "用于测试用户服务是否正常")
    public Result<Map<String, Object>> test() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "用户服务接口正常");
        data.put("timestamp", LocalDateTime.now());
        data.put("service", "admin-identity");
        return Result.success("接口调用成功", data);
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/users/page")
    @Operation(summary = "分页查询用户", description = "根据条件分页查询用户列表")
    public Result<IPage<User>> pageUsers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        // 构建查询条件
        queryWrapper.eq(User::getDelFlag, 0)
                    .like(username != null && !username.trim().isEmpty(), User::getUsername, username)
                    .eq(status != null && !status.trim().isEmpty(), User::getStatus, status)
                    .orderByDesc(User::getCreateTime);
        
        IPage<User> result = userService.page(page, queryWrapper);
        return Result.success("查询成功", result);
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "查询用户详情", description = "根据用户ID查询用户详细信息")
    public Result<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null || user.getDelFlag() == 1) {
            return Result.error("用户不存在");
        }
        return Result.success("查询成功", user);
    }

    /**
     * 创建用户
     */
    @PostMapping("/users")
    @Operation(summary = "创建用户", description = "创建新用户")
    public Result<Long> createUser(@RequestBody Map<String, Object> userRequest) {
        try {
            // 构建用户对象
            User user = new User();
            if (userRequest.get("tenantId") != null) {
                user.setTenantId(Long.valueOf(userRequest.get("tenantId").toString()));
            }
            user.setUsername((String) userRequest.get("username"));
            user.setPassword((String) userRequest.get("password"));
            user.setEmail((String) userRequest.get("email"));
            user.setPhone((String) userRequest.get("phone"));
            user.setRealName((String) userRequest.get("realName"));
            user.setNickname((String) userRequest.get("nickname"));
            user.setSex((String) userRequest.get("sex"));
            user.setUserType((String) userRequest.get("userType"));
            user.setStatus("0"); // 默认启用
            user.setDelFlag(0); // 未删除
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            boolean saved = userService.save(user);
            if (saved) {
                return Result.success("创建成功", user.getId());
            }
            return Result.error("创建失败");
        } catch (Exception e) {
            log.error("创建用户失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/users/{id}")
    @Operation(summary = "更新用户", description = "根据ID更新用户信息")
    public Result<User> updateUser(@Parameter(description = "用户ID") @PathVariable Long id, 
                                  @RequestBody User user) {
        try {
            user.setId(id);
            boolean updated = userService.updateById(user);
            if (updated) {
                return Result.success("更新成功", user);
            }
            return Result.error("更新失败");
        } catch (Exception e) {
            log.error("更新用户失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/users/{id}")
    @Operation(summary = "删除用户", description = "根据ID逻辑删除用户")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        try {
            User user = userService.getById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            user.setDelFlag(1);
            user.setUpdateTime(LocalDateTime.now());
            boolean deleted = userService.updateById(user);
            
            if (deleted) {
                return Result.success("删除成功", null);
            }
            return Result.error("删除失败");
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }


}
