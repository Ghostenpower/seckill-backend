package com.example.seckill_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;//响应码，1代表成功，0代表失败
    private String msg;//提示信息
    private Object data;
    public static Result success(Object data){
        return new Result(1,"success",data);
    }

    public static Result success(){
        return new Result(1,"success",null);
    }

    public static Result error(String mgs){
        return new Result(0,mgs,null);
    }
    public static Result error(Object mgs){return new Result(0,"error",mgs);}
}
