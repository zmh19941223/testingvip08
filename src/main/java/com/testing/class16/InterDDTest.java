package com.testing.class16;

import com.testing.common.AutoLogger;
import com.testing.common.AutoTools;
import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.inter.DDTOfInter;
import com.testing.inter.InterKw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Classname InterDDTest
 * @Description 类型说明
 * @Date 2021/2/25 20:07
 * @Created by 特斯汀Roy
 */
public class InterDDTest {

    public static void main(String[] args) {
        ExcelReader cases=new ExcelReader("Cases/InterCases.xlsx");
        ExcelWriter results=new ExcelWriter("Cases/InterCases.xlsx","Cases/Result/resultofInter"+AutoTools.createTime("dd-HHmm"));
        //实例化关键字对象
        DDTOfInter inter=new DDTOfInter(results);
        //遍历sheet页
        for(int sheetNo=0;sheetNo<cases.getTotalSheetNo();sheetNo++){
            cases.useSheetByIndex(sheetNo);
            results.useSheetByIndex(sheetNo);
            AutoLogger.log.info("当前sheet页的名字是："+cases.getSheetName(sheetNo));
            //遍历每一行
            //第一行表头就不遍历了。
            for(int rowNo=1;rowNo<cases.getRowNo();rowNo++){
                //读取每一行的内容
                List<String> rowContent = cases.readLine(rowNo);
                //指定当前执行的操作行，以完成对于该行结果的写入
                inter.setLine(rowNo);
                //根据每一行中的关键字列，和断言列来进行关键字的调用和参数的传入。
                //通过反射机制，执行代码
                AutoTools.invokeI(inter,rowContent,3);
                //进行断言
                AutoTools.invokeI(inter,rowContent,7);
            }
        }
        cases.close();
        results.save();
    }


}
