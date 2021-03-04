package com.testing.class6;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.web.DDTofWeb;
import com.testing.web.WebKeyword;

import java.util.List;

/**
 * @Classname DDTwithWriter
 * @Description 类型说明
 * @Date 2021/1/19 22:38
 * @Created by 特斯汀Roy
 */
public class DDTwithWriter {

    public static final int KEYCOL=3;

    public static void main(String[] args) {
        WebKeyword tool=new WebKeyword();
        ExcelReader casefile = new ExcelReader("Cases/WebCases.xlsx");
        ExcelWriter resultfile = new ExcelWriter("Cases/WebCases.xlsx", "Cases/Result/resultOfWeb" + tool.createTime("MMdd+HH：mm：ss"));
        DDTofWeb web=new DDTofWeb(resultfile);
        for (int sheetNO = 0; sheetNO < casefile.getTotalSheetNo(); sheetNO++) {
            //指定使用当前的sheet页。
            casefile.useSheetByIndex(sheetNO);
            resultfile.useSheetByIndex(sheetNO);
            System.out.println("当前的sheet页是" + casefile.getSheetName(sheetNO));
            for (int rowNo = 0; rowNo < casefile.getRowNo(); rowNo++) {
                //每一行读取对应的内容
                List<String> rowContent = casefile.readLine(rowNo);
                //设置一下当前遍历的行号，好让关键字知道往哪写。
                web.setLine(rowNo);
                //基于每行内容，执行测试用例
                switch (rowContent.get(KEYCOL)) {
                    case "打开浏览器":
                        web.openBrowser(rowContent.get(KEYCOL + 1));
                        break;
                    case "visitWeb":
                        web.visitURL(rowContent.get(KEYCOL + 1));
                        break;
                    case "input":
                        boolean input = web.input(rowContent.get(KEYCOL + 1), rowContent.get(KEYCOL + 2));
                        break;
                    case "click":
                        boolean click = web.click(rowContent.get(KEYCOL + 1));
                        break;
                    case "switchIframebyele":
                    case "intoIframe":
                        boolean iframe=web.switchIframe(rowContent.get(KEYCOL + 1));
                        break;
                    case "halt":
                        web.halt(rowContent.get(KEYCOL + 1));
                        break;
                    case "selectByValue":
                        web.selectByValue(rowContent.get(KEYCOL + 1), rowContent.get(KEYCOL + 2));
                        break;
                    case "assertEleTextContains":
                        boolean result = web.assertText(rowContent.get(KEYCOL + 1), rowContent.get(KEYCOL + 2), rowContent.get(KEYCOL + 3));
                        break;
                    case "closeBrowser":
                        web.closeBrowser();
                        break;
                }
            }
        }
        //释放资源，保存结果文件
        resultfile.save();
        casefile.close();



    }
}
