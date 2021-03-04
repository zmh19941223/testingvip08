package com.testing.class14;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.inter.HttpClientUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ParamTest
 * @Description 类型说明
 * @Date 2021/2/6 22:00
 * @Created by 特斯汀Roy
 */
public class ParamTest {

    public static Map<String,String> paramMap;

    public static void main(String[] args) {
        //完成对象实例化，包括cookiestore和headermap的实例化
        HttpClientUtils roy=new HttpClientUtils();
        paramMap=new HashMap<>();
        String authresult = roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/auth", "");
        AutoLogger.log.info(authresult);
        saveParam("tokenV",authresult,"$.token");

        String finish=useParam("{\"roy\":\"test\",\"token\":\"{tokenV}\"}");
        AutoLogger.log.info("替换之后的结果是"+finish);
        //添加头域
        roy.addHeader(finish);

        //login请求
        String loginResult = roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/login", "username=roys24&password=123456");
        AutoLogger.log.info(loginResult);

        //logout请求
        String logoutResult = roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/logout", "");
        AutoLogger.log.info(logoutResult);

    }

    //解析json格式的返回，获取参数值，存到对应键
    public static void saveParam(String paramKey,String result,String jsonPath){
        String paramValue = JSONPath.read(result, jsonPath).toString();
        paramMap.put(paramKey,paramValue);
    }

    //将字符串中的变量名替换为它在parammap中存储的变量值
    public static String useParam(String origin){
        //遍历parammap里的每个键，在origin字符串中找{key}替换它。
        for (String key : paramMap.keySet()) {
            //替换的键的格式是{key},replaceAll用的是正则，所以要转义{
            //每一轮循环，会替换这一轮的key键值对，替换完之后，把原字符串修改完再进行下一次的循环。
            origin=origin.replaceAll("\\{"+key+"\\}",paramMap.get(key));
        }
        return origin;
    }

}
