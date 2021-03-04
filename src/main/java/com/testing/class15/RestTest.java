package com.testing.class15;

import com.testing.inter.InterKw;

/**
 * @Classname RestTest
 * @Description 类型说明
 * @Date 2021/2/23 21:26
 * @Created by 特斯汀Roy
 */
public class RestTest {

    public static void main(String[] args) {
        InterKw roy=new InterKw();
        roy.doPostUrl("http://192.168.95.135:8080/Inter/REST/auth","");
        roy.saveParam("tokenV","$.token");
        roy.addHeader("{\"token\":\"{tokenV}\"}");
        roy.saveRandomParam("userR","kaka");
        roy.saveConstant("pwd","kk123456");

        roy.doPostUrl("http://192.168.95.135:8080/Inter/REST/user/register","{\"username\":\"{userR}\",\"pwd\":\"{pwd}\",\"nickname\":\"kaka\",\"describe\":\"seller\"}");
        roy.doPostUrl("http://192.168.95.135:8080/Inter/REST/login/{userR}/{pwd}","");
        roy.saveParam("idV","$.userid");
        roy.doPostUrl("http://192.168.95.135:8080/Inter/REST/login/{idV}","");
        roy.doPostUrl("http://192.168.95.135:8080/Inter/REST/login","");

    }

}
