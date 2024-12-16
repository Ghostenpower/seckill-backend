package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserMapper userMapper;

    /**
     * 用户注册
     */
    public void register(String username, String password) {
        // 检查用户名是否已存在
        User existingUser = userMapper.getUserByUsername(username);
        if (existingUser != null) {
            System.out.println(existingUser);
            System.out.println("User already exists");
            throw new RuntimeException("User already exists");
        }
        // 对密码进行加密（MD5 方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword_hash(encryptedPassword);

        userMapper.insertUser(user);
    }

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User does not exist");
        }

        // 对输入的密码进行加密后比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword_hash())) {
            throw new RuntimeException("wrong password");
        }

        return user;
    }
}