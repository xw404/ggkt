package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vod.service.ChapterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationSupport;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
@RestController
@RequestMapping(value="/admin/vod/chapter")
@CrossOrigin
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    //1课程大纲的列表方法（章节和小节）
    @ApiOperation("大纲的列表方法")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable Long courseId){

        List<ChapterVo> list = chapterService.getTreeList(courseId);
        return Result.ok(list);
    }

    //2添加章节
    @PostMapping("save")
    public Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.ok();
    }

    //3修改-根据id查询
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }

    //4修改-最终实现
    @PostMapping(value = "update")
    public Result update(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.ok();
    }

    //删除章节
    @DeleteMapping(value = "remove/{id}")
    public Result remove(@PathVariable Long id){
        chapterService.removeById(id);
        return Result.ok();
    }

}
