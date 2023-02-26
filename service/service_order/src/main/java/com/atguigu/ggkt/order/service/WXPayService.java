package com.atguigu.ggkt.order.service;

import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/25 13:56
 * @Version 1.0
 */
public interface WXPayService {
    //微信支付
    Map<String, Object> createJsapi(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);
}
