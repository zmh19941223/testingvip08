package com.testing.class18;

import org.testng.annotations.*;

/**
 * @Classname FirstTestNG
 * @Description 类型说明
 * @Date 2021/3/2 20:21
 * @Created by 特斯汀Roy
 */
public class FirstTestNG {


    @BeforeTest
    public void BeforeTest(){
        System.out.println("FirstTestNG类中的BeforeTest方法被执行");
    }

    @AfterTest
    public void AfterTest(){
        System.out.println("FirstTestNG类中的AfterTest方法被执行");
    }


    @BeforeClass
    public void beforeFirst(){
        System.out.println("FirstTestNG类中的BeforeClass方法被执行");
    }

    @AfterClass
    public void afterFirst(){
        System.out.println("FirstTestNG类中的AfterClass方法被执行");
    }

    @Test(groups = "roy")
    public void firstTest(){
        System.out.println("testng的第一个测试方法");
    }

    @Test
    public void secondTest(){
        System.out.println("testng的第二个测试方法");
    }

}
