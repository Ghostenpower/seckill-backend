package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.UserService;
import com.example.seckill_backend.util.MultiRequestBody;
import jakarta.servlet.http.HttpServletRequest;
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
        return userService.register(user);
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/verifyLogin")
    public Result verifyLogin() {
        return Result.success();
    }

    @PostMapping("/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return userService.getUserInfo(user);
    }

    @PostMapping("/getUserList")
    public Result getUserList(@MultiRequestBody Page<Object> page,@MultiRequestBody User user , HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        return userService.getUserList(user,page,admin);
    }

    @PostMapping("/createUser")
    public Result createUser(@RequestBody @Validated(User.Register.class) User user) {
        return userService.createUser(user);
    }

    @PostMapping("/editUser")
    public Result editUser(@RequestBody @Validated(User.SearchOrder.class) User user,HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        return userService.editUser(admin,user);
    }

    @PostMapping("adminLogin")
    public Result adminLogin(@RequestBody @Validated(User.Login.class) User admin) {
        return userService.adminLogin(admin);
    }

    @PostMapping("/deleteUserById")
    public Result deleteUserBuId(@RequestBody @Validated(User.SearchOrder.class) User user,HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        return userService.deleteUser(admin,user);
    }
}
