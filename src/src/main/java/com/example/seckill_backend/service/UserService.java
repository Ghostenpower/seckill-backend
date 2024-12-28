package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.util.JWT;
import com.example.seckill_backend.util.PagerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserMapper userMapper;

    /**
     * 用户注册
     */
    public Result register(User user) {
        // 检查用户名是否已存在
        User existingUser = userMapper.getUserByUsername(user);
        if (existingUser != null) {
            System.out.println(existingUser);
            System.out.println("User already exists");
            return Result.error("User already exists");
        }
        // 对密码进行加密（MD5 方式）
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());

        // 创建新用户
        User user1 = new User();
        user.setUsername(user.getUsername());
        user.setPassword_hash(encryptedPassword);
        Map<String, Object> map=new HashMap<>();
        userMapper.insertUser(user);
        return Result.success();
    }

    /**
     * 用户登录
     */
    public Result login(User user) {
        User user1 = userMapper.getUserByUsername(user);
        if (user1 == null) {
            return Result.error("wrong username");
        }

        // 对输入的密码进行加密后比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        if (!encryptedPassword.equals(userMapper.getPassword_hash(user))) {
            return Result.error("wrong password");
        }

        Map<String, Object> map=new HashMap<>();
        map.put("user_id",user1.getUser_id());
        map.put("username",user1.getUsername());
        map.put("password_hash",user1.getPassword_hash());

        String token= JWT.CreateJwt(map);
        map.put("token",token);
        map.remove("password_hash");
        return Result.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result getUserInfo(User user) {
        return Result.success(userMapper.getUserInfo(user));
    }

    public Result getUserList(User user, Page<Object> page,User admin) {
        Boolean is_admin=userMapper.isAdmin(admin);
        if(!is_admin){
            return Result.error("no permission");
        }

        page.setTotal(userMapper.getUserListCount(user));
        List<User> userList = userMapper.getUserList(user, page.getPage_size(), PagerUtil.getOffset(page.getPage_num(), page.getPage_size()));
        page.setItems(userList);
        return Result.success(page);
    }

    public Result createUser(User user) {
        userMapper.insertUser(user);
        return Result.success();
    }

    public Result editUser(User admin, User user) {
        Boolean is_admin=userMapper.isAdmin(admin);
        if(!is_admin){
            return Result.error("no permission");
        }

        String encryptedPassword=null;
        if(user.getPassword()!=null && !user.getPassword().equals("")){
            encryptedPassword= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        }
        user.setPassword_hash(encryptedPassword);

        userMapper.updateUser(user);
        return Result.success();
    }

    public Result adminLogin(User admin) {
        User user1 = userMapper.getUserByUsername(admin);
        if (user1 == null) {
            return Result.error("wrong username");
        }
        if(user1.getIs_admin()==0){
            return Result.error("no permission");
        }
        // 对输入的密码进行加密后比对
        String encryptedPassword = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes());
        if (!encryptedPassword.equals(userMapper.getPassword_hash(admin))) {
            return Result.error("wrong password");
        }

        Map<String, Object> map=new HashMap<>();
        map.put("user_id",user1.getUser_id());
        map.put("username",user1.getUsername());
        map.put("password_hash",user1.getPassword_hash());

        String token= JWT.CreateJwt(map);
        map.put("token",token);
        map.remove("password_hash");
        return Result.success(map);
    }

    public Result deleteUser(User admin,User user) {
        Boolean is_admin=userMapper.isAdmin(admin);
        if(!is_admin){
            return Result.error("no permission");
        }
        Boolean user_is_admin=userMapper.isAdmin(user);
        if(user_is_admin){
            return Result.error("no permission");
        }
        userMapper.deleteUser(user.getUser_id());
        return Result.success();
    }
}