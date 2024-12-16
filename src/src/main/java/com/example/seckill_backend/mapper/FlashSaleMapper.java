package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.FlashSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FlashSaleMapper {

    public FlashSale getFlashSale(FlashSale flashSale);
}
