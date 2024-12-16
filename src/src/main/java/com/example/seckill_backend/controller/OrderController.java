package com.example.seckill_backend.controller;

import com.example.seckill_backend.config.MultiRequestBody;
import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/test")
    public Result test(@RequestBody @Validated(Order.Create.class) Order order) {
        return Result.success(order);
    }

    @RequestMapping("/getOrder")
    public Result getOrder(@MultiRequestBody @Validated(User.SearchOrder.class) User user, @MultiRequestBody @Validated Page page) {
        return Result.success(orderService.getOrder(user, page));
    }
}
