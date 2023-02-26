package com.atguigu.ggkt.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author 小吴
 * @Date 2023/02/25 16:43
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "mtcloud")  //找到配置文件里面前置为这个的值完成注入
public class MTCloudAccountConfig {

    private String openId;
    private String openToken;

}
