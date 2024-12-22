package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.ProductMapper;
import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.Product;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result createProduct(Product product, User user) {
        if(!userMapper.isAdmin(user)){
            return Result.error("权限不足");
        }else {
            productMapper.createProduct(product);
            return Result.success();
        }
    }

    public Result getProduct() {
        return Result.success(productMapper.getProduct());
    }

    public Result getProductById(Product product) {
        return Result.success(productMapper.getProductById(product));
    }

    @Transactional(rollbackFor = Exception.class)
    public Result updateProduct(Product product, User user) {
        if(!userMapper.isAdmin(user)){
            return Result.error("权限不足");
        }else {
            productMapper.updateProduct(product);
            return Result.success();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result deleteProduct(Product product, User user) {
        if(!userMapper.isAdmin(user)){
            return Result.error("权限不足");
        }else {
            productMapper.deleteProduct(product);
            return Result.success();
        }
    }
}
