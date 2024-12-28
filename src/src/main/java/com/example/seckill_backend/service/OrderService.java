package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.OrderMapper;
import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.util.PagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public Result getOrder(User user, Page<Object> page) {
        try {
            Integer total = orderMapper.getOrderTotal(user);
            if (total == null || total < 0) {
                total = 0;
            }
            page.setTotal(total);

            List<Order> orders = orderMapper.getOrder(user.getUser_id(), page.getPage_size(), PagerUtil.getOffset(page.getPage_num(), page.getPage_size()));
            page.setItems(orders);

            return Result.success(page);
        } catch (Exception e) {
            // 处理异常，例如记录日志或返回一个默认的 Page 对象
            log.error("获取订单失败", e);
            return Result.error("获取订单失败"+e);
        }
    }

}
