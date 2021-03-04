package com.testing.inter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.testing.common.AutoLogger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname HttpClientUtils
 * @Description 类型说明
 * @Date 2021/2/2 21:47
 * @Created by 特斯汀Roy
 */
public class HttpClientUtils {

    //client对象，用于发包。
    public CloseableHttpClient client;

    //成员变量cookiestore，用于存储cookie，并且方便进行client之间的传递。
    BasicCookieStore cookieStore;

    //存储头域的headerMap
    public Map<String,String> headerMap;

    //实例化过程中，完成cookiestore和headerMap的实例化操作。
    public HttpClientUtils(){
        cookieStore=new BasicCookieStore();
        headerMap=new HashMap<String,String>();
    }

    //成员变量 isUseCookie，用于决定是否使用cookiestore
    boolean isUseCookie=true;

    //成原变量isUseHeader,用于决定是否需要添加头域
    boolean isUseHeader=true;

    //unicode解码方法
    public static String unicodeDecode(String origin){
        //需要找到字符串中的 u4位16进制数的格式，进行匹配。
        //用正则表达式进行查找匹配。
        //由于替换的时候只需要用到4位数字来匹配字符，所以加上() 表示是一个分组进行获取。
        Pattern uni=Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher m=uni.matcher(origin);
        //创建stringbuffer对象，声明长度为原本的字符串长度。
        StringBuffer stringBuffer=new StringBuffer(origin.length());
        while (m.find()){
            m.appendReplacement(stringBuffer,Character.toString((char)Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    /**
     * SSLcontext用于绕过ssl验证，使发包的方法能够对https的接口进行请求。
     */
    private static SSLContext createIgnoreVerifySSL() {

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSLv3");
            sc.init(null, new TrustManager[] { trustManager }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sc;
    }

    //实例化client的方法，创建使用cookieStore的client
    public CloseableHttpClient createCookieclient(){
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        //如果要使用cookiestore，就在创建时带上setdefaultcookieStore方法。
        client= HttpClients.custom().setConnectionManager(connManager).setDefaultCookieStore(cookieStore).build();
        return client;
    }

    //实例化client的方法，创建不使用cookieStore的client
    public CloseableHttpClient createNocookieClient(){
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        client=HttpClients.custom().setConnectionManager(connManager).build();
        return client;
    }

    //创建client的方法，判断isUsecookie的状态，分别创建使用/不使用cookiestore的client对象。
    public CloseableHttpClient createclient(){
        // 采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext)).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        if(isUseCookie){
            client= HttpClients.custom().setConnectionManager(connManager).setDefaultCookieStore(cookieStore).build();
        }else{
            client=HttpClients.custom().setConnectionManager(connManager).build();
        }
        return client;
    }

    //get请求，返回结果
    //使用传参useCookie的方式决定创建时是否使用cookiestore。
    public String doget(String url,String useCookie){
        //基于输入参数，决定是否调用cookie。
        if(useCookie.equals("use")){
            createCookieclient();
        }
        else{
            createNocookieClient();
        }
        HttpGet get=new HttpGet(url);
        //是否添加头域
        if(isUseHeader){
            //遍历map，将所有头都加进去
            for (String headerKey : headerMap.keySet()) {
                //添加头域方法
                get.addHeader(headerKey,headerMap.get(headerKey));
            }
        }
        try {
            //返回结果
            CloseableHttpResponse execute = client.execute(get);
            //从返回包中获取头域，set-cookie
            String result = EntityUtils.toString(execute.getEntity(),"UTF-8");
            //unicode 转码
            result=unicodeDecode(result);
            return result;
        } catch (IOException e) {
            //报错信息作为返回结果。
            AutoLogger.log.error(e,e.fillInStackTrace());
            return e.fillInStackTrace().toString();
        }
    }

    //将是否使用cookie交给createClient方法判断执行。
    public String doget(String url){
        createclient();
        HttpGet get=new HttpGet(url);
        if(isUseHeader){
            //遍历map，将所有头都加进去
            for (String headerKey : headerMap.keySet()) {
                //添加头域方法
                get.addHeader(headerKey,headerMap.get(headerKey));
            }
        }
        try {
            //返回结果
            CloseableHttpResponse execute = client.execute(get);
            //从返回包中获取头域，set-cookie
            String result = EntityUtils.toString(execute.getEntity(),"UTF-8");
            //unicode 转码
            result=unicodeDecode(result);
            return result;
        } catch (IOException e) {
            //报错信息作为返回结果。
            AutoLogger.log.error(e,e.fillInStackTrace());
            return e.fillInStackTrace().toString();
        }
    }

    //post
    //输入参数，来指定不同content-type
    public String doPost(String url,String contentType,String param){
        createclient();
        HttpPost post=new HttpPost(url);
        // 设置连接的超时时间
        // setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
        RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
        post.setConfig(config);
        //是否添加头域
        if(isUseHeader){
            //遍历map，将所有头都加进去
            for (String headerKey : headerMap.keySet()) {
                //添加头域方法
                post.addHeader(headerKey,headerMap.get(headerKey));
            }
        }
        try {
            HttpEntity postParam=null;
            //根据contenttype，拼接请求体
            switch (contentType){
                case "urlencoded":
                    StringEntity urlen=new StringEntity(param,"utf-8");
                    urlen.setContentType("application/x-www-form-urlencoded");
                    urlen.setContentEncoding("UTF-8");
                    //向上转型。
                    postParam=urlen;
                    break;
                case "json":
                    StringEntity jsonEn=new StringEntity(param,"utf-8");
                    jsonEn.setContentType("application/x-www-form-urlencoded");
                    jsonEn.setContentEncoding("UTF-8");
                    postParam=jsonEn;
                    break;
                case "file":
                    //基于设计好的规则，解析json中的内容    { "text":{"键":"值"},"bin":{"键":"值"} }
                    MultipartEntityBuilder meb=MultipartEntityBuilder.create();
                    JSONObject jsonObject = JSON.parseObject(param);
                    //如果text不为空，则遍历解析所有text内容，进行添加
                    if (jsonObject.get("text")!=null){
                        for (String key : jsonObject.getJSONObject("text").keySet()) {
                            //遍历text中的每一个键值对，添加为textbody
                            meb.addTextBody(key,jsonObject.getJSONObject("text").getString(key));
                        }
                    }
                    //如果bin不为空，则解析所有bin内容添加
                    if(jsonObject.get("bin")!=null){
                        for (String key : jsonObject.getJSONObject("bin").keySet()) {
                            meb.addBinaryBody(key,new File(jsonObject.getJSONObject("bin").getString(key)));
                        }
                    }
                    postParam=meb.build();
                    break;
            }
            post.setEntity(postParam);
            CloseableHttpResponse execute = client.execute(post);
            String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
            result=unicodeDecode(result);
            return result;
        } catch (Exception e) {
            AutoLogger.log.error(e,e.fillInStackTrace());
            return e.fillInStackTrace().toString();
        }
    }


    /**
     * 通过httpclient实现的以x-www-form-urlencoded格式传参的post方法
     * 重载的doPostUrl方法，没有传递头域参数列表。
     * @param url   接口的url地址
     * @param param 接口的参数列表。
     */
    public String doPostUrl(String url, String param) {
        // 创建httpclient对象
        CloseableHttpClient client = createclient();
        // result为最终返回结果
        String result = "";
        try {
            // 创建post方式请求对象
            HttpPost post = new HttpPost(url);
            // 设置连接的超时时间
            // setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
            RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
            post.setConfig(config);
            //是否添加头域
            if(isUseHeader){
                //遍历map，将所有头都加进去
                for (String headerKey : headerMap.keySet()) {
                    //添加头域方法
                    post.addHeader(headerKey,headerMap.get(headerKey));
                }
            }
            // 创建urlencoded格式的请求实体，设置编码为utf8
            StringEntity postParams = new StringEntity(param,"utf-8");
            postParams.setContentType("application/x-www-form-urlencoded");
            postParams.setContentEncoding("UTF-8");
            // 添加请求体到post请求中
            post.setEntity(postParams);
            // 执行请求操作，并获取返回包
            CloseableHttpResponse response = client.execute(post);
            // 获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换返回实体为String类型
                result = EntityUtils.toString(entity, "UTF-8");
            }
            // 对结果中可能出现的unicode编码进行转换，转成中文
            result = unicodeDecode(result);
            // 释放返回实体
            EntityUtils.consume(entity);
            // 关闭返回包
            response.close();
        } catch (Exception e) {
            // 当出现报错时，将报错信息记录作为结果。
            AutoLogger.log.error(e, e.fillInStackTrace());
            result = e.fillInStackTrace().toString();
        } finally {
            // 关闭httpclient对象，释放资源
            try {
                client.close();
            } catch (IOException e) {
                AutoLogger.log.error(e, e.fillInStackTrace());
            }
        }
        return result;
    }

    /**
     *  使用json格式传参的post方法
     * @param url
     * @param param
     * @return
     */
    public String doPostJson(String url,String param){
        // 创建httpclient对象
        CloseableHttpClient client = createclient();
        // result为最终返回结果
        String result = "";
        try {
            // 创建post方式请求对象
            HttpPost post = new HttpPost(url);
            // 设置连接的超时时间
            // setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
            RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
            post.setConfig(config);
            //是否添加头域
            if(isUseHeader){
                //遍历map，将所有头都加进去
                for (String headerKey : headerMap.keySet()) {
                    //添加头域方法
                    post.addHeader(headerKey,headerMap.get(headerKey));
                }
            }
            // 创建urlencoded格式的请求实体，设置编码为utf8
            StringEntity postParams = new StringEntity(param,"utf-8");
            postParams.setContentType("application/json");
            postParams.setContentEncoding("UTF-8");
            // 添加请求体到post请求中
            post.setEntity(postParams);

            // 执行请求操作，并获取返回包
            CloseableHttpResponse response = client.execute(post);
            // 获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换返回实体为String类型
                result = EntityUtils.toString(entity, "UTF-8");
            }
            // 对结果中可能出现的unicode编码进行转换，转成中文
            result = unicodeDecode(result);
            // 释放返回实体
            EntityUtils.consume(entity);
            // 关闭返回包
            response.close();
        } catch (Exception e) {
            // 当出现报错时，将报错信息记录作为结果。
            AutoLogger.log.error(e, e.fillInStackTrace());
            result = e.fillInStackTrace().toString();
        } finally {
            // 关闭httpclient对象，释放资源
            try {
                client.close();
            } catch (IOException e) {
                AutoLogger.log.error(e, e.fillInStackTrace());
            }
        }
        return result;
    }

    //实现soap接口的发送。
    public String doPostSoap(String url,String param){
        // 创建httpclient对象
        CloseableHttpClient client = createclient();
        // result为最终返回结果
        String result = "";
        try {
            // 创建post方式请求对象
            HttpPost post = new HttpPost(url);
            // 设置连接的超时时间
            // setsocketTImeout指定收发包过程中的超时上线是15秒，connectTime指定和服务器建立连接，还没有发包时的超时上限为10秒。
            RequestConfig config = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(10000).build();
            post.setConfig(config);
            //是否添加头域
            if(isUseHeader){
                //遍历map，将所有头都加进去
                for (String headerKey : headerMap.keySet()) {
                    //添加头域方法
                    post.addHeader(headerKey,headerMap.get(headerKey));
                }
            }
            // 创建urlencoded格式的请求实体，设置编码为utf8
            StringEntity postParams = new StringEntity(param,"UTF-8");
            postParams.setContentType("text/xml");
            postParams.setContentEncoding("UTF-8");
            // 添加请求体到post请求中
            post.setEntity(postParams);

            // 执行请求操作，并获取返回包
            CloseableHttpResponse response = client.execute(post);
            // 获取结果实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换返回实体为String类型
                result = EntityUtils.toString(entity, "UTF-8");
            }
            // 对结果中可能出现的unicode编码进行转换，转成中文
            result = unicodeDecode(result);
            //针对soap的xml格式，需要的是<return></return>元素中的内容，编写正则 <return>(.*?)</return> 来进行获取
            Pattern returnResult=Pattern.compile("<return>(.*?)</return>");
            Matcher m=returnResult.matcher(result);
            if(m.find()){
                result=m.group(1);
            }
            // 释放返回实体
            EntityUtils.consume(entity);
            // 关闭返回包
            response.close();
        } catch (Exception e) {
            // 当出现报错时，将报错信息记录作为结果。
            AutoLogger.log.error(e, e.fillInStackTrace());
            result = e.fillInStackTrace().toString();
        } finally {
            // 关闭httpclient对象，释放资源
            try {
                client.close();
            } catch (IOException e) {
                AutoLogger.log.error(e, e.fillInStackTrace());
            }
        }
        return result;

    }

    //usecookie方法，将isUseCookie字段置为true，从而在创建client的时候知道如何创建
    public void useCookie(){
        isUseCookie=true;
    }

    public void notUseCookie(){
        isUseCookie=false;
    }

    public void clearCookie(){
        isUseCookie=false;
        //通过重新实例化，完成清理操作。
        cookieStore=new BasicCookieStore();
    }


    //useHeader方法，将isUseCookie字段置为true，从而在各个方法请求的时候，决定是否添加header。
    public void useHeader(){
        isUseHeader=true;
    }

    public void notUseHeader(){
        isUseHeader=false;
    }

    //将json内容转换为map进行存储，方便进行调用
    public void addHeader(String headerjson){
        try {
            //解析字符串为json格式
            JSONObject header=JSON.parseObject(headerjson);
            //将json格式的内容遍历存储到map里面。
            for (String key : header.keySet()) {
                headerMap.put(key,header.getString(key));
            }
            //加了头域，那么肯定是要用的
            useHeader();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //清理已存储的头信息
    public  void clearHeader(){
        //重新实例化，将headerMap清空
        headerMap=new HashMap<>();
        notUseHeader();
    }



}
