package com.example.seckill_backend.mapper;

import com.example.seckill_backend.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    void createProduct(Product product);

    List<Product> getProduct(@Param("page_size") Integer page_size,@Param("offset") Integer offset);

    List<Product> getProductById(Product product);

    void updateProduct(Product product);

    void deleteProduct(Product product);

    List<Product> getProductByName(@Param("name") String name, @Param("page_size") Integer page_size,@Param("offset") Integer offset);

    Integer getProductCountByName(Product product);

    void descreaseStock(Integer product_id, Integer num);

    Integer getProductCount();
}
