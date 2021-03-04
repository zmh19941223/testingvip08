package com.testing.class15;

import com.testing.common.Encrypt;
import com.testing.inter.InterKw;

import java.net.URLEncoder;

/**
 * @Classname EncPwdTest
 * @Description 类型说明
 * @Date 2021/2/23 20:27
 * @Created by 特斯汀Roy
 */
public class EncPwdTest {
    public static void main(String[] args) {
        InterKw roy=new InterKw();
        //调用auth接口获取token
        roy.doPostUrl("http://192.168.95.135:8080/Inter/HTTP/auth","");
        roy.saveParam("tokenV","$.token");
        roy.addHeader("{\"roy\":\"test\",\"token\":\"{tokenV}\"}");
        roy.saveRandomParam("userR","kaka");
        roy.saveConstant("pwd","kk123456");
        //注册
        roy.doPostUrl("http://192.168.95.135:8080/Inter/HTTP//register","username={userR}&pwd={pwd}&nickname=teacher&describe=tester");
        roy.saveEncPwd("encPwd","{pwd}");
//        System.out.println(roy.paramMap);
        roy.doPostUrl("http://192.168.95.135:8080/Inter/HTTP/login", "username={userR}&password={encPwd}");
        roy.saveParam("idV","$.userid");
        roy.doPostUrl("http://192.168.95.135:8080/Inter/HTTP/getUserInfo","id={idV}");
        roy.doPostUrl("http://192.168.95.135:8080/Inter/HTTP/logout","");
    }
}
