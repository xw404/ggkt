package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.*;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @Resource
    private VideoService videoService;

    @Resource
    private ChapterService chapterService;

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

    //课程的删除
    @Override
    public void removeCourseId(Long id) {
        //根据课程id删除小节
        videoService.removeVideoByCourseId(id);
        //根据课程id删除章节
        chapterService.removeChapterByCourseId(id);
        //根据课程id删除课程描述
        descriptionService.removeById(id);
        //根据课程id删除课程
        courseMapper.deleteById(id);
    }

    //根据课程分类查询课程的列表信息
    @Override
    public Map<String ,Object> findPage(Page<Course> pageParam,
                                    CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();//名称
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long teacherId = courseQueryVo.getTeacherId();//讲师
        //封装条件
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
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
        //调用方法查询
        Page<Course> pages = baseMapper.selectPage(pageParam, wrapper);

        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数
        //每页数据集合
        //遍历获取讲师名称还有课程分类名称
        List<Course> records = pages.getRecords();
        records.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);
        });
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);
        return map;
    }
    //获取讲师和分类名称
    private Course getTeacherOrSubjectName(Course course) {
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if(teacher != null) {
            course.getParam().put("teacherName",teacher.getName());
        }
        //一级分类
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        //二级分类
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }


    //根据课程id查询课程的详情信息
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //view_count浏览数量加一
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
        //根据课程id查询数据
        //根据id查询课程详情
        CourseVo courseVo = baseMapper.selectCourseById(courseId);
        //课程章节小节数据
        List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);
        //课程描述
        CourseDescription courseDescription = descriptionService.getById(courseId);
        //课程所属的讲师
        Teacher teacher = teacherService.getById(course.getTeacherId());
        //TODO后续完善
        Boolean isBuy = false;
        //封装返回
        Map<String ,Object> map = new HashMap<>();
        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ?
                courseDescription.getDescription() : "");
        map.put("teacher", teacher);
        map.put("isBuy", isBuy);//是否购买
        return map;

    }

    //查询所有课程
    @Override
    public List<Course> findlist() {
        List<Course> list = baseMapper.selectList(null);
        list.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);
        });
        return list;
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
