package com.example.seckill_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlashSale {
    @NotNull(message = "秒杀活动ID不能为空", groups = {Get.class})
    @Builder.Default
    private Integer flash_sale_id=null; // 秒杀活动ID
    @NotNull(message = "商品ID不能为空", groups = {Create.class, Get.class})
    private Integer product_id; // 商品ID
    @NotNull(message = "秒杀开始时间不能为空", groups = {Create.class})
    @NotBlank(message = "秒杀开始时间不能为空", groups = {Create.class})
    private LocalDateTime start_time; // 活动开始时间
    @NotNull(message = "秒杀结束时间不能为空", groups = {Create.class})
    @NotBlank(message = "秒杀结束时间不能为空", groups = {Create.class})
    private LocalDateTime end_time; // 活动结束时间
    @NotNull(message = "秒杀价格不能为空", groups = {Create.class})
    private BigDecimal flash_price; // 秒杀价格
    @NotNull(message = "秒杀库存数量不能为空", groups = {Create.class})
    private Integer total_stock; // 总库存数量
    private Integer sold_count; // 已售出数量
    private Integer status; // 活动状态（1：进行中，0：已结束）

    public interface Create {}

    public interface Get {}

    // 构造函数
    public FlashSale() {
        this.flash_sale_id = null;
        this.product_id = null;
        this.start_time = null;
        this.end_time = null;
        this.flash_price = null;
        this.total_stock = null;
        this.sold_count = null;
        this.status = null;
    }
}
