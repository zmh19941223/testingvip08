package com.testing.class14;

import com.testing.inter.InterKw;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Classname InterTest
 * @Description 类型说明
 * @Date 2021/2/6 22:26
 * @Created by 特斯汀Roy
 */
public class InterTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        InterKw roy=new InterKw();
        roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/auth","");
        roy.saveParam("tokenV","$.token");
        roy.addHeader("{\"roy\":\"test\",\"token\":\"{tokenV}\"}");
        roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/login", "username=roys24&password=123456");
        roy.doPostUrl("http://www.testingedu.com.cn:8081/inter/HTTP/logout","");
        String encode = URLEncoder.encode("roy老师","utf8");
        System.out.println(encode);
    }
}
