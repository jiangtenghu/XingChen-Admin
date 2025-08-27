package com.admin.common.core.domain;

import com.admin.common.constant.CommonConstants;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private Integer code;

    @Schema(description = "返回消息")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "时间戳")
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> success() {
        return new Result<>(CommonConstants.SUCCESS_CODE, "操作成功");
    }

    /**
     * 成功返回数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(CommonConstants.SUCCESS_CODE, "操作成功", data);
    }

    /**
     * 成功返回消息和数据
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(CommonConstants.SUCCESS_CODE, message, data);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> error() {
        return new Result<>(CommonConstants.ERROR_CODE, "操作失败");
    }

    /**
     * 失败返回消息
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(CommonConstants.ERROR_CODE, message);
    }

    /**
     * 失败返回状态码和消息
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }

    /**
     * 参数错误
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(CommonConstants.BAD_REQUEST_CODE, message);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(CommonConstants.UNAUTHORIZED_CODE, message);
    }

    /**
     * 禁止访问
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(CommonConstants.FORBIDDEN_CODE, message);
    }

    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(CommonConstants.NOT_FOUND_CODE, message);
    }

    // Getters and Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
}
