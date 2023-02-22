package com.atguigu.ggkt.client.user;

import com.atguigu.ggkt.model.user.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author 小吴
 * @Date 2023/02/20 22:45
 * @Version 1.0
 */
//远程调用
@FeignClient(value = "service-user")
public interface UserInfoFeignClient {

    @GetMapping("/admin/user/userInfo/inner/getById/{id}")
    UserInfo getById(@PathVariable Long id);

}
