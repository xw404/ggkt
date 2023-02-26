package com.atguigu.ggkt.live.service;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


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
}
