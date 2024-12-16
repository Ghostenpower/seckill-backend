package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public Result register(@RequestBody @Validated(User.Register.class) User user) {
        log.trace("register");
        try {
            userService.register(user);
            return Result.success("registration success");
        } catch (RuntimeException e) {
            log.error("register failed", e);
            return Result.error("registration failed:" + e.getMessage());
        }
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        Map<String, Object> tokenMap = userService.login(user);
        return Result.success(tokenMap);
    }
}
