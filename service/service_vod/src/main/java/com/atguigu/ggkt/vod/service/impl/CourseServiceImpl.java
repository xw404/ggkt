package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.CourseDescriptionService;
import com.atguigu.ggkt.vod.service.CourseService;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {


    @Resource
    private TeacherService teacherService;
    @Resource
    private SubjectService subjectService;
    @Resource
    private CourseDescriptionService descriptionService;
    @Resource
    private CourseMapper courseMapper;
    //点播课程的查询功能
    @Override
    public Map<String, Object> fingPageCourse(Page<Course> pageParam,
                                             CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();
        Long subjectId = courseQueryVo.getSubjectId();  //第二层分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();  //第一层分类
        Long teacherId = courseQueryVo.getTeacherId();
        //判断条件并封装
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }

        //调用方法实现查询分页
        Page<Course> pages = baseMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal(); //总记录
        long totalPage = pages.getPages();  //总页数
        List<Course> list = pages.getRecords();  //每页数据集合

        //获取id对应的名称，进行封装  最终显示
        list.stream().forEach(item -> {
            this.getNameById(item);
        });
        //封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",list);
        return map;
    }

    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {

        //添加课程的基本信息，操作course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.insert(course);
        //添加课程描述信息，操作course_description表
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        //设置课程id
        courseDescription.setId(course.getId());
        descriptionService.save(courseDescription);
        return course.getId();
    }

    /**
     * 根据Id查询课程信息
     */
    @Override
    public CourseFormVo getCourseInfoById(Long id) {
        //课程信息
        Course course = baseMapper.selectById(id);
        if(course==null) {return null;}
        //描述信息
        CourseDescription courseDescription = descriptionService.getById(id);
        if(courseDescription==null) {return null;}
        //封装对象
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course,courseFormVo);
        courseFormVo.setDescription(courseDescription.getDescription());
        return courseFormVo;
    }
    //修改课程信息
    @Override
    public void updateCourseId(CourseFormVo courseFormVo) {
        //修改课程信息和描述信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);
        //修改描述信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        //设置课程描述的id
        courseDescription.setId(course.getId());
        descriptionService.updateById(courseDescription);
    }

    //根据课程Id查询发布的课程信息
    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return courseMapper.selectCoursePublishVoById(id);
    }

    //课程的最终发布（修改课程状态）
    @Override
    public void publishCourse(Long id) {
        Course course = courseMapper.selectById(id);
        course.setStatus(1);  //代表课程已经发布
        course.setPublishTime(new Date());
        courseMapper.updateById(course);
    }

    //获取id对应的名称，进行封装  最终显示
    private Course getNameById(Course course) {
        //根据讲师id获取讲师名称
        Teacher teacher = this.teacherService.getById(course.getTeacherId());
        if(teacher!=null){
            String teacherName = teacher.getName();
            course.getParam().put("teacherName",teacherName);
        }
        //根据课程分类id获取课程名称
        Subject subjectOne = this.subjectService.getById(course.getSubjectParentId());
        if(subjectOne!=null){
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = this.subjectService.getById(course.getSubjectId());
        if(subjectTwo!=null){
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }
}
