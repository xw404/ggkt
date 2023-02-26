package com.atguigu.ggkt.vod.controller;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.service.CourseService;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
@Api(tags = "课程管理接口")
@RestController
@RequestMapping("admin/vod/course")
//@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;
    @GetMapping("findAll")
    public Result findAll() {
        List<Course> list = courseService.findlist();
        return Result.ok(list);
    }

    @ApiOperation("点播课程列表")
    @GetMapping(value = "/{page}/{limit}")
    public Result courseList(@PathVariable Long page,
                             @PathVariable Long limit,
                             CourseQueryVo courseQueryVo){
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = courseService.fingPageCourse(pageParam,courseQueryVo);
        return Result.ok(map);
    }

    //添加课程基本信息
    @ApiOperation("添加课程基本信息")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo){
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    //根据Id获取课程信息

    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        CourseFormVo courseFormVo = courseService.getCourseInfoById(id);
        return Result.ok(courseFormVo);
    }
    //修改课程信息
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result update(@RequestBody CourseFormVo courseFormVo){
        courseService.updateCourseId(courseFormVo);
        //返回课程id  后续使用
        return Result.ok(courseFormVo.getId());
    }

    //根据课程Id查询发布的课程信息
    @ApiOperation("id查询发布课程的信息")
    @GetMapping("/getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable Long id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }

    //课程的最终发布
    @ApiOperation("课程的最终发布")
    @PostMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable Long id){
        courseService.publishCourse(id);
        return Result.ok();
    }

    //删除课程接口
    @ApiOperation("删除课程接口")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseId(id);
        return Result.ok();
    }


}

