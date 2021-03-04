package com.testing.class18;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.web.TestNGDDTOfWeb;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.testng.Assert.assertEquals;

public class WebExcelDriven {
	public TestNGDDTOfWeb web;
	public ExcelReader caseExcel;
	public ExcelWriter resultExcel;
	public String createdate;
	public String rootpath;
	
//通过数据指定的关键字运行，并且断言
  @Test(dataProvider = "keywords")
  public void webTest(String rowNo, String kong,String casename,String keywords,String param1,String param2,String param3,String k1,String k2,String k3,String k4 ) {
	  //读取excel返回的内容中的行数，作为当前正在操作的行
	  int No=0;
	  No=Integer.parseInt(rowNo);
	  web.line=No;
	  //行数和用例名称输出
	  System.out.println(rowNo+casename);
	  //通过java反射机制调用excel文件中指定的关键字方法。
	  String runRes=runUIWithInvoke(keywords, param1, param2, param3);
	  System.out.println(runRes);
	  //通过testng的断言机制来判断用例执行的结果是否是通过的。
	  assertEquals(runRes, "pass");
  }

  //读数据
  @DataProvider
  public Object[][] keywords() {
	  return caseExcel.readAsMatrix();
  }
  
  //完成excel文件的读取和关键字类的实例化
  @BeforeSuite
  public void beforeSuite() {
	  //获取当前的执行时间
      Date date = new Date();
      //将时间以设定的标准格式转存为一个字符串
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
      createdate = sdf.format(date);
      //指定文件路径打开excel用例文件，并且拷贝一份结果文件用于记录执行结果
      rootpath = System.getProperty("user.dir");
	  caseExcel = new ExcelReader(rootpath+"\\cases\\TestNGWebCases.xlsx");
	  resultExcel = new ExcelWriter(rootpath+"\\cases\\TestNGWebCases.xlsx", rootpath+"\\Cases\\result-"+createdate+"TestNGWebCases");
	  web =new TestNGDDTOfWeb(resultExcel);
  }
  //清理测试环境，保存excel结果，关闭excel在内存中的对象。
  @AfterSuite
  public void afterSuite() {
	  caseExcel.close();
	  resultExcel.save();
  }
  //需要调用的反射方式执行关键字的方法。
  private String runUIWithInvoke(String key,String param1,String param2,String param3) {
	  	String result ="fail";
		try {
			//基于参数名查找参数列表为空的方法。
			Method appMethod = web.getClass().getDeclaredMethod(key);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=appMethod.invoke(web).toString();
			return result;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(key, String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=uis.invoke(web, param1).toString();
			return result;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(key, String.class, String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=uis.invoke(web, param1, param2).toString();
			return result;
		} catch (Exception e) {
		}
		try {
			Method uis = web.getClass().getDeclaredMethod(key, String.class, String.class,
					String.class);
			// invoke语法，需要输入类名以及相应的方法用到的参数
			result=uis.invoke(web, param1, param2,param3).toString();
			return result;
		} catch (Exception e) {
		}
		return result;
	}
  
}
