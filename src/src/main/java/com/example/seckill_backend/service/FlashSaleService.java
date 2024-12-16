package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.FlashSaleMapper;
import com.example.seckill_backend.model.FlashSale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleService {
    @Autowired
    private FlashSaleMapper flashSaleMapper;


    public FlashSale getFlashSale(FlashSale flashSale) {
        return flashSaleMapper.getFlashSale(flashSale);
    }
}
