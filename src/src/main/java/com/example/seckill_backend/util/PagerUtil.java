package com.example.seckill_backend.util;

import com.example.seckill_backend.model.Page;

public class PagerUtil {
    public static Integer getOffset(Integer page_num, Integer page_size) {
        return (page_num - 1) * page_size;
    }
}
