package com.atguigu.ggkt.vod.service;

import java.util.Map;

/**
 * @Author 小吴
 * @Date 2023/02/19 21:25
 * @Version 1.0
 */
public interface VodService {
    String updateVideo();

    void removeVideo(String fileId);

    Map<String, Object> getPlayAuth(Long courseId, Long videoId);
}
