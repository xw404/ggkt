package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
public interface CourseService extends IService<Course> {

    Map<String, Object> fingPageCourse(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    Long saveCourseInfo(CourseFormVo courseFormVo);

    CourseFormVo getCourseInfoById(Long id);

    void updateCourseId(CourseFormVo courseFormVo);

    CoursePublishVo getCoursePublishVo(Long id);

    void publishCourse(Long id);
}
