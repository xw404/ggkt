package com.atguigu.ggkt.live.service;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.vo.live.LiveCourseConfigVo;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.atguigu.ggkt.vo.live.LiveCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-25
 */
public interface LiveCourseService extends IService<LiveCourse> {

    //直播课程的列表方法
    IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam);

    void saveLive(LiveCourseFormVo liveCourseVo);

    void removeLive(Long id);

    //查询课程的基本信息和描述信息
    LiveCourseFormVo getLiveCourseFormVo(Long id);

    //更新直播课程信息
    void updateLiveById(LiveCourseFormVo liveCourseVo);

    LiveCourseConfigVo getCourseConfig(Long id);

    void updateConfig(LiveCourseConfigVo liveCourseConfigVo);

    List<LiveCourseVo> findLatelyList();
}
