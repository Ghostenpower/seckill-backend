package com.example.seckill_backend.controller;

import com.example.seckill_backend.util.MultiRequestBody;
import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.OrderService;
import com.example.seckill_backend.util.rateLimiter.Limit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/test")
    @Limit(key = "testAPI", permitsPerSecond = 1, timeout = 3, timeunit = TimeUnit.SECONDS, msg = "请求太频繁，请稍后再试！")
    public Result test(@RequestBody Order order) {
        return Result.success(order);
    }

    @RequestMapping("/getOrder")
    public Result getOrder(@MultiRequestBody @Validated(User.SearchOrder.class) User user, @MultiRequestBody @Validated Page page) {
        return Result.success(orderService.getOrder(user, page));
    }
}
