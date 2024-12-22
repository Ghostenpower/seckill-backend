package com.example.seckill_backend.controller;

import com.example.seckill_backend.util.MultiRequestBody;
import com.example.seckill_backend.model.FlashSale;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.FlashSaleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/flashSale")
public class FlashSaleController {
    @Autowired
    private FlashSaleService flashSaleService;

    @PostMapping("getFlashSale")
    public Result getFlashSale(@RequestBody FlashSale flashSale) {
        FlashSale flashSale1 = flashSaleService.getFlashSaleById(flashSale);
        return Result.success(flashSale1);
    }

    @PostMapping("/flashSale")
    public Result flashSale(@MultiRequestBody @Validated(FlashSale.Get.class) FlashSale flashSale, HttpServletRequest request) {
        //从token中获取用户信息
        User user = (User) request.getAttribute("user");
        return Result.success(flashSaleService.flashSale(flashSale, user));
    }

    @PostMapping("/initializeFlashSale")
    public Result initializeFlashSale(@RequestBody @Validated(FlashSale.Create.class) FlashSale flashSale) {
        return flashSaleService.initializeFlashSale(flashSale);
    }
}
