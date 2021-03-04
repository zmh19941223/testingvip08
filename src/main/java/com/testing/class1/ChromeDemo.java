package com.testing.class1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Classname ChromeDemo
 * @Description 类型说明
 * @Date 2021/1/7 21:22
 * @Created by 特斯汀Roy
 */
public class ChromeDemo {

    public static WebDriver driver;

    public static void main(String[] args) {
        //chrome浏览器驱动
        //相当于设置临时环境变量，设置系统参数，指定webdriver的位置。
//        System.setProperty("webdriver.chrome.driver","E:\\AutoTools\\chromedriver\\87\\chromedriver.exe");
//        //实例化webdriver对象，向上转型。
        driver=new ChromeDriver();
////        WebDriver driver1=new ChromeDriver();

//        GoogleDriver gg=new GoogleDriver("E:\\AutoTools\\chromedriver\\87\\chromedriver.exe");
//        driver=gg.getdriver();

//        FFDriver ff=new FFDriver("C:\\Program Files\\Mozilla Firefox\\firefox.exe","webDriverExe\\geckodriver.exe");
//        driver = ff.getdriver();

        //火狐
        //把webDriver可以放到项目路径下，通过相对路径来进行访问，方便项目进行移植供他人使用。
        //如果火狐没有装在默认路径下，通过webdriver.firefox.bin来进行指定,如果是在默认路径下，就不用指定。
//        System.setProperty("webdriver.firefox.bin","C:\\Program Files\\Mozilla Firefox\\firefox.exe");
//        System.setProperty("webdriver.gecko.driver","webDriverExe\\geckodriver.exe");
//        driver=new FirefoxDriver();

        //IE
//        System.setProperty("webdriver.ie.driver","webDriverExe\\IEDriverServer.exe");
//        WebDriver driver=new InternetExplorerDriver();

        //Edge
//        System.setProperty("webdriver.edge.driver","webDriverExe\\msedgedriver.exe");
//        WebDriver driver=new EdgeDriver();

        searchBaidu("特斯汀","特斯汀_百度搜索");
        //close其实是关闭当前浏览器窗口
//        driver.close();
        //退出浏览器，并且关闭driver进程。
        driver.quit();

    }

    public static void searchBaidu(String content,String expect) {
        //1、访问百度
        driver.get("https://www.baidu.com/");
        //navigate方法，可以调用 back forward to 分别完成浏览器后退前进和访问。
        driver.navigate().to("https://www.51job.com/");
        driver.navigate().back();
        //2、输入框中输入特斯汀
        //2.1 找到输入框
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='kw']"));
        //2.2在输入框中输入特斯汀。
        searchInput.sendKeys(content);
        //3、执行搜索
        //点击百度一下按钮
        driver.findElement(By.cssSelector("#su")).click();

        //4、验证结果是否正确， 断言。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //1、验证标题是否是特斯汀_百度搜索
        String title = driver.getTitle();
        System.out.println("当前页面标题是"+title);
        if(expect.equals(title)){
            System.out.println("测试成功");
        }
        else{
            System.out.println("测试失败");
        }

        //2、验证url地址中是否包含特斯汀
        String currentUrl = driver.getCurrentUrl();
        System.out.println("当前url地址是"+currentUrl);
        try {
            //使用java中的urlencoder，对字符进行url编码转换
            String 特斯汀 = URLEncoder.encode("特斯汀", "UTF-8");
            System.out.println("转换后的编码是"+特斯汀);
            System.out.println(currentUrl.contains(特斯汀));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //3、获取完整的页面源码内容
        String pageSource = driver.getPageSource();
        System.out.println(pageSource.contains("特斯汀"));

        //4、判断某个元素的内容，是预期值。
        //判断第二条搜索记录的文本内容，包含知乎
        //第二条搜索记录，这个业务逻辑，通过定位表达式来进行表达。
        String text = driver.findElement(By.xpath("//div[@id='content_left']/div[2]/h3/a")).getText();
        System.out.println(text);
        String innerText=driver.findElement(By.xpath("//div[@id='content_left']/div[2]/h3/a")).getAttribute("innerText");
        System.out.println("innerText是"+innerText);

        //5、判断元素的属性
        String href = driver.findElement(By.xpath("//div[@id='content_left']/div[2]/h3/a")).getAttribute("href");
        System.out.println(href);


        driver.findElement(By.xpath("//a[text()='百度首页']")).click();


    }


}
