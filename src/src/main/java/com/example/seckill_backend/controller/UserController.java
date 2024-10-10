package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        try {
            userService.register(user.getUsername(), user.getPassword());
            return "注册成功";
        } catch (Exception e) {
            return "注册失败：" + e.getMessage();
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        try {
            userService.login(user.getUsername(), user.getPassword());
            return "登录成功";
        } catch (Exception e) {
            return "登录失败：" + e.getMessage();
        }
    }
}
