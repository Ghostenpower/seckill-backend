package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.Order;
import com.example.seckill_backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order> getOrder(Integer user_id,Integer page_size ,Integer offset);

    @Select("select count(*) from orders where user_id=#{user_id}")
    Integer getOrderTotal(User user);
}
