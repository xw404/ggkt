package com.atguigu.ggkt.vod.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 小吴
 * @Date 2023/02/14 16:30
 * @Version 1.0
 */
@Configuration
@MapperScan("com.atguigu.ggkt.vod.mapper")    //mapper映射
public class VodConfig {

    /**
     * 配置分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
