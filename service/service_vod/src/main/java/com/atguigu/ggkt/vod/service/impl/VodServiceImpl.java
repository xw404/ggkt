package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.vod.service.VodService;
import com.atguigu.ggkt.vod.utils.ConstantPropertiesUtil;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import org.springframework.stereotype.Service;

/**
 * @Author 小吴
 * @Date 2023/02/19 21:25
 * @Version 1.0
 */
//都需要到腾讯云的使用手册上查看调用方法
@Service
public class VodServiceImpl implements VodService {
    //上传视频
    @Override
    public String updateVideo() {
        //指定腾讯云的id和key
        VodUploadClient client = new VodUploadClient(
                ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        //创建请求对象
        VodUploadRequest request = new VodUploadRequest();
        //视频本地地址
        request.setMediaFilePath("C:\\Users\\86152\\Desktop\\测试腾讯云点播\\01.mp4");
        //指定任务流
        request.setProcedure("LongVideoPreset");
        try{
            //调用上传方法，传入接入点地域及上传请求。
            VodUploadResponse response = client.upload(ConstantPropertiesUtil.END_POINT, request);
            //返回文件id保存到业务表，用于控制视频播放
            String fileId = response.getFileId();
            System.out.println("Upload FileId = {}"+response.getFileId());
            return fileId;
        }catch (Exception e){
            throw new GgktException(20001,"上传视频失败");
        }
    }

    //删除腾讯云视频
    @Override
    public void removeVideo(String fileId) {
        try {
            //实例化认证对象，传入Id和Key
            Credential cred = new Credential(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //实例化一个http对象
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");
            // 实例化client对象,可选的
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, "",clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            req.setFileId(fileId);
            // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);
            // 输出json格式的字符串回包
            System.out.println(DeleteMediaResponse.toJsonString(resp));
        }catch (TencentCloudSDKException e){
            System.out.println(e.toString());
            throw new GgktException(20001,"删除视频失败");
        }
    }
}
