package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public String register(@RequestBody @Validated(User.Register.class) User user) {
        log.trace("register");
        try {
            userService.register(user.getUsername(), user.getPassword_hash());
            return "registration success";
        } catch (RuntimeException e) {
            log.error("register failed", e);
            return "registration failed:" + e.getMessage();
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        try {
            userService.login(user.getUsername(), user.getPassword_hash());
            return "login successful";
        } catch (Exception e) {
            return "Login failed:" + e.getMessage();
        }
    }
}
