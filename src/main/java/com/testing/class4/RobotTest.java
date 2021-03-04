package com.testing.class4;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @Classname RobotTest
 * @Description 类型说明
 * @Date 2021/1/14 21:37
 * @Created by 特斯汀Roy
 */
public class RobotTest {
    public static void main(String[] args) throws AWTException {
        Robot rb=new Robot();
        //移动到指定位置
        rb.mouseMove(989,238);
        //点击鼠标左键
        rb.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
        //松开鼠标左键
        rb.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
        //按下键盘4
        rb.keyPress(KeyEvent.VK_4);
        rb.keyRelease(KeyEvent.VK_4);
        rb.delay(2000);
        //按下键盘退格
        rb.keyPress(KeyEvent.VK_BACK_SPACE);
        rb.keyRelease(KeyEvent.VK_BACK_SPACE);
    }

}
