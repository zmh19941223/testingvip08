package com.testing.class6;

import com.testing.web.WebKeyword;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PoiTest
 * @Description 类型说明
 * @Date 2021/1/19 20:27
 * @Created by 特斯汀Roy
 */
public class PoiTest {

    public static final int KEYCOL=3;

    public static void main(String[] args) throws IOException, InvalidFormatException {
        WebKeyword web=new WebKeyword();
        //打开xlsx文件。
        File excel=new File("Cases/WebCases.xlsx");
        //通过文件输入流，来打开excel文件
        FileInputStream inputStream=new FileInputStream(excel);
        //xssfworkbook对象对应与xlsx文件。
        //通过文件输入流作为参数，实例化workbook
//        Workbook workbook=new XSSFWorkbook(inputStream);
        //直接用文件，作为参数，实例化workbook
        Workbook workbook=new XSSFWorkbook(excel);
        System.out.println(workbook);

//        System.out.println(workbook.getSheetName(0));
//        System.out.println(workbook.getSheetName(1));
    //   调用超出范围的sheet，会报错。
    //   Exception in thread "main" java.lang.IllegalArgumentException: Sheet index (2) is out of range (0..1)
//        System.out.println(workbook.getSheetName(2));

        //读取excel中的所有行的内容。
        //1、遍历sheet页。
        for(int sheetNo=0;sheetNo<workbook.getNumberOfSheets();sheetNo++){
            System.out.println("当前打开的sheet页是："+workbook.getSheetName(sheetNo));
            //2、遍历sheet页中的每一行。
            //使用当前的sheet页。
            Sheet nowSheet=workbook.getSheetAt(sheetNo);
            //读取一行的操作：
//            Row row = nowSheet.getRow(3);
//            System.out.println(row);
//            Cell cell = row.getCell(3);
//            System.out.println(cell);

            //读取所有行：
            for(int rowNo=0;rowNo<nowSheet.getPhysicalNumberOfRows();rowNo++){
                //每一行存到一个list里面去，元素就是cell的内容。
                List<String> rowContent=new ArrayList<>();
                Row nowRow=nowSheet.getRow(rowNo);
                //3、遍历一行中的所有单元格
                for(int colNo=0;colNo<nowRow.getPhysicalNumberOfCells();colNo++){


                    Cell cell=nowRow.getCell(colNo);
                    String cellValue="";
                    if(cell.getCellType().equals(CellType.NUMERIC)){
                        cellValue = (long)(cell.getNumericCellValue())+"";
                    }else{
                        cellValue= cell.getStringCellValue();
                    }

            //  13800138006会变成科学技术法，所以数字需要处理一下      1.3800138006E10
//                     rowContent.add(cell.toString()+"格式是"+cell.getCellType());
                    rowContent.add(cellValue);
                }
                System.out.println("第"+rowNo+"行的内容是"+rowContent);
                //调用用例执行：基于关键字列进行判断，调用对应的方法。
                //千万记得break。
                switch(rowContent.get(KEYCOL)){
                    case "打开浏览器":
                        web.openBrowser(rowContent.get(KEYCOL+1));
                        break;
                    case "visitWeb":
                        web.visitURL(rowContent.get(KEYCOL+1));
                        break;
                    case "input":
                        web.input(rowContent.get(KEYCOL+1),rowContent.get(KEYCOL+2));
                        break;
                    case "click":
                        web.click(rowContent.get(KEYCOL+1));
                        break;
                    case "closeBrowser":
                        web.closeBrowser();
                        break;
                }

            }



        }



    }

}
