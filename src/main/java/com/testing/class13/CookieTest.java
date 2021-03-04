package com.testing.class13;

import com.testing.common.AutoLogger;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Classname CookieTest
 * @Description 类型说明
 * @Date 2021/2/26 15:13
 * @Created by 特斯汀Roy
 */
public class CookieTest {
    public static void main(String[] args) throws Exception {

        CloseableHttpClient roy= HttpClients.createDefault();
        HttpPost post=new HttpPost("http://www.testingedu.com.cn:8000/index.php?m=Home&c=User&a=do_login&t=0.672859533627163");
        StringEntity entity=new StringEntity("username=13800138006&password=123456&verify_code=1","UTF-8");
        entity.setContentType("application/x-www-form-urlencoded");
        entity.setContentEncoding("UTF-8");
        post.setEntity(entity);
        CloseableHttpResponse response = roy.execute(post);
        AutoLogger.log.info("--------------------------------------------下面是Cookie-----------------------");
        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header header : headers) {
            System.out.println(header.getName()+"::"+header.getValue());

        }
        AutoLogger.log.info("--------------------------------------------结束-----------------------");
        String login = EntityUtils.toString(response.getEntity(), "UTF-8");
        AutoLogger.log.info(login);


        CloseableHttpClient will= HttpClients.createDefault();
        HttpGet cart=new HttpGet("http://www.testingedu.com.cn:8000/index.php?m=Home&c=Cart&a=header_cart_list");
        String cookieV="";
        for (Header header : headers) {
                cookieV+= header.getValue().substring(0, header.getValue().indexOf(";"))+";";
                System.out.println("拼接后的cookie值"+cookieV);
        }
        cart.addHeader("Cookie", cookieV);
        String cartresult = EntityUtils.toString(will.execute(cart).getEntity(), "UTF-8");
        AutoLogger.log.info(cartresult);
    }
}
