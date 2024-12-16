package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User getUserById(@Param("id") Long id);

    User getUserByUsername(User user);

    int insertUser(User user);

    int updateUser(User user);

    int deleteUser(@Param("id") Long id);

    String getPassword_hash(User user);
}