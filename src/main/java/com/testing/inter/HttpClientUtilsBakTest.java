package com.testing.inter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.testing.common.AutoLogger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname HttpClientUtils
 * @Description 类型说明
 * @Date 2021/2/2 21:47
 * @Created by 特斯汀Roy
 */
public class HttpClientUtilsBakTest {


    //client对象，用于发包。
    public CloseableHttpClient client;

    //成员变量 用于存储cookie
    Header[] cookies=null;

    //unicode解码方法
    public static String unicodeDecode(String origin){
        //需要找到字符串中的 u4位16进制数的格式，进行匹配。
        //用正则表达式进行查找匹配。
        //由于替换的时候只需要用到4位数字来匹配字符，所以加上() 表示是一个分组进行获取。
        Pattern uni=Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m=uni.matcher(origin);
        //创建stringbuffer对象，声明长度为原本的字符串长度。
        StringBuffer stringBuffer=new StringBuffer(origin.length());
        while (m.find()){
            m.appendReplacement(stringBuffer,Character.toString((char)Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(stringBuffer);
        return stringBuffer.toString();
    }


    //实例化client的方法
    public CloseableHttpClient createclient(){
        client= HttpClients.createDefault();
        return client;
    }

    //get请求，返回结果
    public String doget(String url){
        CloseableHttpClient client=createclient();
        HttpGet get=new HttpGet(url);
        try {

            if(cookies!=null){
                for (Header cookie : cookies) {
                    if(cookie.getValue().contains("PHPSESSID")){
                        //获取cookie值的最后一个;进行截断
                        System.out.println(cookie.getValue());
                        System.out.println(cookie.getValue().substring(0,cookie.getValue().lastIndexOf(";")));
                        get.addHeader("Cookie",cookie.getValue().substring(0,cookie.getValue().lastIndexOf(";")));
                    }
                }
            }

            //返回结果
            CloseableHttpResponse execute = client.execute(get);

            //从返回包中获取头域，set-cookie
            Header[] headers = execute.getHeaders("Set-Cookie");
            cookies=headers;
            for (Header cookie : cookies) {
                AutoLogger.log.info("cookie的键是"+cookie.getName()+"cookie的值是"+cookie.getValue());
            }
            String result = EntityUtils.toString(execute.getEntity(),"UTF-8");
            //unicode 转码
            result=unicodeDecode(result);
            return result;
        } catch (IOException e) {
            //报错信息作为返回结果。
            AutoLogger.log.error(e,e.fillInStackTrace());
            return e.fillInStackTrace().toString();
        }
    }



    //post
    //输入参数，来指定不同content-type
    public String doPost(String url,String contentType,String param){
        CloseableHttpClient client=createclient();
        HttpPost post=new HttpPost(url);
        try {
            if(cookies!=null){

                for (Header cookie : cookies) {

                    if(cookie.getValue().contains("PHPSESSID")){
                        //获取cookie值的最后一个;进行截断
                        System.out.println(cookie.getValue());
                        System.out.println(cookie.getValue().substring(0,cookie.getValue().lastIndexOf(";")));
                        post.addHeader("Cookie",cookie.getValue().substring(0,cookie.getValue().lastIndexOf(";")));
                    }
                }
            }
            HttpEntity postParam=null;
            //根据contenttype，拼接请求体
            switch (contentType){
                case "urlencoded":
                    StringEntity urlen=new StringEntity(param,"utf-8");
                    urlen.setContentType("application/x-www-form-urlencoded");
                    urlen.setContentEncoding("UTF-8");
                    //向上转型。
                    postParam=urlen;
                    break;
                case "json":
                    StringEntity jsonEn=new StringEntity(param,"utf-8");
                    jsonEn.setContentType("application/x-www-form-urlencoded");
                    jsonEn.setContentEncoding("UTF-8");
                    postParam=jsonEn;
                    break;
                case "file":
                    //基于设计好的规则，解析json中的内容    { "text":{"键":"值"},"bin":{"键":"值"} }
                    MultipartEntityBuilder meb=MultipartEntityBuilder.create();
                    JSONObject jsonObject = JSON.parseObject(param);
                    //如果text不为空，则遍历解析所有text内容，进行添加
                    if (jsonObject.get("text")!=null){
                        for (String key : jsonObject.getJSONObject("text").keySet()) {
                            //遍历text中的每一个键值对，添加为textbody
                            meb.addTextBody(key,jsonObject.getJSONObject("text").getString(key));
                        }
                    }
                    //如果bin不为空，则解析所有bin内容添加
                    if(jsonObject.get("bin")!=null){
                        for (String key : jsonObject.getJSONObject("bin").keySet()) {
                            meb.addBinaryBody(key,new File(jsonObject.getJSONObject("bin").getString(key)));
                        }
                    }
                    postParam=meb.build();
                    break;
            }
            post.setEntity(postParam);
            CloseableHttpResponse execute = client.execute(post);
//            cookies=execute.getHeaders("Set-Cookie");
            String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
            result=unicodeDecode(result);
            return result;
        } catch (Exception e) {
            AutoLogger.log.error(e,e.fillInStackTrace());
            return e.fillInStackTrace().toString();
        }
    }


    //执行www-form-urlencoded格式的发包

    //json格式发包

    //文件上传multipart/form-data发包






}
