package com.testing.class9;

import com.testing.app.AppKeyword;

/**
 * @Classname QQkeyWordTest
 * @Description 类型说明
 * @Date 2021/1/26 20:47
 * @Created by 特斯汀Roy
 */
public class QQkeyWordTest {
    public static void main(String[] args) {
        AppKeyword app=new AppKeyword();
        app.startAppiumServer("50111","10");
        app.runAPP("127.0.0.1:62001","5.1.1","com.tencent.mobileqq",".activity.SplashActivity","50111","5");
        //点击搜索，选择之前搜索记录。
        app.click("//android.widget.EditText[@content-desc=\"搜索\"]");
        app.click("//android.widget.TextView[@text=\"青鸿\"]");
        //点击设置
        app.click("//android.widget.ImageView[@content-desc=\"聊天设置\"]");
        //点击用户名
        app.halt("5");
        app.click("//android.widget.TextView[@text=\"青鸿\"]");
        app.click("//android.widget.TextView[@text=\"青鸿的空间\"]");

        //等待空间的加载
        app.halt("15");

        app.adbSwipe("522","1400","522","200","1000");

        app.halt("2");
        //点赞
        app.multiByclick("id","com.tencent.mobileqq:id/c7p");

        //点击评论
        app.multiByclick("xpath","//*[@resource-id='com.tencent.mobileqq:id/c7j']");

        //发送评论
        app.input("//android.widget.EditText[@resource-id='com.tencent.mobileqq:id/h_0']","棒棒哒");

        app.click("//android.widget.Button[@text='发送']");

        app.quitApp();
        app.killAppium();



    }

}
