package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.Page;
import com.example.seckill_backend.model.Product;
import com.example.seckill_backend.model.Result;
import com.example.seckill_backend.model.User;
import com.example.seckill_backend.service.ProductService;
import com.example.seckill_backend.util.MultiRequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public Result createProduct(@RequestBody @Validated(Product.Create.class) Product product, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return productService.createProduct(product,user);
    }

    @PostMapping("/get")
    public Result getProduct(@MultiRequestBody @Validated Page<Object> page) {
        return productService.getProduct(page);
    }

    @PostMapping("/getByName")
    public Result getProductByName(@MultiRequestBody @Validated Page<Object> page,@MultiRequestBody @Validated(Product.Search.class) Product product) {
        return productService.getProductByName(page,product);
    }

    @PostMapping("/getByNamePlus")
    public Result getByNamePlus(@MultiRequestBody @Validated Page<Object> page,@MultiRequestBody Product product) {
        return productService.getProductByName(page,product);
    }

    @PostMapping("/getById")
    public Result getProductById(@RequestBody @Validated(Product.Search.class) Product product) {
        return productService.getProductById(product);
    }

    @PostMapping("/update")
    public Result updateProduct(@RequestBody @Validated(Product.Update.class) Product product, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return productService.updateProduct(product,user);
    }

    @PostMapping("/delete")
    public Result deleteProduct(@RequestBody @Validated(Product.DELETE.class) Product product, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return productService.deleteProduct(product,user);
    }

    @PostMapping("/test")
    public Result test(@RequestBody @Validated(Product.Test.class) Product product){
        return Result.success();
    }

    @PostMapping("buy")
    public Result buy(@MultiRequestBody @Validated(Product.Buy.class) Product product,@MultiRequestBody @Validated Integer num, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return productService.buy(product,num,user);
    }

    @PostMapping("edit")
    public Result editProduct(@RequestBody @Validated(Product.Update.class) Product product, HttpServletRequest request) {
        User admin = (User) request.getAttribute("user");
        return productService.editProduct(product,admin);
    }
}
