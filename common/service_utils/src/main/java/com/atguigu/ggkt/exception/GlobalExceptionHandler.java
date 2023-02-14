package com.atguigu.ggkt.exception;

import com.atguigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author 小吴
 * @Date 2023/02/14 21:42
 * @Version 1.0
 */
@ControllerAdvice  //aop思想
public class GlobalExceptionHandler {

    //全局异常处理器
    @ExceptionHandler(Exception.class)
    @ResponseBody  //处理异常，能返回json数据到前端
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局异常处理器");
    }

    //特定异常处理器ArithmeticException
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody  //处理异常，能返回json数据到前端
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行ArithmeticException异常处理器");
    }

    //处理自定义异常GgktException
    @ExceptionHandler(GgktException.class)
    @ResponseBody  //处理异常，能返回json数据到前端
    public Result error(GgktException e){
        e.printStackTrace();
        return Result.fail()
                .message(e.getMsg())
                .code(e.getCode());
    }
}
