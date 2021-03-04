package com.testing.class13;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;

/**
 * @Classname VIP08LoginTest
 * @Description 类型说明
 * @Date 2021/2/4 21:22
 * @Created by 特斯汀Roy
 */
public class VIP08LoginTest {


    public static void main(String[] args) throws Exception {
        //创建一个公用的cookiestore,类似于存储cookie的钱包
        BasicCookieStore purse=new BasicCookieStore();
        //第一次请求，roy带上purse去请求，把cookie装到purse里面
        CloseableHttpClient roy=HttpClients.custom().setDefaultCookieStore(purse).build();
        HttpPost login=new HttpPost("http://localhost:8080/VIP08Login/Login?username=Will&password=123456");
        StringEntity loginParam=new StringEntity("username=Will&password=123456");
        loginParam.setContentType("application/x-www-form-urlencoded");
        loginParam.setContentEncoding("UTF-8");
        login.setEntity(loginParam);
        CloseableHttpResponse execute = roy.execute(login);
        String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
        System.out.println("第一次登录的结果是"+result);
        //遍历cookie池purse中的所有cookie，输出他们的键值、作用域。
        for (Cookie cookie : purse.getCookies()) {
            System.out.println(cookie.getName()+cookie.getValue()+cookie.getDomain());
        }

        //进行第二次请求
        //将cookiestore传递给第二个client来进行使用
        CloseableHttpClient royswife=HttpClients.custom().setDefaultCookieStore(purse).build();
        HttpPost login1=new HttpPost("http://localhost:8080/VIP08Login/Login");
        StringEntity loginParam1=new StringEntity("username=Will&password=123456");
        loginParam1.setContentType("application/x-www-form-urlencoded");
        loginParam1.setContentEncoding("UTF-8");
        login1.setEntity(loginParam1);
        CloseableHttpResponse execute1 = royswife.execute(login1);
        String result1 = EntityUtils.toString(execute1.getEntity(), "UTF-8");
        System.out.println("第二次登录的结果："+result1);

        //进行第三次请求
        //不使用已经保存的cookiestore
        CloseableHttpClient will=HttpClients.custom().build();
        HttpPost login2=new HttpPost("http://localhost:8080/VIP08Login/Login");
        StringEntity loginParam2=new StringEntity("username=Will&password=123456");
        loginParam2.setContentType("application/x-www-form-urlencoded");
        loginParam2.setContentEncoding("UTF-8");
        login2.setEntity(loginParam2);
        CloseableHttpResponse execute2 = will.execute(login2);
        String result2 = EntityUtils.toString(execute2.getEntity(), "UTF-8");
        System.out.println("第三次登录的结果："+result2);

    }

    /**
     * 手动添加cookie的操作方式。
     * @param args
     * @throws Exception
     */
    public static void main3(String[] args) throws Exception{
        //两次请求用不同的client来进行请求，通过自己添加cookie的方式来进行操作。
        CloseableHttpClient client= HttpClients.createDefault();
        HttpPost login=new HttpPost("http://localhost:8080/VIP08Login/Login");
        StringEntity loginParam=new StringEntity("username=Will&password=123456");
        loginParam.setContentType("application/x-www-form-urlencoded");
        loginParam.setContentEncoding("UTF-8");
        login.setEntity(loginParam);
        //获取到返回包
        CloseableHttpResponse execute = client.execute(login);
        //获取返回头 Set-cookie
        Header[] headers = execute.getHeaders("Set-Cookie");
        //格式是  Set-Cookie: JSESSIONID=988A9C44D8155C6A0D65588D263FC2FB; Max-Age=120; Expires=Thu, 04-Feb-2021 13:47:38 GMT
        String value="";
        for (Header header : headers) {
            System.out.println("头域的键值对是："+header.getName()+"值是"+header.getValue());
            value = header.getValue();
            //获取分号之前的内容：
            value = value.substring(0, value.indexOf(";"));
            System.out.println("取出来的结果是"+value);
        }

        String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
        System.out.println("第一次登录的结果是"+result);

        //如果用相同的client进行执行，那么client会保存之前请求的cookie，并且在之后的请求中带上。
        HttpPost login1=new HttpPost("http://localhost:8080/VIP08Login/Login");
        StringEntity loginParam1=new StringEntity("username=Will&password=123456");
        loginParam1.setContentType("application/x-www-form-urlencoded");
        loginParam1.setContentEncoding("UTF-8");
        login1.setEntity(loginParam1);
        //添加Cookie头域，携带Cookie,格式是：  Cookie: JSESSIONID=E2EBFFA797351D7CC127F6BDEB98E8F8
        login1.addHeader("Cookie",value);
        CloseableHttpResponse execute1 = client.execute(login1);
        String result1 = EntityUtils.toString(execute1.getEntity(), "UTF-8");
        System.out.println("第二次登录的结果："+result1);
    }



    public static void main2(String[] args) throws Exception {
        //两次请求用相同的client来进行执行
        CloseableHttpClient client= HttpClients.createDefault();
        HttpPost login=new HttpPost("http://localhost:8080/VIP08Login/Login?username=Will&password=123456");
        StringEntity loginParam=new StringEntity("username=Will&password=123456");
        loginParam.setContentType("application/x-www-form-urlencoded");
        loginParam.setContentEncoding("UTF-8");
        login.setEntity(loginParam);
        CloseableHttpResponse execute = client.execute(login);
        String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
        System.out.println("第一次登录的结果是"+result);

        //如果用相同的client进行执行，那么client会保存之前请求的cookie，并且在之后的请求中带上。
        HttpPost login1=new HttpPost("http://localhost:8080/VIP08Login/Login");
        StringEntity loginParam1=new StringEntity("username=Will&password=123456");
        loginParam1.setContentType("application/x-www-form-urlencoded");
        loginParam1.setContentEncoding("UTF-8");
        login1.setEntity(loginParam1);
        CloseableHttpResponse execute1 = client.execute(login1);
        String result1 = EntityUtils.toString(execute1.getEntity(), "UTF-8");
        System.out.println("第二次登录的结果："+result1);

    }


    public static void main1(String[] args) throws Exception {
        //两次请求用两个不同的client来进行执行
        CloseableHttpClient client= HttpClients.createDefault();
        HttpPost login=new HttpPost("http://localhost:8080/VIP08Login/Login?username=Will&password=123456");
        StringEntity loginParam=new StringEntity("username=Will&password=123456");
        loginParam.setContentType("application/x-www-form-urlencoded");
        loginParam.setContentEncoding("UTF-8");
        login.setEntity(loginParam);
        CloseableHttpResponse execute = client.execute(login);
        String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
        System.out.println("第一次登录的结果是"+result);

        //如果用不同的client进行登录，那么是不会携带cookie的，相当于两个不同的人去住酒店，两个人都得登记。
        CloseableHttpClient roy= HttpClients.createDefault();
        HttpPost login1=new HttpPost("http://localhost:8080/VIP08Login/Login");
        StringEntity loginParam1=new StringEntity("username=Will&password=123456");
        loginParam1.setContentType("application/x-www-form-urlencoded");
        loginParam1.setContentEncoding("UTF-8");
        login1.setEntity(loginParam1);
        CloseableHttpResponse execute1 = roy.execute(login1);
        String result1 = EntityUtils.toString(execute1.getEntity(), "UTF-8");
        System.out.println("第二次登录的结果："+result1);

    }

}
