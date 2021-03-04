package com.testing.class12;

import com.alibaba.fastjson.JSONPath;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

/**
 * @Classname JsonPost
 * @Description 类型说明
 * @Date 2021/2/2 21:10
 * @Created by 特斯汀Roy
 */
public class JsonPost {

    public static void main(String[] args) {
        CloseableHttpClient client= HttpClients.createDefault();
        HttpPost jsonPost=new HttpPost("https://b.zhulogic.com/designer_api/account/login_quick_pc");
        //请求体
        try {
            StringEntity jsonParam=new StringEntity("{\"phone\":\"13301234567\",\"code\":\"\",\"messageType\":3,\"registration_type\":1,\"channel\":\"zhulogic\",\"unionid\":\"\"}","UTF-8");
            jsonParam.setContentType("application/json;charset=UTF-8");
            jsonParam.setContentEncoding("UTF-8");
            jsonPost.setEntity(jsonParam);

            //请求头
//            jsonPost.setHeader("Content-Type","application/json;charset=UTF-8");
            CloseableHttpResponse execute = client.execute(jsonPost);
            String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
            System.out.println("返回结果是"+result);
            String msg = JSONPath.read(result, "$.message").toString();
            if(msg.contains("验证码不能为空")){
                System.out.println("pass");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }





    }

}
