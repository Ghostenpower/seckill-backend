package com.example.seckill_backend.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    @NotNull(message = "商品ID不能为空", groups = {Create.class, Update.class})
    private Integer product_id; // 商品ID

    @NotNull(message = "商品名称不能为空", groups = {Create.class, Update.class})
    @Size(min = 1, max = 100, message = "商品名称长度必须在1到100字符之间", groups = {Create.class, Update.class})
    private String name; // 商品名称

    @NotNull(message = "商品价格不能为空", groups = {Create.class, Update.class})
    @DecimalMin(value = "0.01", message = "商品价格必须大于0", groups = {Create.class, Update.class})
    private BigDecimal price; // 商品价格

    @NotNull(message = "商品库存不能为空", groups = {Create.class, Update.class})
    @Min(value = 0, message = "商品库存不能小于0", groups = {Create.class, Update.class})
    private Integer stock; // 商品库存

    @Size(max = 500, message = "商品描述不能超过500字符", groups = {Create.class, Update.class})
    private String description; // 商品描述

    @NotNull(message = "商品状态不能为空", groups = {Create.class, Update.class})
    private Integer status; // 商品状态（1：上架，0：下架）

    public interface Create {}
    public interface Update {}
}
