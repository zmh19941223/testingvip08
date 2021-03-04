package com.testing.class5;

import com.testing.common.AutoLogger;
import com.testing.class5.pageAdmin.AddGoodsPage;
import com.testing.class5.pageAdmin.AdminLoginPage;
import com.testing.class5.pageShop.HomePage;
import com.testing.class5.pageShop.LoginPage;
import com.testing.web.WebKeyword;

/**
 * @Classname TestSeleniumPOFactory
 * @Description 类型说明
 * @Date 2021/1/16 22:02
 * @Created by 特斯汀Roy
 */
public class TestSeleniumPOFactory {
    public static void main(String[] args) {
        WebKeyword web=new WebKeyword();
        web.openBrowser("chrome");
        AutoLogger.log.info("---------------执行后台登录界面测试----------------------");
        AdminLoginPage adminLoginPage=new AdminLoginPage(web.getDriver());
        adminLoginPage.load();
        adminLoginPage.login();
        AutoLogger.log.info("---------------执行后台商品添加测试----------------------");
        AddGoodsPage addGoodsPage=new AddGoodsPage(web);
        addGoodsPage.load();
        addGoodsPage.addGoods();
        AutoLogger.log.info("---------------执行用户端登录测试----------------------");
        LoginPage loginPage=new LoginPage(web);
        loginPage.load();
        loginPage.login();
        AutoLogger.log.info("---------------执行用户端登录测试----------------------");
        HomePage homePage=new HomePage(web);
        homePage.load();
        homePage.joinCart();
        web.closeBrowser();



    }
}
