package com.atguigu.ggkt.result;

import lombok.Data;

/**
 * @Author 小吴
 * @Date 2023/02/14 17:38
 * @Version 1.0
 */
//同意返回结果类
@Data
public class Result<T> {

    private Integer code;  //状态码

    private String message;   //返回状态信息

    private T data;  //返回数据

    public Result(){

    }
    //成功的方法（没有数据）
    public  static<T> Result<T> ok(){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }
    //失败的方法（没有数据）
    public  static<T> Result<T> fail(){
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMessage("失败");
        return result;
    }

    //成功的方法（有数据）
    public  static<T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("成功");
        if(data!=null){
            result.setData(data);
        }
        return result;
    }
    //失败的方法（有数据）
    public  static<T> Result<T> fail(T data){
        Result<T> result = new Result<>();
        result.setCode(201);
        result.setMessage("失败");
        if(data!=null){
            result.setData(data);
        }
        return result;
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}