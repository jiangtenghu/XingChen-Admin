package com.admin.common.util;

import com.admin.common.constant.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 用户上下文工具类 - 公共模块
 * 从网关传递的HTTP头中获取用户信息，供所有微服务使用
 *
 * @author admin
 * @since 2024-08-27
 */
@Slf4j
public class UserContextUtil {

    /**
     * 网关认证相关请求头常量
     */
    public static final String HEADER_CLIENT_IP = "X-Client-IP";
    public static final String HEADER_AUTH_SOURCE = "X-Auth-Source";
    public static final String HEADER_AUTH_TIME = "X-Auth-Time";
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_USER_TYPE = "X-User-Type";
    public static final String HEADER_ROLES = "X-User-Roles";

    /**
     * 认证来源常量
     */
    public static final String AUTH_SOURCE_GATEWAY = "Gateway";
    public static final String AUTH_SOURCE_INTERNAL = "Internal";

    /**
     * 从请求头获取用户ID
     *
     * @param request HTTP请求
     * @return 用户ID，如果不存在返回null
     */
    public static String getUserId(HttpServletRequest request) {
        String userId = request.getHeader(CommonConstants.USER_ID_HEADER);
        if (StringUtils.hasText(userId)) {
            log.debug("从请求头获取用户ID: {}", userId);
            return userId;
        }
        log.warn("请求头中未找到用户ID");
        return null;
    }

    /**
     * 从请求头获取用户ID（Long类型）
     *
     * @param request HTTP请求
     * @return 用户ID，如果不存在或格式错误返回null
     */
    public static Long getUserIdAsLong(HttpServletRequest request) {
        String userId = getUserId(request);
        if (StringUtils.hasText(userId)) {
            try {
                return Long.valueOf(userId);
            } catch (NumberFormatException e) {
                log.warn("用户ID格式错误: {}", userId);
            }
        }
        return null;
    }

    /**
     * 从请求头获取用户名
     *
     * @param request HTTP请求
     * @return 用户名，如果不存在返回null
     */
    public static String getUsername(HttpServletRequest request) {
        String username = request.getHeader(CommonConstants.USERNAME_HEADER);
        if (StringUtils.hasText(username)) {
            log.debug("从请求头获取用户名: {}", username);
            return username;
        }
        log.warn("请求头中未找到用户名");
        return null;
    }

    /**
     * 从请求头获取租户ID
     *
     * @param request HTTP请求
     * @return 租户ID，如果不存在返回null
     */
    public static String getTenantId(HttpServletRequest request) {
        return request.getHeader(HEADER_TENANT_ID);
    }

    /**
     * 从请求头获取用户类型
     *
     * @param request HTTP请求
     * @return 用户类型，如果不存在返回null
     */
    public static String getUserType(HttpServletRequest request) {
        return request.getHeader(HEADER_USER_TYPE);
    }

    /**
     * 从请求头获取用户角色
     *
     * @param request HTTP请求
     * @return 用户角色字符串，如果不存在返回null
     */
    public static String getUserRoles(HttpServletRequest request) {
        return request.getHeader(HEADER_ROLES);
    }

    /**
     * 从请求头获取客户端IP
     *
     * @param request HTTP请求
     * @return 客户端IP，如果不存在返回请求的远程地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader(HEADER_CLIENT_IP);
        if (StringUtils.hasText(clientIp)) {
            return clientIp;
        }
        // 如果网关没有传递，尝试直接获取
        return request.getRemoteAddr();
    }

    /**
     * 获取认证来源
     *
     * @param request HTTP请求
     * @return 认证来源
     */
    public static String getAuthSource(HttpServletRequest request) {
        return request.getHeader(HEADER_AUTH_SOURCE);
    }

    /**
     * 获取认证时间
     *
     * @param request HTTP请求
     * @return 认证时间字符串
     */
    public static String getAuthTime(HttpServletRequest request) {
        return request.getHeader(HEADER_AUTH_TIME);
    }

    /**
     * 检查请求是否来自网关认证
     *
     * @param request HTTP请求
     * @return true如果来自网关认证
     */
    public static boolean isFromGateway(HttpServletRequest request) {
        return AUTH_SOURCE_GATEWAY.equals(getAuthSource(request));
    }

    /**
     * 检查请求是否为内部调用
     *
     * @param request HTTP请求
     * @return true如果是内部调用
     */
    public static boolean isInternalCall(HttpServletRequest request) {
        return AUTH_SOURCE_INTERNAL.equals(getAuthSource(request));
    }

    /**
     * 验证用户上下文是否完整
     *
     * @param request HTTP请求
     * @return true如果用户上下文完整
     */
    public static boolean hasValidUserContext(HttpServletRequest request) {
        return StringUtils.hasText(getUserId(request)) && 
               StringUtils.hasText(getUsername(request));
    }

    /**
     * 验证是否为已认证的请求
     *
     * @param request HTTP请求
     * @return true如果请求已认证
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        return hasValidUserContext(request) && 
               (isFromGateway(request) || isInternalCall(request));
    }

    /**
     * 获取用户信息摘要（用于日志）
     *
     * @param request HTTP请求
     * @return 用户信息摘要
     */
    public static String getUserSummary(HttpServletRequest request) {
        String userId = getUserId(request);
        String username = getUsername(request);
        String clientIp = getClientIp(request);
        String authSource = getAuthSource(request);
        
        return String.format("User[id=%s, name=%s, ip=%s, source=%s]", 
                           userId != null ? userId : "unknown",
                           username != null ? username : "unknown", 
                           clientIp != null ? clientIp : "unknown",
                           authSource != null ? authSource : "unknown");
    }

    /**
     * 构建审计信息（用于操作日志）
     *
     * @param request HTTP请求
     * @param operation 操作类型
     * @return 审计信息
     */
    public static String buildAuditInfo(HttpServletRequest request, String operation) {
        return String.format("[%s] %s from %s at %s", 
                           operation,
                           getUserSummary(request),
                           getClientIp(request),
                           getAuthTime(request));
    }

    /**
     * 为当前请求设置用户上下文（用于内部调用）
     * 
     * @param request HTTP请求
     * @param userId 用户ID
     * @param username 用户名
     */
    public static void setInternalUserContext(HttpServletRequest request, String userId, String username) {
        // 注意：这个方法通常用于内部服务间调用，实际使用时需要实现Request的Header设置
        // 在Servlet环境中，请求头是只读的，这里提供接口定义
        log.debug("设置内部调用用户上下文: userId={}, username={}", userId, username);
    }
}
