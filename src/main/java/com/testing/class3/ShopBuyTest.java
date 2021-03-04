package com.testing.class3;

import com.testing.web.WebKeyword;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Set;

/**
 * @Classname ShopBuyTest
 * @Description 类型说明
 * @Date 2021/1/12 20:04
 * @Created by 特斯汀Roy
 */


public class ShopBuyTest {

    public static String inputUser="//input[@id='username']";

    public static void main(String[] args) {
        WebKeyword web =new WebKeyword();
        web.openBrowser("chrome");
        visitShop(web);
        //1、登录
        shopLogin(web);
        //2、购买商品
        shopBuy(web);

        web.closeBrowser();

    }

    public static void visitShop(WebKeyword web) {
        web.visitURL("http://www.testingedu.com.cn:8000/");
    }

    public static void shopBuy(WebKeyword web) {
        //2、商城首页找到手机数码中的手机通讯进行点击。
        Actions actions=new Actions(web.driver);
        actions.moveToElement(web.getDriver().findElement(By.xpath("//a[text()='手机数码']"))).perform();
        //
//        actions.dragAndDropBy(web.getDriver().findElement(By.xpath("//span[text()='我的购物车']")),300,600).perform();
//        actions.clickAndHold(web.getDriver().findElement(By.xpath("//span[text()='我的购物车']"))).moveByOffset(600,300).perform();

        //2.2通过执行js来完成对手机通讯的点击。
//        JavascriptExecutor runner=(JavascriptExecutor)web.getDriver();
//        //2.2.1通过css，使用document.querySelector()方法定位，并且点击。
////        runner.executeScript("document.querySelector(\"dl.clearfix a\").click()");
//
//        //2.2.2 通过jsexecutor，执行的时候，传递可变参数列表，通过js脚本中arguments[0]来调用参数列表中的内容。
//        WebElement mobilePhone=web.getDriver().findElement(By.xpath("//a[text()='手机通讯' and @target]"));
//        String result = runner.executeScript("return arguments[0].innerText", mobilePhone).toString();
//        System.out.println(result);

        //打开新窗口之前，获取窗口的句柄信息，句柄就是一个字符串。
        String homepage = web.getDriver().getWindowHandle();
        System.out.println("首页的句柄是"+homepage);


        web.click("//a[text()='手机通讯' and @target]");
        Set<String> windowHandles = web.getDriver().getWindowHandles();
        System.out.println("所有窗口的句柄是："+windowHandles);

        //通过设置一个计数器，来计算当前句柄是set中的第几个，从而完成切换。
        int count=0;
        for(String window:windowHandles){
            count++;
            if(count==2){
                System.out.println("这是第二个句柄"+window);
                web.getDriver().switchTo().window(window);
            }
        }

        web.halt("2");

        //切换回之前的句柄。
        Set<String> newHandles=web.getDriver().getWindowHandles();
        System.out.println("切换窗口之后的所有句柄："+newHandles);
        //切回之前的窗口
        web.getDriver().switchTo().window(homepage);

        web.halt("3");

        //基于页面标题来判断相应句柄进行切换。
        for(String handle:newHandles){
            //先切换到当前handle的窗口
            web.getDriver().switchTo().window(handle);
            //获取到窗口的标题
            String title = web.getDriver().getTitle();
            //如果标题和想要切的窗口标题相等那么就不再进行后续循环，直接break退出。
            if("商品列表".equals(title)){
                break;
            }
        }

        //3、点击第五个商品的加入购物车。
        web.click("//div[@class='shop-list-splb p']/ul/li[5]//a[text()='加入购物车']");

        //4、进入商品详情页面，点击加入购物车
        web.click("//a[@id='join_cart']");
        //5、结算
//        web.getDriver().switchTo().frame("layui-layer-iframe1");
        web.getDriver().switchTo().frame(web.getDriver().findElement(By.id("layui-layer-iframe1")));
        web.halt("2");
        web.click("//a[text()='去购物车结算']");
        web.halt("3");
        //结算页面点击去结算
        //滚动页面，按绝对位置滚动
        web.runJs("window.scrollTo(0,600)");
        //将某个元素滚动到出现在页面中。
        web.runJsWithElement("scrollIntoView()","//a[text()='加入购物车']");

        web.halt("3");
    }

    public static void shopLogin(WebKeyword web) {
        web.click("//a[text()='登录']");
        web.input("//input[@id='username']","13800138006");
        web.input("//input[@id='password']","123456");
        web.input("//input[@id='verify_code']","1");
        web.click("//a[@name='sbtbutton']");
        web.click("//a[text()='返回商城首页']");
    }
}
