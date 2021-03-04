package com.testing.class5;

import com.testing.class3.ShopBuyTest;
import com.testing.class4.ShopAdminTest;
import com.testing.web.WebKeyword;

import java.io.IOException;

/**
 * @Classname RunAll
 * @Description 类型说明
 * @Date 2021/1/16 21:17
 * @Created by 特斯汀Roy
 */
public class RunAll {

    public static void main(String[] args) {
        //1、创建webkeyword对象，用于调用浏览器
        WebKeyword basepage=new WebKeyword();
        basepage.openBrowser("chrome");
        //2、浏览器执行用例时，操作的都是同一个浏览器，区别是对不同页面进行的方法调用不同。
        //基于浏览器完成admin页面中的方法调用操作。
        ShopAdminTest.visitAdmin(basepage);
        ShopAdminTest.adminLogin(basepage);
        ShopAdminTest.adminAdd(basepage);

        //基于basepage浏览器完成shop页面的方法调用操作
        ShopBuyTest.visitShop(basepage);
        ShopBuyTest.shopLogin(basepage);
        ShopBuyTest.shopBuy(basepage);
        basepage.closeBrowser();

    }

}
