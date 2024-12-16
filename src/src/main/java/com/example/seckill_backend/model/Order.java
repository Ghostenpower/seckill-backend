package com.example.seckill_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    @NotNull(message = "订单id不能为空", groups = {Create.class, Cancel.class})
    private Integer order_id;        // 订单ID，主键
    @NotBlank(message = "用户id不能为空", groups = {Create.class})
    private Integer user_id;         // 用户ID
    private Integer flash_sale_id;    // 秒杀活动ID
    @NotNull(message = "订单商品id不能为空", groups = {Create.class})
    private Integer product_id;      // 商品ID
    @NotNull(message = "订单商品数量不能为空", groups = {Create.class})
    private Integer quantity;       // 购买数量
    private BigDecimal total_price;  // 订单总价
    private Integer status;         // 订单状态（1：待支付，2：已支付，3：已发货，4：已完成，5：已取消）
    private Date created_at;         // 订单创建时间
    private Date updated_at;         // 最后更新时间

    public interface Create {
    }

    private interface Cancel {
    }
}
