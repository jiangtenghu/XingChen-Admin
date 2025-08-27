package com.admin.common.web.exception;

import com.admin.common.core.domain.Result;
import com.admin.common.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 是否为开发环境，开发环境返回详细错误信息
     */
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleServiceException(ServiceException e) {
        logger.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        logger.error("参数校验异常: {}", message);
        return Result.badRequest(message);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        logger.error("参数绑定异常: {}", message);
        return Result.badRequest(message);
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getMessage();
        logger.error("参数校验异常: {}", message);
        return Result.badRequest(message);
    }

    /**
     * 资源未找到异常 (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleNoResourceFoundException(NoResourceFoundException e) {
        String message = "请求的资源不存在: " + e.getResourcePath();
        logger.warn("资源未找到: {}", message);
        
        if (isDevelopmentEnvironment()) {
            return Result.notFound(message + " - 异常类型: " + e.getClass().getSimpleName());
        }
        return Result.notFound("请求的资源不存在");
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常: {}", e.getMessage(), e);
        
        if (isDevelopmentEnvironment()) {
            // 开发环境返回详细错误信息
            return Result.error(500, "系统异常: " + e.getMessage() + " - 详细堆栈: " + getStackTrace(e));
        }
        // 生产环境返回通用错误信息
        return Result.error("系统繁忙，请稍后再试");
    }
    
    /**
     * 判断是否为开发环境
     */
    private boolean isDevelopmentEnvironment() {
        return "dev".equals(activeProfile) || "docker".equals(activeProfile) || "local".equals(activeProfile);
    }
    
    /**
     * 获取完整的异常堆栈信息
     */
    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
