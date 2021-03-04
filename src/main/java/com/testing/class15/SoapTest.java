package com.testing.class15;

import com.testing.inter.InterKw;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname SoapTest
 * @Description 类型说明
 * @Date 2021/2/23 21:55
 * @Created by 特斯汀Roy
 */
public class SoapTest {
    public static void main(String[] args) {
        InterKw roy=new InterKw();
        roy.doPostSoap("http://www.testingedu.com.cn:8081/inter/SOAP?wsdl","<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.testingedu.com/\"><soapenv:Header/><soapenv:Body><soap:auth></soap:auth></soapenv:Body></soapenv:Envelope>");
        roy.saveParam("tokenV","$.token");
        roy.addHeader("{\"token\":\"{tokenV}\"}");
//        roy.saveEncPwd("encPwd","123456");
        roy.doPostSoap("http://www.testingedu.com.cn:8081/inter/SOAP?wsdl","<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.testingedu.com/\"><soapenv:Header/><soapenv:Body><soap:register><arg0>Willasfd1</arg0><arg1>aasdfasdf</arg1><arg2>roy老师</arg2><arg3>asdf1阿斯蒂芬</arg3></soap:register></soapenv:Body></soapenv:Envelope>");




    }
}
