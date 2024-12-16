package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.util.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserMapper userMapper;

    /**
     * 用户注册
     */
    public void register(User user) {
        // 检查用户名是否已存在
        User existingUser = userMapper.getUserByUsername(user);
        if (existingUser != null) {
            System.out.println(existingUser);
            System.out.println("User already exists");
            throw new RuntimeException("User already exists");
        }
        // 对密码进行加密（MD5 方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());

        // 创建新用户
        User user1 = new User();
        user.setUsername(user.getUsername());
        user.setPassword_hash(encryptedPassword);

        userMapper.insertUser(user);
    }

    /**
     * 用户登录
     */
    public Map<String, Object> login(User user) {
        User user1 = userMapper.getUserByUsername(user);
        if (user1 == null) {
            throw new RuntimeException("User does not exist");
        }

        // 对输入的密码进行加密后比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        if (!encryptedPassword.equals(userMapper.getPassword_hash(user))) {
            throw new RuntimeException("wrong password");
        }

        Map<String, Object> map=new HashMap<>();
        map.put("user_id",user1.getUser_id());
        map.put("username",user1.getUsername());
        map.put("password_hash",user1.getPassword_hash());

        String token= JWT.CreateJwt(map);
        map.put("token",token);
        map.remove("password_hash");
        return map;
    }
}