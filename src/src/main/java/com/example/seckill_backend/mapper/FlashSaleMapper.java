package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.FlashSale;
import com.example.seckill_backend.model.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlashSaleMapper {

    FlashSale getFlashSaleById(FlashSale flashSale);

    void initializeFlashSale(FlashSale flashSale);

    void updateFlashSale(FlashSale flashSale);

    List<FlashSale> getFlashSale(@Param("flashSale")FlashSale flashSale,@Param("page_size") Integer page_size,@Param("offset") Integer offset);
}
