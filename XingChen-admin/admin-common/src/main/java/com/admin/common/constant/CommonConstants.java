package com.admin.common.constant;

/**
 * 通用常量
 *
 * @author admin
 * @since 2024-08-26
 */
public class CommonConstants {

    /**
     * 编码格式
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * HTTP状态码 - 成功
     */
    public static final Integer SUCCESS_CODE = 200;

    /**
     * HTTP状态码 - 服务器错误
     */
    public static final Integer ERROR_CODE = 500;

    /**
     * HTTP状态码 - 未授权
     */
    public static final Integer UNAUTHORIZED_CODE = 401;

    /**
     * HTTP状态码 - 禁止访问
     */
    public static final Integer FORBIDDEN_CODE = 403;

    /**
     * HTTP状态码 - 资源未找到
     */
    public static final Integer NOT_FOUND_CODE = 404;

    /**
     * HTTP状态码 - 请求错误
     */
    public static final Integer BAD_REQUEST_CODE = 400;

    /**
     * Token请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户ID请求头
     */
    public static final String USER_ID_HEADER = "X-User-Id";

    /**
     * 用户名请求头
     */
    public static final String USERNAME_HEADER = "X-Username";

    /**
     * 客户端IP请求头
     */
    public static final String CLIENT_IP_HEADER = "X-Client-IP";

    /**
     * 认证来源请求头
     */
    public static final String AUTH_SOURCE_HEADER = "X-Auth-Source";

    /**
     * 认证时间请求头
     */
    public static final String AUTH_TIME_HEADER = "X-Auth-Time";

    /**
     * 租户ID请求头
     */
    public static final String TENANT_ID_HEADER = "X-Tenant-Id";

    /**
     * 用户类型请求头
     */
    public static final String USER_TYPE_HEADER = "X-User-Type";

    /**
     * 用户角色请求头
     */
    public static final String USER_ROLES_HEADER = "X-User-Roles";

    /**
     * 删除标志 - 正常
     */
    public static final Integer DEL_FLAG_NORMAL = 0;

    /**
     * 删除标志 - 删除
     */
    public static final Integer DEL_FLAG_DELETED = 1;

    /**
     * 状态 - 正常
     */
    public static final Integer STATUS_NORMAL = 0;

    /**
     * 状态 - 停用
     */
    public static final Integer STATUS_DISABLE = 1;

    /**
     * 菜单类型 - 目录
     */
    public static final String MENU_TYPE_DIR = "M";

    /**
     * 菜单类型 - 菜单
     */
    public static final String MENU_TYPE_MENU = "C";

    /**
     * 菜单类型 - 按钮
     */
    public static final String MENU_TYPE_BUTTON = "F";

    /**
     * 默认页码
     */
    public static final Long DEFAULT_PAGE_NUM = 1L;

    /**
     * 默认页面大小
     */
    public static final Long DEFAULT_PAGE_SIZE = 10L;

    /**
     * 最大页面大小
     */
    public static final Long MAX_PAGE_SIZE = 1000L;

    /**
     * 来自网关的认证
     */
    public static final String AUTH_SOURCE_GATEWAY = "Gateway";

    /**
     * 内部服务调用
     */
    public static final String AUTH_SOURCE_INTERNAL = "Internal";

    /**
     * 第三方认证
     */
    public static final String AUTH_SOURCE_EXTERNAL = "External";
}