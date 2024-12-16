package com.example.seckill_backend.controller;

import com.example.seckill_backend.config.MultiRequestBody;
import com.example.seckill_backend.model.FlashSale;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.FlashSaleService;
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
        FlashSale flashSale1 = flashSaleService.getFlashSale(flashSale);
        return Result.success(flashSale1);
    }

    @PostMapping("/flashSale")
    public Result flashSale(@MultiRequestBody @Validated(FlashSale.Get.class) FlashSale flashSale, @MultiRequestBody User user) {
        System.out.println(user);
        System.out.println(flashSale);
        return Result.success();
    }
}
