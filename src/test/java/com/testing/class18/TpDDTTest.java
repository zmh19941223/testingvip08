package com.testing.class18;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * @Classname TpDDTTest
 * @Description 类型说明
 * @Date 2021/3/2 22:06
 * @Created by 特斯汀Roy
 */
public class TpDDTTest {
    public static WebDriver driver;

    @BeforeSuite
    public void openBrowser(){
        System.setProperty("webdriver.chrome.driver","webDriverExe/chromedriver.exe");
        driver =new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    //一条访问电商，进行登录
    @Test(priority = 1,dataProvider = "logindata")
    public void login(String loginName,String password){
        driver.get("http://www.testingedu.com.cn:8000");
        driver.findElement(By.xpath("//a[text()='登录']")).click();
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys(loginName);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@id='verify_code']")).sendKeys("1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//a[@name='sbtbutton']")).click();
//        driver.findElement(By.xpath("//a[text()='返回商城首页']")).click();
    }

    @DataProvider
    public Object[][] logindata(){
        return new Object[][]{
                {"aka","123456"},
                {"a^","123456"},
                {"royyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy","123456"},
                {"13800138006","123456"}
        };
    }

    @AfterSuite
    public void closeBrowser(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
    }
}
