package com.atguigu.ggkt.vod.service.impl;


import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vod.mapper.VideoMapper;
import com.atguigu.ggkt.vod.service.VideoService;
import com.atguigu.ggkt.vod.service.VodService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Resource
    private VideoMapper videoMapper;
    @Resource
    private VodService vodService;
    //根据课程ID删除小节，同时删除小节里面的所有视频
    @Override
    public void removeVideoByCourseId(Long id) {
        //根据课程id查询课程中的所有小节
        LambdaQueryWrapper<Video> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Video::getCourseId,id);
        List<Video> videos = videoMapper.selectList(wrapper);
        //遍历所有小节的集合得到每个小节，获取每个小节中的视频id
        for (Video video : videos) {
            String videoSourceId = video.getVideoSourceId();
            //判空并删除腾讯云视频
            if(!StringUtils.isEmpty(videoSourceId)){
                //根据id删除小节
                vodService.removeVideo(videoSourceId);
            }
        }
        videoMapper.delete(wrapper);
    }

    //根据小节id删除小节的时候同时删除视频
    @Override
    public void removeVideoById(Long id) {
        //id查询小节的信息
        Video video = videoMapper.selectById(id);
        //获取video中的视频id
        String videoSourceId = video.getVideoSourceId();
        //判空
        if(!StringUtils.isEmpty(videoSourceId)){
            //根据视频id删除腾讯云中的视频
            vodService.removeVideo(videoSourceId);
        }
        //根据视频id删除小节
        videoMapper.deleteById(id);
    }
}
