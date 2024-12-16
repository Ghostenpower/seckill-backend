package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.OrderMapper;
import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    public List<Order> getOrder(User user) {
        return orderMapper.getOrder(user);
    }
}
