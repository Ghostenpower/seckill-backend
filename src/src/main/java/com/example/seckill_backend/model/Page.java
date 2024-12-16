package com.example.seckill_backend.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Page<T> {
    @Min(value = 1, message = "一页item数不能小于1")
    @NotNull(message = "一页item数不能为空")
    private Integer page_size;
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码不能小于1")
    private Integer page_num;
    private Integer total;
    private T items;
}
