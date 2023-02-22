package com.atguigu.ggkt.order.service.impl;

import com.atguigu.ggkt.model.order.OrderDetail;
import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.order.mapper.OrderInfoMapper;
import com.atguigu.ggkt.order.service.OrderDetailService;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-20
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;
    //订单详情
    @Resource
    private OrderDetailService orderDetailService;
    //订单列表
    @Override
    public Map<String, Object> selectOrderInfoPage(Page<OrderInfo> pageParam,
                                                   OrderInfoQueryVo orderInfoQueryVo) {
        //通过orderInfoQueryVo获取查询条件
        Long userId = orderInfoQueryVo.getUserId();
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();//交易号
        String phone = orderInfoQueryVo.getPhone();
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        //判空并封装条件
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(userId)){
            wrapper.eq(OrderInfo::getUserId,userId);
        }
        if(!StringUtils.isEmpty(outTradeNo)){
            wrapper.eq(OrderInfo::getOutTradeNo,outTradeNo);
        }
        if(!StringUtils.isEmpty(phone)){
            wrapper.eq(OrderInfo::getPhone,phone);
        }
        if(!StringUtils.isEmpty(orderStatus)){
            wrapper.eq(OrderInfo::getOrderStatus,orderStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge(OrderInfo::getCreateTime,createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le(OrderInfo::getCreateTime,createTimeEnd);
        }
        //调用方法，实现分页
        Page<OrderInfo> pages = orderInfoMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal();
        long pageCount = pages.getPages();
        List<OrderInfo> records = pages.getRecords(); //每页数据的集合
        //订单中包含详情，根据订单ID查询详情
        records.stream().forEach(item ->{
            this.getOrderDetail(item);
        });
        //封装集合返回
        Map<String ,Object> map = new HashMap<>();
        map.put("total",totalCount);
        map.put("pageCount",pageCount);
        map.put("records",records);
        return map;
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo orderInfo) {
        //id
        Long id = orderInfo.getId();
        //根据id查出订单的详情
        OrderDetail orderDetail = orderDetailService.getById(id);  //一对一关系，使用同一个id
        if(orderDetail != null){
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}
