package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.result.Result;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/15 14:52
 * @Version 1.0
 */
@Api(tags = "用户登录")
@RestController
@RequestMapping("/admin/vod/user")
//@CrossOrigin  //解决跨域问题
public class UserLoginController {

    //Login接口
    @PostMapping(value = "/login")
    public Result login(){
        //{"code":20000,"data":{"token":"admin-token"}}
        Map<String,Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }
    //info接口
    @GetMapping(value = "/info")
    public Result info(){
/*        {"code":20000,
        "data":{"roles":["admin"],
            "introduction":"I am a super administrator",
            "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
             "name":"Super Admin"}}*/
        Map<String,Object> map = new HashMap<>();
        map.put("roles","admin");
        map.put("introduction","I am a super administrator");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name","Super Admin");
        return Result.ok(map);
    }
}
