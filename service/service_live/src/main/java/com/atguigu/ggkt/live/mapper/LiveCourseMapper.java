package com.atguigu.ggkt.live.mapper;


import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 直播课程表 Mapper 接口
 * </p>
 *
 * @author 小吴
 * @since 2023-02-25
 */
public interface LiveCourseMapper extends BaseMapper<LiveCourse> {

    //获取最近的直播课程
    List<LiveCourseVo> findLatelyList();
}
