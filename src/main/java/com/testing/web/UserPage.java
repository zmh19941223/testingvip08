package com.testing.web;

/**
 * @Classname UserPage
 * @Description 类型说明
 * @Date 2021/1/15 11:29
 * @Created by 特斯汀Roy
 */
public class UserPage extends WebKeyword{

    public void loginUser(){
        click("//a[text()='登录']");
        input("//input[@id='username']","13800138006");
        input("//input[@id='password']","123456");
        input("//input[@id='verify_code']","1");
        click("//a[@name='sbtbutton']");
        click("//a[text()='返回商城首页']");
    }
}
