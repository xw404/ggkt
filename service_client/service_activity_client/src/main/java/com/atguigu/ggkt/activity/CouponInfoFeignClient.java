package com.atguigu.ggkt.activity;

import com.atguigu.ggkt.model.activity.CouponInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author 小吴
 * @Date 2023/02/23 16:26
 * @Version 1.0
 */
@FeignClient(value = "service-activity")
public interface CouponInfoFeignClient {

    /**
     * 查询优惠卷
     * @param couponId
     * @return
     */
    @ApiOperation(value = "获取优惠券")
    @GetMapping(value = "/api/activity/couponInfo/inner/getById/{couponId}")
    CouponInfo getById(@PathVariable("couponId") Long couponId);

    /**
     * 更新优惠券使用状态
     */
    @GetMapping(value = "/api/activity/couponInfo/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId,
                                      @PathVariable("orderId") Long orderId);

}