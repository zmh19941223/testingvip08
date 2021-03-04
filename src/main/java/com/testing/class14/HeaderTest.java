package com.testing.class14;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.inter.HttpClientUtils;

/**
 * @Classname HeaderTest
 * @Description 使用httpclientutils完成头域添加操作。
 * @Date 2021/2/6 21:45
 * @Created by 特斯汀Roy
 */
public class HeaderTest {
    public static void main(String[] args) {
        //完成对象实例化，包括cookiestore和headermap的实例化
        HttpClientUtils roy=new HttpClientUtils();
        String authresult = roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/auth", "");
        AutoLogger.log.info(authresult);
        //获取token值
        String tokenValue = JSONPath.read(authresult, "$.token").toString();
//        {"roy":"test","token":""}

        //添加头域
        roy.addHeader("{\"roy\":\"test\",\"token\":\""+tokenValue+"\"}");

        //login请求
        String loginResult = roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/login", "username=roys24&password=123456");
        AutoLogger.log.info(loginResult);

        roy.notUseHeader();
        //logout请求
        String logoutResult = roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/logout", "");
        AutoLogger.log.info(logoutResult);



    }
}
