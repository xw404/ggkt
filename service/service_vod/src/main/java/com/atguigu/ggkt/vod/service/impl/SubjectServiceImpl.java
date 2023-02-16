package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
        implements SubjectService {

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
}
