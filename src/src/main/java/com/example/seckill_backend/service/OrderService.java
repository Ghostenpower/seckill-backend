package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.OrderMapper;
import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public Page getOrder(User user, Page page) {
        try {
            Integer total = orderMapper.getOrderTotal(user);
            if (total == null || total < 0) {
                total = 0;
            }
            page.setTotal(total);

            List<Order> orders = orderMapper.getOrder(user.getUser_id(),page.getPage_size(),page.getPage_size()*(page.getPage_num()-1));
            if (orders == null) {
                orders = new ArrayList<>();
            }
            page.setItems(orders);

            return page;
        } catch (Exception e) {
            // 处理异常，例如记录日志或返回一个默认的 Page 对象
            e.printStackTrace();
            return new Page<>();
        }
    }

}
