package com.example.seckill_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class User {
    private Integer user_id;         // 用户ID，主键
    @NotNull(message = "用户名不能为空",groups ={Register.class})
    @NotBlank(message = "用户名不能为空",groups ={Register.class})
    private String username;        // 用户名
    @NotNull(message = "密码不能为空",groups ={Register.class})
    @NotBlank(message = "密码不能为空",groups ={Register.class})
    private String password;        // 密码
    private String password_hash;    // 密码哈希值
    private String email;           // 邮箱
    private String phone;           // 手机号
    private Date created_at;         // 注册时间
    private Date updated_at;         // 更新信息时间
    private Integer status;         // 用户状态（1：正常，0：禁用）
    private BigDecimal balance;     // 余额

    public interface Register{}

}