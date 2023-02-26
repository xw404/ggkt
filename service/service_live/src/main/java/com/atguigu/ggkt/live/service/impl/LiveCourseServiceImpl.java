package com.atguigu.ggkt.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.client.CourseFeignClient;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.live.mtcloud.CommonResult;
import com.atguigu.ggkt.live.mtcloud.MTCloud;
import com.atguigu.ggkt.model.live.LiveCourse;
import com.atguigu.ggkt.live.mapper.LiveCourseMapper;
import com.atguigu.ggkt.live.service.LiveCourseService;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.live.LiveCourseFormVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-25
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Resource
    private MTCloud mtCloudClient;
    //直播课程的列表方法
    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        //分页查询
        IPage<LiveCourse> pageModel = baseMapper.selectPage(pageParam, null);
        //获取直播课程里面的讲师信息
        List<LiveCourse> recordList = pageModel.getRecords();
        for (LiveCourse liveCourse:recordList){
            Long teacherId = liveCourse.getTeacherId();
            //根据讲师id查询讲师信息
            Teacher teacher = courseFeignClient.getTeacherInfo(teacherId);
            //封装数据
            if(teacher!= null) {
                liveCourse.getParam().put("teacherName", teacher.getName());
                liveCourse.getParam().put("teacherLevel", teacher.getLevel());
            }
        }
        return pageModel;
    }

    //直播课程的添加
    @Override
    public void saveLive(LiveCourseFormVo liveCourseFormVo) {
        //LiveCourseFormVo转换为liveCouse对象
        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);

        //获取讲师的信息
        Teacher teacher =
                courseFeignClient.getTeacherInfo(liveCourseFormVo.getTeacherId());
        //调用方法添加直播课程
        //创建map集合，封装直播课程需要的其他参数
        HashMap<Object,Object> options  = new HashMap<>();
        options.put("scenes", 2);//直播类型。1: 教育直播，2: 生活直播。默认 1，说明：根据平台开通的直播类型填写
        options.put("password", liveCourseFormVo.getPassword());
        try {
            String res = mtCloudClient.courseAdd(
                    liveCourse.getCourseName(),  //直播名
                    teacher.getId().toString(),  //直播ID
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),  //开始时间
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),  //结束时间
                    teacher.getName(),     //主播昵称
                    teacher.getIntro(),    //主播介绍
                    options);//其他参数
            System.out.println("res :"+res);
            //把创建之后的结果转换并判断
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {  //创建成功
                //添加直播的基本信息到信息表中

                JSONObject object = commonResult.getData();
                //添加直播的描述信息到描述表中

                //添加直播账号信息

            }else {  //创建失败
                System.out.println(commonResult.getmsg());
                throw new GgktException(20001,"直播创建失败");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
