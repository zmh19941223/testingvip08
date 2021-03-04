package com.testing.class3;

import com.testing.web.WebKeyword;

/**
 * @Classname ShopBuyKeyWord
 * @Description 类型说明
 * @Date 2021/1/12 21:16
 * @Created by 特斯汀Roy
 */
public class ShopBuyKeyWord {
    public static void main(String[] args) {
        WebKeyword web =new WebKeyword();
        web.openBrowser("chrome");
        web.visitURL("http://www.testingedu.com.cn:8000/");
        //1、登录
        web.click("//a[text()='登录']");
        web.input("//input[@id='username']","13800138006");
        web.input("//input[@id='password']","123456");
        web.input("//input[@id='verify_code']","1");
        web.click("//a[@name='sbtbutton']");
        web.click("//a[text()='地址管理']");
        web.click("//span[text()='增加新地址']");
        web.click("//select[@id='province']");
        web.halt("1");
        web.runJsWithElement("scrollIntoView()","//option[text()='甘肃省']");
//        web.click("//option[text()='甘肃省']");
        web.runJsWithElement("click()","//option[text()='甘肃省']");
//        web.click("//a[text()='返回商城首页']");
//
//        //2、商城首页找到手机数码中的手机通讯进行点击。
//        web.hover("//a[text()='手机数码']");
//        web.click("//a[text()='手机通讯' and @target]");


    }
}
