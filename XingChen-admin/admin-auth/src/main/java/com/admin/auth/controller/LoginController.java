package com.admin.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OAuth登录控制器
 * 
 * @author admin
 * @since 2024-08-27
 */
@Controller
public class LoginController {

    /**
     * 显示登录页面
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            Model model) {
        
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        
        model.addAttribute("title", "XingChen Admin - 用户登录");
        return "login";
    }

    /**
     * OAuth授权页面
     */
    @GetMapping("/oauth2/consent")
    public String consent(Model model) {
        model.addAttribute("title", "XingChen Admin - 授权确认");
        return "consent";
    }
}
