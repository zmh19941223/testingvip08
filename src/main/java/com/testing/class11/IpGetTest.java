package com.testing.class11;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.xmlbeans.impl.regex.Match;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname IpGetTest
 * @Description 类型说明
 * @Date 2021/1/30 21:20
 * @Created by 特斯汀Roy
 */
public class IpGetTest {

    public static void main(String[] args) {
        //创建httpclient对象，作为发送请求的客户端
        CloseableHttpClient roy= HttpClients.createDefault();

        //拼接构造请求包
        //四大要素：  url  http方法  请求头  请求体
        HttpGet ipget=new HttpGet("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=3.3.3.3&co=&resource_id=5809&t=1612267942405&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110208873521310280592_1612267928253&_=1612267928259");
        try {
            CloseableHttpResponse ipResponse = roy.execute(ipget);
            System.out.println("返回包的结果是："+ipResponse);
            //获取返回体中的实际内容：
            HttpEntity ipEntity = ipResponse.getEntity();
            System.out.println("状态码是"+ipResponse.getStatusLine().getStatusCode());
            System.out.println("返回体是"+ipEntity);
            String ipResult = EntityUtils.toString(ipEntity, "UTF-8");
            System.out.println("返回结果是"+ipResult);
            //转换unicode编码
            ipResult=UnicodeDecoder.unicodeDecode(ipResult);

            //断言，其实就是对返回体字符串来进行处理
            if(ipResult.contains("美国")){
                System.out.println("测试成功");
            }
            else{
                System.out.println("测试失败");
            }

            //1、正则表达式，根据制定的规则提取需要的内容。
            Pattern p=Pattern.compile("\\((.*?)\\)");
            Matcher m=p.matcher(ipResult);
            m.find();
            //得到标准的字符串json格式
            String ipjson=m.group(1);

            //2.1直接逐层解析获取结果信息中location键的值
            JSONObject jsonObject = JSON.parseObject(ipjson);
            //获取第一层，也就是result，是一个数组
            JSONArray result = jsonObject.getJSONArray("Result");
            //数组的元素需要取下标。
            JSONObject resultjson= result.getJSONObject(0);
            System.out.println("result的内容是"+resultjson);
            //子json的内容直接逐层取值就可以了
            String location = resultjson.getJSONObject("DisplayData").getJSONObject("resultData").getJSONObject("tplData").get("location").toString();
            System.out.println("fastjson逐层解析的结果是："+location);

            //2.2jsonpath解析：也要基于字符串是个标准的json格式了。
            String jsonpathResult = JSONPath.read(ipjson, "$.Result[0].DisplayData.resultData.tplData.location").toString();
            System.out.println("jsonPath解析出来的结果是："+jsonpathResult);

            //2.3正则表达式，根据制定的规则提取需要的内容。
            Pattern locationPattern=Pattern.compile("\"location\":\"(.*?)\",");
            Matcher matcher=locationPattern.matcher(ipResult);
            matcher.find();
            String locationregex=matcher.group(1);
            System.out.println("通过正则表达式直接获取"+locationregex);

//            //断言，其实就是对返回体字符串来进行处理
//            if(ipResult.contains("\\u7f8e\\u56fd \\u4e9a\\u9a6c\\u900a\\u4e91")){
//                System.out.println("测试成功");
//            }
//            else{
//                System.out.println("测试失败");
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
