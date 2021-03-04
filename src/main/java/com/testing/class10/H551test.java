package com.testing.class10;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @Classname H551test
 * @Description 类型说明
 * @Date 2021/1/28 20:26
 * @Created by 特斯汀Roy
 */
public class H551test {

    public static void main(String[] args) {

        //1、启动appium服务端
        //获取当前项目的文件路径。
        String rootpath=System.getProperty("user.dir");
        System.out.println(rootpath);
        String logpath=rootpath+"/logs/appium.log";
        System.out.println(logpath);
        //在脚本中执行cmd命令。

        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd /c start appium -p 50111 -g " + logpath + " --local-timezone --log-timestamp ");
            //由于启动要时间，所以最好加个等待，避免客户端连接通信的时候，服务端还没起起来。
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //2、客户端连接服务端完成应用启动和测试。
        //启动手机应用进行测试
        //1、设置使用的desired capabilities
        DesiredCapabilities qqcap = new DesiredCapabilities();
        qqcap.setCapability("platformName", "Android");
        qqcap.setCapability("platformVersion", "5.1.1");
        qqcap.setCapability("deviceName", "127.0.0.1:62001");
        qqcap.setCapability("appPackage", "com.android.browser");
        qqcap.setCapability("appActivity", ".BrowserActivity");
        qqcap.setCapability("noReset", true);
        qqcap.setCapability("chromedriverExecutable", "E:\\AutoTools\\chromedriver\\chromedriver7403729\\chromedriver.exe");


        //2、指定连接的服务器地址。
        try {
            URL appiumServerurl = new URL("http://127.0.0.1:50111/wd/hub");

            AndroidDriver driver=new AndroidDriver(appiumServerurl,qqcap);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            //点击了浏览器的url输入框之后，再输入search事件
            driver.findElement(By.id("com.android.browser:id/url")).click();
            driver.findElement(By.id("com.android.browser:id/url")).sendKeys("https://www.51job.com");
            Thread.sleep(3000);
            driver.pressKey(new KeyEvent(AndroidKey.ENTER));

            driver.findElement(By.xpath("//android.view.View[contains(@text,'附近职位')]")).click();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
