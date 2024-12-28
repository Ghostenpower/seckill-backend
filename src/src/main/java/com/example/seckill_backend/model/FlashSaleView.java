package com.example.seckill_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashSaleView {
    private Long flash_sale_id;       // 秒杀活动ID
    private Long product_id;         // 商品ID
    @NotNull(message = "商品名称不能为空", groups = {SearchByName.class})
    private String product_name;     // 商品名称
    private Double original_price;   // 商品原价
    private Double flash_price;      // 秒杀价格
    private Integer start_time;  // 秒杀开始时间戳
    private Integer end_time;    // 秒杀结束时间戳
    private Integer total_stock;     // 秒杀活动总库存
    private Integer sold_count;      // 已售数量
    private Integer flash_sale_status; // 秒杀活动状态（1：进行中，0：已结束）
    private Integer product_stock;   // 商品库存

    public interface SearchByName {
    }
}
