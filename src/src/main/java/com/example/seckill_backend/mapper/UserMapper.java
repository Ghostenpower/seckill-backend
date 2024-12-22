package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User getUserById(@Param("id") Long id);

    User getUserByUsername(User user);

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(@Param("id") Long id);

    String getPassword_hash(User user);

    Boolean isAdmin(User user);
}