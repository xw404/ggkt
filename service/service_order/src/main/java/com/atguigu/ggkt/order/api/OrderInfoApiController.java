package com.atguigu.ggkt.order.api;

import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author 小吴
 * @Date 2023/02/23 9:22
 * @Version 1.0
 */
@RestController
@RequestMapping("api/order/orderInfo")
public class OrderInfoApiController {
    @Resource
    private OrderInfoService orderInfoService;

    @ApiOperation("新增点播课程订单")
    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo, HttpServletRequest request) {
        //返回订单id
        Long orderId = orderInfoService.submitOrder(orderFormVo);
        return Result.ok(orderId);
    }

    //根据订单号码获取订单详情
    @ApiOperation(value = "获取")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
        OrderInfoVo orderInfoVo = orderInfoService.getOrderInfoVoById(id);
        return Result.ok(orderInfoVo);
    }
}
