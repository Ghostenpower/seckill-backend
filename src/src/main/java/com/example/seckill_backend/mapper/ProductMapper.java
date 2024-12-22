package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    void createProduct(Product product);

    List<Product> getProduct();

    List<Product> getProductById(Product product);

    void updateProduct(Product product);

    void deleteProduct(Product product);
}
