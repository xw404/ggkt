package com.atguigu.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/16 17:15
 * @Version 1.0
 */
public class ExcelListener extends AnalysisEventListener<User> {

    //一行一行的从Excel读取内容，并封装到对象中
    //从第二行开始读取
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println(user);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //所有操作之后才会执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
