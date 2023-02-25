package com.atguigu.ggkt.wechat.service;

import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/22 15:31
 * @Version 1.0
 */
public interface MessageService {
    String receiveMessage(Map<String, String> param);

    void pushPayMessage(long id);
}
