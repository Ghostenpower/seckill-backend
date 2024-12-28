package com.example.seckill_backend.service;

import com.example.seckill_backend.mapper.OrderMapper;
import com.example.seckill_backend.mapper.ProductMapper;
import com.example.seckill_backend.mapper.UserMapper;
import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Product;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.util.PagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
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

    public Result getProduct(Page<Object> page) {
        page.setTotal(productMapper.getProductCount());
        page.setItems(productMapper.getProduct(page.getPage_size(), PagerUtil.getOffset(page.getPage_num(), page.getPage_size())));
        return Result.success(page);
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

    public Result getProductByName(Page<Object> page,Product product) {
        page.setTotal(productMapper.getProductCountByName(product));
        List<Product> products = productMapper.getProductByName(product.getName(),page.getPage_size() ,PagerUtil.getOffset(page.getPage_num(), page.getPage_size()));
        page.setItems(products);
        return Result.success(page);
    }

    public Result buy(Product product, Integer num, User user) {
        List<Product> product1 = productMapper.getProductById(product);
        if(product1.get(0).getStock()<num){
            return Result.error("库存不足");
        }else if(product1.get(0).getStatus()==0){
            return Result.error("该商品已下架");
        }else{
            productMapper.descreaseStock(product.getProduct_id(),num);
            orderMapper.createOrder(user.getUser_id(),null,product.getProduct_id(),num,product1.get(0).getPrice().multiply(new java.math.BigDecimal(num)));
            return Result.success();
        }
    }

    public Result editProduct(Product product, User admin) {
        if(!userMapper.isAdmin(admin)){
            return Result.error("权限不足");
        }else {
            productMapper.updateProduct(product);
            return Result.success();
        }
    }
}
