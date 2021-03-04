package com.testing.inter;

import com.alibaba.fastjson.JSONPath;
import com.testing.common.AutoLogger;
import com.testing.common.Encrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname InterKw
 * @Description 接口自动化测试的关键字, 包括断言等和请求发包无关的操作。
 * @Date 2021/2/4 20:15
 * @Created by 特斯汀Roy
 */
public class InterKw {
    //参数存储的map
    public Map<String, String> paramMap;

    //由于返回结果是所有方法都要使用的，所以设为成员变量
    public String responseResult;

    //成员变量httpclientUtils
    public HttpClientUtils httpClientUtils;


    //构造方法完成实例化
    public InterKw() {
        paramMap = new HashMap<>();
        httpClientUtils = new HttpClientUtils();
    }

    //有可能牵涉到使用参数，所以先进行替换
    public void addHeader(String headerJson) {
        //先替换变量参数
        headerJson = useParam(headerJson);
        httpClientUtils.addHeader(headerJson);
    }

    public void clearHeader(){
        httpClientUtils.clearHeader();
    }

    public void useCookie(){
        httpClientUtils.useCookie();
    }

    public void notUseCookie(){
        httpClientUtils.notUseCookie();
    }

    public void doGet(String url){
        try {
            url=useParam(url);
            responseResult= httpClientUtils.doget(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //url编码格式post请求
    public void doPostUrl(String url, String param) {
        //可能含有变量，先替换
        url = useParam(url);
        param = useParam(param);
        responseResult = httpClientUtils.doPostUrl(url, param);
        AutoLogger.log.info(responseResult);
    }
    //xml格式post请求
    public void doPostSoap(String url, String param) {
        //可能含有变量，先替换
        url = useParam(url);
        param = useParam(param);
        responseResult = httpClientUtils.doPostSoap(url, param);
        AutoLogger.log.info(responseResult);
    }


    //解析json格式的返回，获取参数值，存到对应键
    public void saveParam(String paramKey, String jsonPath) {
        String paramValue = JSONPath.read(responseResult, jsonPath).toString();
        paramMap.put(paramKey, paramValue);
    }

    //进行加密，得到加密后的密码字符串，存到paramMap供之后使用
    public void saveEncPwd(String paramKey, String originString) {
        //调用加密算法，对输入内容进行加密，得到加密后的字符串，作为参数值。
        Encrypt enc = new Encrypt();
        String paramValue = enc.enCrypt(useParam(originString));
        //将参数存储到结果中。
        paramMap.put(paramKey, paramValue);
    }

    //存储随机生成的参数。
    public void saveRandomParam(String paramKey, String originString) {
        //基于输入内容，拼接一个时间戳。
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String timeStamp = sdf.format(now);
        String paramValue = originString + timeStamp;
        //将参数存储到paramMap中。
        paramMap.put(paramKey, paramValue);
    }

    //存储固定的之后要用的参数
    public void saveConstant(String paramKey, String paramValue) {
        paramMap.put(paramKey, paramValue);
    }

    //将字符串中的变量名替换为它在parammap中存储的变量值
    public String useParam(String origin) {
        //遍历parammap里的每个键，在origin字符串中找{key}替换它。
        for (String key : paramMap.keySet()) {
            //替换的键的格式是{key},replaceAll用的是正则，所以要转义{
            //每一轮循环，会替换这一轮的key键值对，替换完之后，把原字符串修改完再进行下一次的循环。
            origin = origin.replaceAll("\\{" + key + "\\}", paramMap.get(key));
        }
        return origin;
    }

    /**
     * 断言返回结果通过正则表达式获取其中部分内容之后，是否和预期结果一致。
     *
     * @param regex  断言时使用的正则表达式
     * @param expect 预期结果
     * @return
     */
    public boolean assertRegexEq(String regex, String expect) {
        //正则表达式解析想要的结果。
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(responseResult);
        String result = "";
        if (m.find()) {
            result = m.group(1);
        }
        AutoLogger.log.info("提取后的结果是" + result);
        if (result.equals(expect)) {
            AutoLogger.log.info("pass");
            return true;
        }
        return false;
    }

    /**
     * 断言json解析结果是否符合预期。
     *
     * @param jsonpath jsonpath表达式
     * @param expected 预期结果
     * @return
     */
    public  boolean jsonValueCheck(String jsonpath, String expected) {
        String jsonValue = JSONPath.read(responseResult, jsonpath).toString();
        if (jsonValue.equals(expected)) {
            AutoLogger.log.info("解析结果是" + jsonValue);
            return true;
        }
        return false;
    }



}
