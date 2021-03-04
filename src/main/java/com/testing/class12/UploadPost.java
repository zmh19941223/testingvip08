package com.testing.class12;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * @Classname UploadPost
 * @Description 类型说明
 * @Date 2021/2/2 21:24
 * @Created by 特斯汀Roy
 */
public class UploadPost {
    public static void main(String[] args) {
        CloseableHttpClient client= HttpClients.createDefault();

        HttpPost uploadPost=new HttpPost("http://www.testingedu.com.cn:8000/index.php/home/Uploadify/imageUp/savepath/head_pic/pictitle/banner/dir/images.html");
        //multipart请求体的设置，通过multipartEntityBuilder来设置
        MultipartEntityBuilder meb=MultipartEntityBuilder.create();
        meb.addTextBody("id","WU_FILE_0");
        meb.addTextBody("name","持续集成架构图.png");
        meb.addTextBody("type","image/png");
        meb.addTextBody("lastModifiedDate","Fri Mar 06 2020 11:47:02 GMT+0800 (中国标准时间)");
        //添加文件格式的参数
        meb.addBinaryBody("file",new File("E:\\VIPclassnew\\持续集成架构图.png"));
        //完成multipart请求体的构建。
        HttpEntity fileContent = meb.build();
        //将创建好的请求体，加到请求包里面去。
        uploadPost.setEntity(fileContent);
        //由于boundry是httpclient自动生成的，会自己带上content-type，所以，千万不要自己再指定一次。
//        uploadPost.setHeader("Content-Type","multipart/form-data");

        try {
            CloseableHttpResponse execute = client.execute(uploadPost);
            String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
            System.out.println(result);
            String state = JSONPath.read(result, "$.state").toString();
            if("SUCCESS".equals(state)){
                AutoLogger.log.info("pass");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
