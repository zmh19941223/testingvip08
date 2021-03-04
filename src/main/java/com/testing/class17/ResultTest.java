package com.testing.class17;

import com.testing.common.AutoLogger;
import com.testing.common.AutoTools;
import com.testing.common.ExcelResult;
import com.testing.common.Report;

import java.util.List;
import java.util.Map;

/**
 * @Classname ResultTest
 * @Description 类型说明
 * @Date 2021/2/27 21:17
 * @Created by 特斯汀Roy
 */
public class ResultTest {
    public static void main(String[] args) {
        ExcelResult re=new ExcelResult();
        String time = AutoTools.createTime("yyyyMMdd HH:mm:ss");
//        List<Map<String, String>> sumarry = re.Sumarry("Cases\\Result\\resultofInter-0225-221810.xlsx", time);
//        for (Map<String, String> stringStringMap : sumarry) {
//            System.out.println(stringStringMap);
//        }
//        String s = Report.makeReport(sumarry);
//        System.out.println(s);

        Report.sendreport("Cases\\Result\\resultofInter-0225-221810.xlsx",time);


    }

}
