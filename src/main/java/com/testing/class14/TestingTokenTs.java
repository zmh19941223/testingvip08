package com.testing.class14;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.inter.HttpClientUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * @Classname TestingTokenTs
 * @Description 类型说明
 * @Date 2021/2/6 20:00
 * @Created by 特斯汀Roy
 */
public class TestingTokenTs {

    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();

        //auth接口请求
        HttpPost auth=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP/auth");
        String authResult = postResult(client, auth);
        AutoLogger.log.info("auth接口的返回是："+authResult);
        //auth接口中，获取当次得到的token值。
        String tokenValue = JSONPath.read(authResult, "$.token").toString();
        AutoLogger.log.info("token的值是："+tokenValue);


        //生成随机的username进行注册和登录测试。
        Random random=new Random();
        int randomint=random.nextInt(100);
        String username="roys"+randomint;
        AutoLogger.log.info("随机的用户名是："+username);

        //register接口请求
        HttpPost register=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP//register");
        register.addHeader("token",tokenValue);
        //url编码处理：
        String nicknameUrl = URLEncoder.encode("roy老师最好看");
        //注册的请求体内容，用户名用随机编号，nickname进行url编码处理
        StringEntity registerEn = createEntity("username="+username+"&pwd=123456&nickname=" + nicknameUrl + "&describe=%E6%B5%8B%E8%AF%95%E6%B3%A8%E5%86%8C");
        register.setEntity(registerEn);
        String registerResult = postResult(client, register);
        AutoLogger.log.info("registere接口返回是："+registerResult);


        //login接口请求
        HttpPost login=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP/login");
        //设置请求头，携带token
        login.addHeader("token",tokenValue);
        //设置请求体内容
        StringEntity loginEn=createEntity("username="+username+"&password=123456");
        login.setEntity(loginEn);
        String loginResult = postResult(client, login);
        AutoLogger.log.info("login接口的返回是："+loginResult);
        //获取用户id的值
        String idValue= JSONPath.read(loginResult, "$.userid").toString();
        AutoLogger.log.info("id的值是"+idValue);

        //getUserinfo接口请求
        HttpPost getUser=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP/getUserInfo");
        //添加头域token
        getUser.addHeader("token",tokenValue);
        //设置请求体的内容，id= idValue
        StringEntity getUserEn = createEntity("id=" + idValue);
        getUser.setEntity(getUserEn);
        String getUserResult = postResult(client, getUser);
        AutoLogger.log.info("getUser接口的返回是："+getUserResult);

        //logout接口请求
        HttpPost logout=new HttpPost("http://www.testingedu.com.cn:8081/inter/HTTP//logout");
        //设置请求头token
        logout.addHeader("token",tokenValue);
        String logoutResult = postResult(client, logout);
        AutoLogger.log.info("logout接口的返回是："+logoutResult);



    }


    public static String postResult(CloseableHttpClient client,HttpPost post){
        try {
            CloseableHttpResponse execute = client.execute(post);
            String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
            result = HttpClientUtils.unicodeDecode(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return e.fillInStackTrace().toString();
        }
    }

    public static StringEntity createEntity(String param){
        StringEntity en=null;
        try {
            en=new StringEntity(param);
            en.setContentType("application/x-www-form-urlencoded");
            en.setContentEncoding("utf-8");
            return en;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return en;
        }

    }



}
