package com.testing.class8;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.management.relation.RoleUnresolved;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Classname QQTest
 * @Description 类型说明
 * @Date 2021/1/23 22:20
 * @Created by 特斯汀Roy
 */
public class QQTest {

    public static void main(String[] args) {
        //1、启动appium服务端
        //获取当前项目的文件路径。
        String rootpath=System.getProperty("user.dir");
        System.out.println(rootpath);
        String logpath=rootpath+"/logs/appium.log";
        System.out.println(logpath);
        //在脚本中执行cmd命令。
        Runtime runtime= Runtime.getRuntime();
        try {
            runtime.exec("cmd /c start appium -p 50111 -g "+logpath+" --local-timezone --log-timestamp ");
            //由于启动要时间，所以最好加个等待，避免客户端连接通信的时候，服务端还没起起来。
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //2、客户端连接服务端完成应用启动和测试。
        //启动手机应用进行测试
        //1、设置使用的desired capabilities
        DesiredCapabilities qqcap=new DesiredCapabilities();
        qqcap.setCapability("platformName","Android");
        qqcap.setCapability("platformVersion","5.1.1");
        qqcap.setCapability("deviceName","127.0.0.1:62001");
        qqcap.setCapability("appPackage","com.tencent.mobileqq");
        qqcap.setCapability("appActivity",".activity.SplashActivity");
        qqcap.setCapability("noReset",true);


        //2、指定连接的服务器地址。
        try {
            URL appiumServerurl=new URL("http://127.0.0.1:50111/wd/hub");
            //Androiddriver对象才有findElementById方法
//            AndroidDriver driver=new AndroidDriver(appiumServerurl,qqcap);
//            MobileElement el1 = (MobileElement) driver.findElementById("com.tencent.mobileqq:id/kag");
//            el1.click();
//            MobileElement el2 = (MobileElement) driver.findElementById("com.tencent.mobileqq:id/et_search_keyword");
//            el2.sendKeys("roy");

            //WEebDriver对象，用findelement(BY.id())
            WebDriver driver=new AndroidDriver(appiumServerurl,qqcap);
            Thread.sleep(5000);
            //隐式等待
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

//            List<WebElement> elements = driver.findElements(By.cssSelector("[content-desc='搜索']"));
//            for (WebElement element : elements) {
//                System.out.println(element.getAttribute("content-desc"));
//            }

//            WebElement searchinput = driver.findElement(MobileBy.AccessibilityId("搜索"));
            driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").textContains(\"搜索\")")).click();

//            MobileElement el1 = (MobileElement) driver.findElement(By.id("com.tencent.mobileqq:id/kag"));
//            el1.click();
            Thread.sleep(5000);
            MobileElement el2 = (MobileElement) driver.findElement(By.id("com.tencent.mobileqq:id/et_search_keyword"));
            el2.clear();


            el2.sendKeys("roy");

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
