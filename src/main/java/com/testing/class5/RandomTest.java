package com.testing.class5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @Classname RandomTest
 * @Description 类型说明
 * @Date 2021/1/16 20:12
 * @Created by 特斯汀Roy
 */
public class RandomTest {
    public static void main(String[] args) {
//        Random random=new Random();
//        String[] availStr=new String[]{"roy","will","土匪","卡卡"};
//        int number=random.nextInt(availStr.length-1);
//        System.out.println("获取到的随机值是"+availStr[number]);

        //将当前时间转换为指定格式
        Date now=new Date();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        SimpleDateFormat goodsNum=new SimpleDateFormat("ddHHmm");
        String format = goodsNum.format(now);
        System.out.println(format);
    }
}
