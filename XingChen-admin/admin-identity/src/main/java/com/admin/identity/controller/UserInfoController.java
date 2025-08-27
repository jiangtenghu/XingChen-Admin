package com.admin.identity.controller;

import com.admin.common.core.domain.Result;
import com.admin.common.util.UserContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息控制器 - 演示如何使用公共工具类获取用户上下文
 *
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
@RestController
@RequestMapping("/api/identity/user-info")
@Tag(name = "用户信息", description = "用户上下文信息获取")
public class UserInfoController {

    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "从网关传递的请求头中获取用户信息")
    public Result<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        log.info("获取当前用户信息请求: {}", UserContextUtil.getUserSummary(request));

        // 验证是否为已认证的请求
        if (!UserContextUtil.isAuthenticated(request)) {
            log.warn("未认证的请求: {}", request.getRequestURI());
            return Result.error(401, "未认证的请求");
        }

        // 从请求头获取用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", UserContextUtil.getUserId(request));
        userInfo.put("username", UserContextUtil.getUsername(request));
        userInfo.put("tenantId", UserContextUtil.getTenantId(request));
        userInfo.put("userType", UserContextUtil.getUserType(request));
        userInfo.put("roles", UserContextUtil.getUserRoles(request));
        userInfo.put("clientIp", UserContextUtil.getClientIp(request));
        userInfo.put("authSource", UserContextUtil.getAuthSource(request));
        userInfo.put("authTime", UserContextUtil.getAuthTime(request));
        userInfo.put("isFromGateway", UserContextUtil.isFromGateway(request));

        log.info("用户信息获取成功: {}", UserContextUtil.buildAuditInfo(request, "GET_USER_INFO"));
        return Result.success("获取用户信息成功", userInfo);
    }

    @GetMapping("/context")
    @Operation(summary = "获取用户上下文摘要", description = "获取用户上下文的简要信息")
    public Result<Map<String, Object>> getUserContext(HttpServletRequest request) {
        Map<String, Object> context = new HashMap<>();
        
        context.put("userSummary", UserContextUtil.getUserSummary(request));
        context.put("hasValidContext", UserContextUtil.hasValidUserContext(request));
        context.put("isAuthenticated", UserContextUtil.isAuthenticated(request));
        context.put("isFromGateway", UserContextUtil.isFromGateway(request));
        context.put("isInternalCall", UserContextUtil.isInternalCall(request));
        
        return Result.success("获取用户上下文成功", context);
    }

    @GetMapping("/audit-demo")
    @Operation(summary = "审计日志演示", description = "演示如何记录操作审计日志")
    public Result<String> auditDemo(HttpServletRequest request) {
        String operation = "DEMO_OPERATION";
        String auditInfo = UserContextUtil.buildAuditInfo(request, operation);
        
        log.info("操作审计: {}", auditInfo);
        
        return Result.success("审计日志记录成功", auditInfo);
    }
}
