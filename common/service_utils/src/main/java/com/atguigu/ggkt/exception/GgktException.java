package com.atguigu.ggkt.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 小吴
 * @Date 2023/02/14 21:57
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GgktException extends RuntimeException{
    private Integer code;
    private String msg;
}
