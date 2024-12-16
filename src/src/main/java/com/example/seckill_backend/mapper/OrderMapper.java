package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    public List<Order> getOrder(User user);
}
