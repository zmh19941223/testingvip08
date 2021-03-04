package com.testing.class2;

import com.testing.web.WebKeyword;

import java.util.List;
import java.util.Map;

/**
 * @Classname KeywordTest
 * @Description 类型说明
 * @Date 2021/1/10 20:41
 * @Created by 特斯汀Roy
 */
public class KeywordTest {
    public static void main(String[] args) {
        WebKeyword web=new WebKeyword();
        web.openBrowser("chrome");
        web.visitURL("https://www.baidu.com");
        web.input("//input[@id='kw']","特斯汀");
        web.halt("1");
        web.click("id","su");
        web.halt("1");
        //显式等待，等待元素内容变为指定值。
        web.exWaitTextToContains();
        //可变参数填0个。获取text内容，以及boolean结果为false
        Map<String, Boolean> textandresult = web.getText("//div[@id='content_left']/div[2]/h3/a");
        //可变参数填n个，和每个预期结果对比，得到最终断言结论。
        Map<String, Boolean> textandresult1 = web.getText("//div[@id='content_left']/div[2]/h3/a","哔哩哔哩","知乎","百度一下");
        System.out.println(textandresult);
        List<String> allText = web.getAllText("//div[@id='content_left']/div//h3/a");
        System.out.println("获取到所有的搜索结果是"+allText);
        web.halt("3");
        web.closeBrowser();
    }
}
