package com.atguigu.ggkt.vod.mapper;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
public interface CourseMapper extends BaseMapper<Course> {

    CoursePublishVo selectCoursePublishVoById(Long id);
}
