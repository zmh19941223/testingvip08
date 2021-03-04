package com.testing.class11;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname UnicodeDecoder
 * @Description 类型说明
 * @Date 2021/1/30 21:47
 * @Created by 特斯汀Roy
 */
public class UnicodeDecoder {
    public static void main(String[] args) {
        //unicode编码，其实是 u4位16进制数
        //转换16进制数为10进制
        int i = Integer.parseInt("7f8e", 16);
        //强转为char格式，因为java用的就是unicode编码，所以直接把数字转成char，就能得到unicode结果。
        System.out.println((char)i);

        String s = unicodeDecode("jQuery110206146068135530687_1612012452966({\"ResultCode\":\"0\",\"ResultNum\":\"1\",\"Result\":[{\"DisplayData\":{\"strategy\":{\"tempName\":\"ip\",\"precharge\":\"0\",\"ctplOrPhp\":\"1\"},\"resultData\":{\"tplData\":{\"srcid\":\"5809\",\"resourceid\":\"5809\",\"OriginQuery\":\"3.3.3.3\",\"origipquery\":\"3.3.3.3\",\"query\":\"3.3.3.3\",\"origip\":\"3.3.3.3\",\"location\":\"\\u7f8e\\u56fd \\u4e9a\\u9a6c\\u900a\\u4e91\",\"userip\":\"\",\"showlamp\":\"1\",\"tplt\":\"ip\",\"titlecont\":\"IP\\u5730\\u5740\\u67e5\\u8be2\",\"realurl\":\"http:\\/\\/www.ip138.com\\/\",\"showLikeShare\":\"1\",\"shareImage\":\"1\",\"data_source\":\"AE\"},\"extData\":{\"tplt\":\"ip\",\"resourceid\":\"5809\",\"OriginQuery\":\"3.3.3.3\"}}},\"ResultURL\":\"http:\\/\\/www.ip138.com\\/\",\"Weight\":\"2\",\"Sort\":\"1\",\"SrcID\":\"5809\",\"ClickNeed\":\"0\",\"SubResult\":[],\"SubResNum\":\"0\",\"ar_passthrough\":[],\"RecoverCacheTime\":\"0\"}],\"QueryID\":\"845666549\",\"Srcid\":\"5809\",\"status\":\"0\",\"data\":[{\"srcid\":\"5809\",\"resourceid\":\"5809\",\"OriginQuery\":\"3.3.3.3\",\"origipquery\":\"3.3.3.3\",\"query\":\"3.3.3.3\",\"origip\":\"3.3.3.3\",\"location\":\"\\u7f8e\\u56fd \\u4e9a\\u9a6c\\u900a\\u4e91\",\"userip\":\"\",\"showlamp\":\"1\",\"tplt\":\"ip\",\"titlecont\":\"IP\\u5730\\u5740\\u67e5\\u8be2\",\"realurl\":\"http:\\/\\/www.ip138.com\\/\",\"showLikeShare\":\"1\",\"shareImage\":\"1\"}]})");
        System.out.println(s);


    }

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
}
