package com.testing.class11;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

/**
 * @Classname ShopLoginPost
 * @Description 类型说明
 * @Date 2021/1/30 22:07
 * @Created by 特斯汀Roy
 */
public class ShopLoginPost {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient roy= HttpClients.createDefault();

        //四大要素：url 方法  请求头 请求体
        HttpPost loginpost=new HttpPost("http://www.testingedu.com.cn:8000/index.php?m=Home&c=User&a=do_login&t=0.672859533627163");
        //设置请求体内容：
        StringEntity loginParam=new StringEntity("username=13800138006&password=12346&verify_code=1","UTF-8");
        //在这里其实就设置好了content-type请求头。
        loginParam.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
        loginParam.setContentEncoding("UTF-8");
        //请求体封装到请求包里面
        loginpost.setEntity(loginParam);

        //执行发包
        CloseableHttpResponse loginResponse = roy.execute(loginpost);
        String loginResult = EntityUtils.toString(loginResponse.getEntity(), "UTF-8");
        String s = UnicodeDecoder.unicodeDecode(loginResult);

        System.out.println(s);




    }

}
