package com.atguigu.ggkt.order.service.impl;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.order.service.WXPayService;
import com.atguigu.ggkt.utils.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/25 13:56
 * @Version 1.0
 */
@Service
public class WXPayServiceImpl implements WXPayService {
    //微信支付（此方法微信官方有详细的说明）
    @Override
    public Map<String, Object> createJsapi(String orderNo)  {
        //1 封装微信支付所需要的一些参数，map集合完成
        Map<String ,String> paramMap = new HashMap<>();
        //设置参数
        paramMap.put("appid", "wxf913bfa3a2c7eeeb");   //正式服务号公众号id
        paramMap.put("mch_id", "1481962542");   //正式服务号商户号
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());  //随机数
        paramMap.put("body", "test");
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("total_fee", "1");  //为了测试，统一支付为0.01元
        paramMap.put("spbill_create_ip", "127.0.0.1");   //当前支付客户端的ip
        paramMap.put("notify_url", "http://glkt.atguigu.cn/api/order/wxPay/notify");  //支付之后的跳转
        paramMap.put("trade_type", "JSAPI");//支付类型（按照生成固定金额支付）
        /**
         * 设置参数值是当前微信用户openid：
         * 目前实现逻辑：1根据定单号获取用户id 2根据用户id获取openid
         * 为了测试，测试号不支持支付，为了能使用正式号码测试。获取正式号码中的openid
         */
//			paramMap.put("openid", "o1R-t5trto9c5sdYt6l1ncGmY5Y");
//          UserInfo userInfo = userInfoFeignClient.getById(paymentInfo.getUserId());
//			paramMap.put("openid", "oepf36SawvvS8Rdqva-Cy4flFFg");
        paramMap.put("openid", "oQTXC56lAy3xMOCkKCImHtHoLL");   //为了测试而设置的

        //2 通过httpClient方式调用微信支付的接口（微信约定的）
        HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
        //client设置参数
        String paramXml = null;
        try {
            paramXml = WXPayUtil.generateSignedXml(paramMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9");
            client.setXmlParam(paramXml);
            client.setHttps(true); //支持https请求
            //请求
            client.post();

            //3 微信支付接口还会返回相关数据。对数据封装，最终返回
            String xml = client.getContent(); //返回的数据（XML格式）
            System.out.println("xml ："+xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            if(null != resultMap.get("result_code")  && !"SUCCESS".equals(resultMap.get("result_code"))) {
                throw new GgktException(20001,"支付失败");
            }
            //4、再次封装参数
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", "wxf913bfa3a2c7eeeb");
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9");

            //返回结果
            Map<String, Object> result = new HashMap();
            result.put("appId", "wxf913bfa3a2c7eeeb");
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //查询订单支付状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //封装微信接口的参数值
            Map paramMap = new HashMap<>();
            paramMap.put("appid", "wxf913bfa3a2c7eeeb");
            paramMap.put("mch_id", "1481962542");
            paramMap.put("out_trade_no", orderNo);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //调用httpclient
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            client.setHttps(true);
            client.post();

            //封装返回结果
            String xml = client.getContent();
            System.out.println(xml);
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
