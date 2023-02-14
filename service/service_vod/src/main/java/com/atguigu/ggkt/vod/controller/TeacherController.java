package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "讲师管理接口")
@RestController
@RequestMapping("/admin/vod/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

//    //1 查询所有的讲师
//    @ApiOperation("查询所有讲师")
//    @GetMapping(value = "/findAll")
//    public List<Teacher>  findAllTeacher(){
//        List<Teacher> list = teacherService.list();
//        return list;
//    }
    //1 查询所有的讲师
    @ApiOperation("查询所有讲师")
    @GetMapping(value = "/findAll")
    public Result findAllTeacher(){
        List<Teacher> list = teacherService.list();
        return Result.ok(list).message("查询成功");
    }
    //2 逻辑删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping(value = "/remove/{id}")
    public Result removeTeacher(
            @ApiParam(name = "id", value = "ID", required = true)
            @PathVariable Long id){
        boolean isSuccess = teacherService.removeById(id);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}

