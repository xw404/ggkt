package com.atguigu.xecel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author 小吴
 * @Date 2023/02/16 16:46
 * @Version 1.0
 */
@Data
public class User {

    @ExcelProperty(value = "用户编号")
    private int id;

    @ExcelProperty(value = "用户名称")
    private String name;

}
