package com.atguigu.ggkt.vod.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vod.listener.SubjectListener;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
        implements SubjectService {

    @Autowired
    private SubjectListener subjectListener;
    //注意：（每个id是一层数据）
    @Override
    public List<Subject> selectSubjectList(Long id) {
        //SELECT * FROM SUBJECT WHERE PARENT_ID =O
        //条件构造器
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Subject> subjectList = baseMapper.selectList(wrapper);
        //subjectList遍历得到每个对象，判断是否有下一层对象，如果有，把hasChildren值设置为true
        for(Subject subject:subjectList){
            //获取id值
            Long subjectId = subject.getId();
            //查询
            boolean isChild = this.idChildren(subjectId);
            //封装到对象
            subject.setHasChildren(isChild);
        }
        return subjectList;
    }

    //判断是否有下一层数据
    private boolean idChildren(Long subjectId) {
        //条件构造器
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",subjectId);
        return baseMapper.selectCount(wrapper)>0;
    }

    //课程分类导出
    @Override
    public void exportData(HttpServletResponse response) {
        //设置下载信息
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            //查询课程分类表里面的所有数据
            List<Subject> subjectList = baseMapper.selectList(null);
            //对象转换
            List<SubjectEeVo> subjectEeVoList = new ArrayList<>();
            for (Subject subject:subjectList){
                SubjectEeVo subjectEeVo = new SubjectEeVo();
//                subjectEeVo.setId(subject.getId());
//                subjectEeVo.setParentId(subject.getParentId());
                BeanUtils.copyProperties(subject,subjectEeVo);
                subjectEeVoList.add(subjectEeVo);
            }
            //EasyExcel写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类")
                    .doWrite(subjectEeVoList);
        } catch (Exception e) {
            throw new GgktException(20001,"导出失败");
        }
    }

    //文件上传
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), SubjectEeVo.class,subjectListener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"导入失败");
        }
    }

}
