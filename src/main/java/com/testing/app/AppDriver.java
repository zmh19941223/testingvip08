package com.testing.app;

import com.testing.common.AutoLogger;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * 启动app，实例化完成AndroidDriver,并且在其它地方使用。
 */
public class AppDriver {

    //成员变量driver
    public AndroidDriver driver;

    /***
     * 构造方法，完成driver的实例化
     */
    public AppDriver(String device, String version, String app, String activity, String appiumPort){
        //填写capabilities
        DesiredCapabilities cap=new DesiredCapabilities();
        //创建的时候的必填项。
        cap.setCapability("deviceName",device);
        cap.setCapability("platformName","Android");
        cap.setCapability("platformVersion",version);
        cap.setCapability("appPackage",app);
        cap.setCapability("appActivity",activity);
        //有用的半必填参数
        //不重置应用数据，也就是在这个参数生效时，应用一直保持原有登录状态。
        cap.setCapability("noReset",true);
        //允许输入中文
        cap.setCapability("unicodeKeyboard",true);
        cap.setCapability("resetKeyboard",true);
        //不对应用重新签名。
        cap.setCapability("noSign",true);

        //如果有多个设备连接的时候，使用udid来进行配置，其实和deviceName作用是一样的
        cap.setCapability("udid",device);
        cap.setCapability("skipServerInstallation",true);


        //填写需要连接的服务器
        try {
            URL serverURL = new URL("http://127.0.0.1:"+appiumPort+"/wd/hub");
            //实例化对象
            driver=new AndroidDriver(serverURL,cap);
            //隐式等待依然有效。
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            AutoLogger.log.error("app启动失败，请检查appium日志信息！");
        }

    }


    //返回成员变量进行调用。
    public AndroidDriver getDriver(){
        return this.driver;
    }
}
