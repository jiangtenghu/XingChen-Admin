package com.admin.system.controller;

import com.admin.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 */
@Tag(name = "测试接口", description = "用于测试服务是否正常运行")
@RestController
@RequestMapping("/api/system")
public class TestController {

    @Operation(summary = "服务健康测试", description = "测试系统服务是否正常运行")
    @GetMapping("/health")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("service", "admin-system");
        data.put("status", "running");
        data.put("timestamp", LocalDateTime.now());
        data.put("message", "系统服务运行正常");
        
        return Result.success("服务正常", data);
    }

    @Operation(summary = "数据库连接测试", description = "测试数据库连接是否正常")
    @GetMapping("/db")
    public Result<String> testDatabase() {
        return Result.success("数据库连接正常");
    }
}
