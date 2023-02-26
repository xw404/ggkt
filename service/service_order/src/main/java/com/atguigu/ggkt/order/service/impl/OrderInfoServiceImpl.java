package com.atguigu.ggkt.order.service.impl;

import com.atguigu.ggkt.activity.CouponInfoFeignClient;
import com.atguigu.ggkt.client.CourseFeignClient;
import com.atguigu.ggkt.client.user.UserInfoFeignClient;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.activity.CouponInfo;
import com.atguigu.ggkt.model.order.OrderDetail;
import com.atguigu.ggkt.model.order.OrderInfo;
import com.atguigu.ggkt.model.user.UserInfo;
import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.order.mapper.OrderInfoMapper;
import com.atguigu.ggkt.order.service.OrderDetailService;
import com.atguigu.ggkt.order.service.OrderInfoService;
import com.atguigu.ggkt.utils.AuthContextHolder;
import com.atguigu.ggkt.utils.OrderNoUtils;
import com.atguigu.ggkt.vo.order.OrderFormVo;
import com.atguigu.ggkt.vo.order.OrderInfoQueryVo;
import com.atguigu.ggkt.vo.order.OrderInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private CouponInfoFeignClient couponInfoFeignClient;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    //生成订单方法
    @Override
    public Long submitOrder(OrderFormVo orderFormVo) {
        //1 获取生成订单条件值
        Long userId = AuthContextHolder.getUserId();    //当前用户的id（和前端有关）
        Long courseId = orderFormVo.getCourseId();  //课程id
        Long couponId = orderFormVo.getCouponId();  //优惠卷id
        //2 判断当前用户,针对当前课程是否已经生成了订单
        //课程id,用户id
        LambdaQueryWrapper<OrderDetail> wapper = new LambdaQueryWrapper<>();
        wapper.eq(OrderDetail::getCourseId,courseId);
        wapper.eq(OrderDetail::getUserId,userId);
        OrderDetail orderDetailExists = orderDetailService.getOne(wapper);
        if(orderDetailExists != null){
            //订单存在
            return orderDetailExists.getId();//直接返回订单id
        }

        //3 根据课程id查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if(course == null){
            throw new GgktException(20001,"课程不存在");
        }
        //4 根据用户id查询用户信息
        //注意获取当前用户id比较麻烦，需要前后端一起（工具类封装在common工具中）
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if(userInfo == null){
            throw new GgktException(20001,"用户不存在");
        }
        //5 根据优惠卷的id查询优惠信息
        BigDecimal couponReduce = new BigDecimal(0);   //金额处理
        if(couponId != null){   //有优惠卷再进行查询
            CouponInfo couponInfo = couponInfoFeignClient.getById(couponId);
            couponReduce = couponInfo.getAmount();   //优惠额度
        }
        //6 封装订单生成需要的数据到对象，完成添加订单
        //6.1封装数据到orderInfo中添加订单基本信息表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount()
                .subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());  //生成订单流水号
        orderInfo.setTradeBody(course.getTitle());
        orderInfo.setOrderStatus("0");
        baseMapper.insert(orderInfo);

        //6.2封装数据到orderDetail，添加订单详情信息表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(new BigDecimal(0));
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().
                subtract(orderDetail.getCouponReduce()));
        orderDetailService.save(orderDetail);

        //7 更新优惠卷数据库。表示已经使用
        if(null != orderFormVo.getCouponUseId()) {
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }
        //8 返回订单id
        return orderInfo.getId();
    }
    //根据订单号查询订单详情
    @Override
    public OrderInfoVo getOrderInfoVoById(Long id) {
        //id查询订单基本信息和详情信息
        OrderInfo orderInfo = baseMapper.selectById(id);
        OrderDetail orderDetail = orderDetailService.getById(id);
        //封装到orderInfoVo对象中
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setCourseId(orderDetail.getCourseId());
        orderInfoVo.setCourseName(orderDetail.getCourseName());
        return orderInfoVo;
    }

    @Override
    public void updateOrderStatus(String out_trade_no) {
        //根据订单号查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOutTradeNo,out_trade_no);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        //设置订单状态
        orderInfo.setOrderStatus("1");
        //调用方法
        baseMapper.updateById(orderInfo);
    }

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
