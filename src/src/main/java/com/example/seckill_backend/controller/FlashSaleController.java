package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.*;
import com.example.seckill_backend.util.MultiRequestBody;
import com.example.seckill_backend.service.FlashSaleService;
import com.example.seckill_backend.util.rateLimiter.Limit;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/flashSale")
public class FlashSaleController {
    @Autowired
    private FlashSaleService flashSaleService;

    @PostMapping("getFlashSaleById")
    public Result getFlashSaleById(@RequestBody FlashSale flashSale) {
        FlashSale flashSale1 = flashSaleService.getFlashSaleById(flashSale);
        return Result.success(flashSale1);
    }

    @PostMapping("/getFlashSaleLimit3")
    public Result getFlashSaleLimit3() {
        return flashSaleService.getFlashSaleLimit3();
    }

    @PostMapping("/flashSale")
    @Limit(key = "flashSaleAPI", permitsPerSecond = 1, timeout = 3, timeunit = TimeUnit.SECONDS, msg = "请求太频繁，请稍后再试！")
    public Result flashSale(@MultiRequestBody @Validated(FlashSale.Get.class) FlashSale flashSale, HttpServletRequest request) {
        //从token中获取用户信息
        User user = (User) request.getAttribute("user");
        return flashSaleService.flashSale1(flashSale, user);
    }

    @PostMapping("/luaTest")
    public Result luaTest(@MultiRequestBody String key) {
        System.out.println(key);
        //从token中获取用户信息
        return flashSaleService.luaTest(key);
    }

    @PostMapping("/initializeFlashSale")
    @Limit(key = "initializeFlashSaleAPI", permitsPerSecond = 1, timeout = 3, timeunit = TimeUnit.SECONDS, msg = "请求太频繁，请稍后再试！")
    public Result initializeFlashSale(@RequestBody @Validated(FlashSale.Create.class) FlashSale flashSale,HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        return flashSaleService.initializeFlashSale(flashSale,admin);
    }

    @PostMapping("/getFlashSaleProductLimit3")
    public Result getFlashSaleProductLimit3(){
        return flashSaleService.getFlashSaleProductLimit3();
    }

    @PostMapping("/getByName")
    public Result getByName(@MultiRequestBody @Validated Page<Object> page, @MultiRequestBody @Validated(FlashSaleView.SearchByName.class) FlashSaleView flashSaleView) {
        return flashSaleService.getByName(page, flashSaleView);
    }

    @PostMapping("/getFlashSale")
    public Result getFlashSaleView(@MultiRequestBody FlashSaleView flashSaleView,@MultiRequestBody @Validated Page<Object> page) {
        return flashSaleService.getFlashSaleView(flashSaleView, page);
    }

    @PostMapping("/deleteFlashSale")
    public Result deleteFlashSale(@RequestBody @Validated(FlashSale.Get.class) FlashSale flashSale,HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        return flashSaleService.deleteFlashSale(flashSale,admin);
    }

}
