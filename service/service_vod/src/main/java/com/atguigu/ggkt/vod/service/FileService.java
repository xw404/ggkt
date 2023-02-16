package com.atguigu.ggkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 小吴
 * @Date 2023/02/16 13:46
 * @Version 1.0
 */
public interface FileService {
    //文件上传方法
    String upload(MultipartFile file);
}
