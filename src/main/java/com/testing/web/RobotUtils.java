package com.testing.web;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @Classname RobotUtils
 * @Description 类型说明
 * @Date 2021/1/14 21:45
 * @Created by 特斯汀Roy
 */
public class RobotUtils {

    Robot rb;

    public RobotUtils(){
        try {
            rb=new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void moveToclick(int x,int y){
        rb.mouseMove(x,y);
        rb.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
        rb.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
        rb.delay(500);
    }

}
