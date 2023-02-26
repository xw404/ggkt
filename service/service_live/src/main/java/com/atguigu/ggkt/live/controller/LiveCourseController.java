package com.atguigu.ggkt.live.controller;


import com.atguigu.ggkt.live.service.LiveCourseAccountService;
import com.atguigu.ggkt.live.service.LiveCourseService;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.model.live.LiveCourseAccount;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author 小吴
 * @since 2023-02-25
 */
@RestController
@RequestMapping(value="/admin/live/liveCourse")
public class LiveCourseController {
    @Autowired
    private LiveCourseService liveCourseService;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page<>(page, limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //直播课程的添加
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseVo) {
        liveCourseService.saveLive(liveCourseVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        liveCourseService.removeLive(id);
        return Result.ok(null);
    }

    //查询课程的基本信息
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result<LiveCourse> get(@PathVariable Long id) {
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.ok(liveCourse);
    }

    //查询课程的基本信息和描述信息
    @ApiOperation(value = "获取")
    @GetMapping("getInfo/{id}")
    public Result<LiveCourseFormVo> getInfo(@PathVariable Long id) {
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseFormVo(id);
        return Result.ok(liveCourseFormVo);
    }

    //更新直播课程信息
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result update(@RequestBody LiveCourseFormVo liveCourseVo) {
        liveCourseService.updateLiveById(liveCourseVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取直播账号信息")
    @GetMapping("getLiveCourseAccount/{id}")
    public Result<LiveCourseAccount> getLiveCourseAccount(@PathVariable Long id) {
        LiveCourseAccount liveCourseAccount = liveCourseAccountService.getLiveCourseAccountCourseId(id);
        return Result.ok(liveCourseAccount);
    }
    @ApiOperation(value = "获取直播的配置信息")
    @GetMapping("getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id) {
        LiveCourseConfigVo liveCourseConfigVo = liveCourseService.getCourseConfig(id);
        return Result.ok(liveCourseConfigVo);
    }

    @ApiOperation(value = "修改配置")
    @PutMapping("updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "获取最近的直播")
    @GetMapping("findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> list = liveCourseService.findLatelyList();
        return Result.ok(list);
    }
}

