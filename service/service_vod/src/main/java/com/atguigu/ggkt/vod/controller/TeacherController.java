package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        //模拟异常
        try {
            int i = 1/0;
        }catch (Exception e){
            throw new GgktException(201,"自定义的异常GgktException");
        }

        List<Teacher> list = teacherService.list();
        return Result.ok(list).message("查询成功");
    }

    //2 逻辑删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping(value = "/remove/{id}")
    public Result removeTeacher(
            @ApiParam(name = "id", value = "ID", required = true)
            @PathVariable long id){
        boolean isSuccess = teacherService.removeById(id);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //3 分页条件查询讲师
    @ApiOperation("条件查询分页")
    @PostMapping(value = "/findQueryPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        //创建page对象
        Page<Teacher> pageParam= new Page<>(current,limit);
        //判断ew Result<Teacher>()对象是否为空
        if(teacherQueryVo==null){
            //查询全部
            Page<Teacher> pageModel = teacherService.page(pageParam,null);
            return Result.ok(pageModel);
        }else {
            //获取条件，判断并封装条件
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();

            QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
            if(!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }
            if(!StringUtils.isEmpty(level)){
                wrapper.eq("level",level);
            }
            if(!StringUtils.isEmpty(joinDateBegin)){
                wrapper.ge("join_date",joinDateBegin);
            }
            if(!StringUtils.isEmpty(joinDateEnd)){
                wrapper.le("join_date",joinDateEnd);
            }
            //调用方法
            IPage<Teacher> pageModel = teacherService.page(pageParam, wrapper);  //传入分页参数和条件
            return Result.ok(pageModel);
        }
    }

    //4 添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result saveTeacher(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.save(teacher);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //5 修改讲师接口（先查询在修改）
    @ApiOperation("根据ID查询")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable long id){
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }

    //6 修改，最终实现
    @ApiOperation("修改最终实现")
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody Teacher teacher){
        boolean isSuccess = teacherService.updateById(teacher);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //批量删除讲师
    //注意，前端传过来的是json数组  [1,2,3,4,...]
    @ApiOperation("批量删除讲师")
    @DeleteMapping("removeBatch")
    public Result removeBatch(@RequestBody List<Long> list){
        boolean isSuccess = teacherService.removeByIds(list);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}

