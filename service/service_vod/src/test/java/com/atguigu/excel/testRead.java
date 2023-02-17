package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;

/**
 * @Author 小吴
 * @Date 2023/02/16 17:20
 * @Version 1.0
 */
public class testRead {
    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName="C:\\Users\\86152\\Desktop\\java-user.xlsx";
        //调用方法
        EasyExcel.read(fileName, User.class,new ExcelListener())
                .sheet()
                .doRead();
    }
}
