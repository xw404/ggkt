package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.VideoVisitor;
import com.atguigu.ggkt.vo.vod.VideoVisitorCountVo;
import com.atguigu.ggkt.vo.vod.VideoVisitorVo;
import com.atguigu.ggkt.vod.mapper.VideoVisitorMapper;
import com.atguigu.ggkt.vod.service.VideoVisitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-19
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    @Resource
    private VideoVisitorMapper videoVisitorMapper;
    //统计课程的接口
    @Override
    public Map<String, Object> findCount(Long courseID, String startDate, String endDate) {
        List<VideoVisitorCountVo> videoVisitorCountVoList = videoVisitorMapper.findCount(courseID, startDate, endDate);
        //数据封装
        Map<String ,Object> map = new HashMap<>();
        //创建map创建两个list集合，一个代表日期，一个代表日期上的访问数量
        List<String> dateList = new ArrayList<>();
        List<Integer> countList = new ArrayList<>();
        //遍历集合
        dateList = videoVisitorCountVoList.stream()
                .map(VideoVisitorCountVo::getJoinTime)
                .collect(Collectors.toList());
        countList = videoVisitorCountVoList.stream()
                .map(VideoVisitorCountVo::getUserCount)
                .collect(Collectors.toList());
        //放到Map集合中
        map.put("xData",dateList);
        map.put("yData",countList);
        return map;
    }
}
