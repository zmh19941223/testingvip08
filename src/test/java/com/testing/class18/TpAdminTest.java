package com.testing.class18;

import com.testing.web.WebKeyword;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @Classname TpAdminTest
 * @Description 类型说明
 * @Date 2021/3/2 21:36
 * @Created by 特斯汀Roy
 */
public class TpAdminTest {

    public WebKeyword web;

    @BeforeClass
    public void openBrowser(){
        web=new WebKeyword();
        web.driver=TpshopTest.driver;
//        web.openBrowser("chrome");
    }

    @Parameters({"baiduurl"})
    @Test(priority = 0)
    public void visitAdmin(String baidu){
        web.visitURL(baidu);
    }

    @Parameters({"password"})
    @Test(priority = 1)
    public void adminLogin(String password){
        web.input("//input[@name='username']","admin");
        web.input("//input[@name='password']",password);
        web.input("//input[@name='vertify']","1");
        web.click("//input[@name='submit']");
        Assert.assertTrue(web.assertPageContains("添加商品"));
    }

    @AfterClass
    public void closeBrowser(){
        web.closeBrowser();
    }

}
