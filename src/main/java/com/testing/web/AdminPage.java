package com.testing.web;

import java.util.Random;

/**
 * @Classname AdminPage
 * @Description 类型说明
 * @Date 2021/1/15 11:29
 * @Created by 特斯汀Roy
 */
public class AdminPage extends WebKeyword{

    /**
     * 登录商城后台
     * @param user
     * @param pwd
     * @param verifyCode
     */
    public void LoginAdmin(String user,String pwd,String verifyCode){
        input("//input[@name='username']",user);
        input("//input[@name='password']",pwd);
        input("//input[@name='vertify']",verifyCode);
        click("//input[@name='submit']");
    }

    /**
     * 进入添加菜单
     */
    public void toAddMenu(){
        click("//a[text()='商城']");
        switchIframe("//iframe[@id='workspace']");
        click("//span[string()='添加商品']");
    }

    public void addRandomGoods(){
        Random random=new Random();
         input("//input[@name='goods_name']","VIP08商品"+random.nextInt(100));
//        web.select("//select[@id='cat_id']","text","服饰");
         click("//select[@id='cat_id']");
         click("//option[contains(text(),'服饰')]");
         halt("1");
//        web.select("//select[@id='cat_id_2']","value","23");
         click("//select[@id='cat_id_2']");
         click("//option[@value='23']");
         halt("1");
//        web.select("//select[@id='cat_id_3']","text","牛仔裤");
//        //尝试通过点击来选择下拉框
         click("//select[@id='cat_id_3']");
         click("//option[text()='牛仔裤']");
         halt("1");
         runJsWithElement("click()","//option[text()='牛仔裤']");

         input("//input[@name='shop_price']","300");
         input("//input[@name='market_price']","330");

        //点击选择上传
         click("//input[@id='button1']/following-sibling::input");
        //切换iframe
         switchIframe("//iframe[contains(@id,'layui')]");
        //1.3直接通过input元素输入内容即可。
        input("//div[text()='点击选择文件']/following-sibling::div/input","F:\\微信二维码.png");
         click("//div[text()='确定使用']");

        //切换iframe，回到workspace
//        web.switchIframe("//iframe[@id='workspace']");
         switchUpIframe();
         click("//label[text()='是否包邮']/../following-sibling::dd//label[text()='是']");


         switchIframe("//iframe[@id='ueditor_0']");
         runJs("document.getElementsByTagName(\"p\")[0].innerText=\"测试一下\"");
         input("//p","VIP08商品");

         switchUpIframe();

         click("//a[@id='submit']");
    }



}
