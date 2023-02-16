package com.atguigu;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;
/**
 * @Author 小吴
 * @Date 2023/02/16 12:48
 * @Version 1.0
 */
public class testCos {
    public static void main(String[] args) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = "AKIDpcHrFTfhZWL0VyJ0Z0DCxa4PdkkZDlZ9";
        String secretKey = "Tt8Daq261kPtBKF0c5MuJZddZO5hIvPj";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域,
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        try{
            // 指定要上传的文件
            File localFile = new File("D:\\图片\\图片壁纸\\01.jpg");
            // 指定文件将要存放的存储桶
            String bucketName = "ggkt-project-1316879505";
            // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
            String key = "/2023/01/01.jpg";
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            System.out.println(JSON.toJSONString(putObjectResult));
        } catch (Exception clientException) {
            clientException.printStackTrace();
        }

    }
}
