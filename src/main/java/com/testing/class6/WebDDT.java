package com.testing.class6;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.web.WebKeyword;

import java.util.List;

/**
 * @Classname WebDDT
 * @Description 类型说明
 * @Date 2021/1/19 21:54
 * @Created by 特斯汀Roy
 */
public class WebDDT {

    public static final int KEYCOL = 3;

    public static final int RESULTCOL = 10;

    public static void main(String[] args) {

        WebKeyword web = new WebKeyword();
        ExcelReader casefile = new ExcelReader("Cases/WebCases.xlsx");
        ExcelWriter resultfile = new ExcelWriter("Cases/WebCases.xlsx", "Cases/Result/resultOfWeb" + web.createTime("MMdd+HH：mm：ss"));
        for (int sheetNO = 0; sheetNO < casefile.getTotalSheetNo(); sheetNO++) {
            //指定使用当前的sheet页。
            casefile.useSheetByIndex(sheetNO);
            resultfile.useSheetByIndex(sheetNO);
            System.out.println("当前的sheet页是" + casefile.getSheetName(sheetNO));
            for (int rowNo = 0; rowNo < casefile.getRowNo(); rowNo++) {
                //每一行读取对应的内容
                List<String> rowContent = casefile.readLine(rowNo);
                //基于每行内容，执行测试用例
                switch (rowContent.get(KEYCOL)) {
                    case "打开浏览器":
                        web.openBrowser(rowContent.get(KEYCOL + 1));
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "visitWeb":
                        web.visitURL(rowContent.get(KEYCOL + 1));
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "input":
                        boolean input = web.input(rowContent.get(KEYCOL + 1), rowContent.get(KEYCOL + 2));
                        if (input) {
                            resultfile.writeCell(rowNo, RESULTCOL, "PASS");
                        } else {
                            resultfile.writeFailCell(rowNo, RESULTCOL, "FAIL");
                        }
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "click":
                        boolean click = web.click(rowContent.get(KEYCOL + 1));
                        if (click) {
                            resultfile.writeCell(rowNo, RESULTCOL, "PASS");
                        } else {
                            resultfile.writeFailCell(rowNo, RESULTCOL, "FAIL");
                        }
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "switchIframebyele":
                    case "intoIframe":
                        boolean iframe=web.switchIframe(rowContent.get(KEYCOL + 1));
                        if (iframe) {
                            resultfile.writeCell(rowNo, RESULTCOL, "PASS");
                        } else {
                            resultfile.writeFailCell(rowNo, RESULTCOL, "FAIL");
                        }
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "halt":
                        web.halt(rowContent.get(KEYCOL + 1));
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "selectByValue":
                        web.selectByValue(rowContent.get(KEYCOL + 1), rowContent.get(KEYCOL + 2));
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                    case "assertEleTextContains":
                        boolean result = web.assertText(rowContent.get(KEYCOL + 1), rowContent.get(KEYCOL + 2), rowContent.get(KEYCOL + 3));
                        if (result) {
                            resultfile.writeCell(rowNo, RESULTCOL, "PASS");
                        } else {
                            resultfile.writeFailCell(rowNo, RESULTCOL, "FAIL");
                        }
                        break;
                    case "closeBrowser":
                        web.closeBrowser();
//                        resultfile.writeCell(rowNo,RESULTCOL,"PASS");
                        break;
                }
            }
        }
        //释放资源，保存结果文件
        resultfile.save();
        casefile.close();

    }
}
