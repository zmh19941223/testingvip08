package com.testing.class18;

import com.testing.common.AutoLogger;
import org.testng.annotations.*;

public class DemoNG {

    @BeforeGroups(groups = "roy")
    public void beforeGp(){
        System.out.println("分组之前执行的方法");
    }

    @AfterGroups(groups = "roy")
    public void afterGp(){
        System.out.println("分组之后执行的方法");
    }


    @BeforeMethod(groups = "roy")
    public void beforeMethod() {
        System.out.println("DemoNG类中@BeforeMethod方法被执行");
    }

    @AfterMethod(groups = "roy")
    public void afterMethod() {
        System.out.println("DemoNG类中@AfterMethod方法被执行");
    }

    @BeforeClass(groups = "roy")
    public void beforeClass() {
        System.out.println("DemoNG类中@beforeClass方法被执行");
    }

    @AfterClass(groups = "roy")
    public void afterClass() {
        System.out.println("DemoNG类中@afterClass方法被执行");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("DemoNG类中@beforeTest方法被执行");
    }

    @AfterTest
    public void afterTest() {
        System.out.println("DemoNG类中@afterTest方法被执行");
    }

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("DemoNG类中@beforeSuite方法被执行");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("DemoNG类中@afterSuite方法被执行");
    }

    @Test(groups = "roy")
    public void demo1() {
        AutoLogger.log.info("第一个测试方法");
    }

    @Test(groups = "will")
    public void demo2() {
        AutoLogger.log.info("第二个测试方法");
    }
}
