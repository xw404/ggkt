package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 小吴
 * @Date 2023/02/16 16:48
 * @Version 1.0
 */
public class TestWrite {
    public static void main(String[] args) {
        //设置文件名称和路径
        String fileName="C:\\Users\\86152\\Desktop\\java-user.xlsx";
        //调用方法
        EasyExcel.write(fileName, User.class)
                .sheet("写操作")
                .doWrite(data());
    }

    private static List<User> data() {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User data = new User();
            data.setId(i);
            data.setName("张三"+i);
            list.add(data);
        }
        return list;
    }
}
