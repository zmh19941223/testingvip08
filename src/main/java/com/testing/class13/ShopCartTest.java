package com.testing.class13;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.inter.HttpClientUtils;
import com.testing.inter.HttpClientUtilsBakTest;
import com.testing.inter.InterKw;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname ShopCartTest
 * @Description 类型说明
 * @Date 2021/2/4 20:07
 * @Created by 特斯汀Roy
 */
public class ShopCartTest {

    public static void main(String[] args) {
        HttpClientUtilsBakTest roy=new HttpClientUtilsBakTest();
        InterKw interKw=new InterKw();
        roy.createclient();
        //未登录的时候请求购物车接口
        String cartResult = roy.doget("http://www.testingedu.com.cn:8000/index.php?m=Home&c=Cart&a=header_cart_list");
        AutoLogger.log.info("返回结果是"+cartResult);

//        //完成登录请求
        String loginResult = roy.doPost("http://www.testingedu.com.cn:8000/index.php?m=Home&c=User&a=do_login&t=0.672859533627163", "urlencoded", "username=13800138006&password=123456&verify_code=1");
        AutoLogger.log.info("登录的结果是："+loginResult);


        //登录之后，再执行购物车接口
        String cartResultafter = roy.doget("http://www.testingedu.com.cn:8000/index.php?m=Home&c=Cart&a=header_cart_list");
        AutoLogger.log.info("返回结果是"+cartResultafter);
        //正则表达式解析想要的结果。

    }

}
