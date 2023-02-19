package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vo.vod.VideoVo;
import com.atguigu.ggkt.vod.mapper.ChapterMapper;
import com.atguigu.ggkt.vod.service.ChapterService;
import com.atguigu.ggkt.vod.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.BagUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-16
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;
    //1 课程章节和小节的列表方法
    @Override
    public List<ChapterVo> getTreeList(Long courseId) {
        //定义最终list集合
        List<ChapterVo> finalChapterList = new ArrayList<>();
        //根据课程id获取课程的所有章节
        LambdaQueryWrapper<Chapter> wrapperChapter = new LambdaQueryWrapper<>();
        wrapperChapter.eq(Chapter::getCourseId,courseId);
        List<Chapter> chapterList = baseMapper.selectList(wrapperChapter);

        //根据课程ID获取所有的小节部分
        LambdaQueryWrapper<Video> wrapperVideo = new LambdaQueryWrapper<>();
        wrapperVideo.eq(Video::getCourseId,courseId);
        List<Video> videoList = videoService.list(wrapperVideo);

        //封装章节
        //遍历章节集合，得到每个章节
        for(int i =0;i<chapterList.size();i++) {
            Chapter chapter = chapterList.get(i);
            //对象转换---放入集合
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            finalChapterList.add(chapterVo);
            //封装章节里面的小节部分
            //创建List集合用于封装章节的所有小节
            List<VideoVo> videoVoList = new ArrayList<>();
            for (Video video : videoList) {
                //判断小节是哪个章节下面的
                if (chapter.getId().equals(video.getChapterId())){
                    //封装为VideoVo对象并存入集合
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            //循环结束，把小节集合放入章节
            chapterVo.setChildren(videoVoList);
        }
        //放入集合
        return finalChapterList;
    }
}
