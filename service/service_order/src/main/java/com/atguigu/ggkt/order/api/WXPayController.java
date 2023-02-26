package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.order.service.WXPayService;
import com.atguigu.ggkt.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/25 13:54
 * @Version 1.0
 */

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/order/wxPay")
public class WXPayController {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private WXPayService wxPayService;
    //微信支付方法
    @ApiOperation(value = "下单 小程序支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        Map<String ,Object> map = wxPayService.createJsapi(orderNo);
        return Result.ok(map);
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        System.out.println("orderNo:"+orderNo);
        //根据订单流水号调用查询接口(查询订单支付状态)
        Map<String, String> resultMap = wxPayService.queryPayStatus(orderNo);
        if (resultMap == null) {//出错
            return Result.fail(null).message("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            System.out.println("out_trade_no:"+out_trade_no);
            orderInfoService.updateOrderStatus(out_trade_no);
            return Result.ok(null).message("支付成功");
        }
        return Result.ok(null).message("支付中");
    }

}
