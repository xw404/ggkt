package com.atguigu.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author 小吴
 * @Date 2023/02/16 16:46
 * @Version 1.0
 */
@Data
public class User {

    //注意：index表示第几列（对应关系映射）
    @ExcelProperty(value = "用户编号",index = 0)
    private int id;

    @ExcelProperty(value = "用户名称",index = 1)
    private String name;

}
