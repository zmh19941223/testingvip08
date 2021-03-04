package com.testing.class18;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.common.Report;
import com.testing.inter.DDTOfInter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InterExcelDriven {

	public DDTOfInter inter;
	public ExcelReader caseExcel;
	public ExcelWriter resultExcel;
	public String createdate;
	public String rootpath;

	@Test(dataProvider = "keywords")
	public void excelDriven(String rowNo, String caseName, String keywords, String param1, String param2,
			String assertMethod, String JsonPath, String expectedResult, String caseResult, String response) {
		// 输出所有读取出来的内容
		System.out.println(rowNo + caseName + keywords + param1 + param2 + assertMethod + JsonPath + expectedResult);
		// 如果关键字没有内容，那么不执行任何操作。
		if (keywords.equals("")) {
			;
		} else {
			// 指定当前执行的行号，让脚本能够向正在操作的这一行写入结果和返回信息。
			int No = 0;
			No = Integer.parseInt(rowNo);
			inter.setLine(No);
			runHttp(keywords, param1, param2, assertMethod, JsonPath, expectedResult);
		}
	}

	@DataProvider
	public Object[][] keywords() {
		return caseExcel.readAsMatrix();
	}

	// 测试开始前的准备工作
	@BeforeSuite
	public void beforeSuite() {
		// 读取当前的时间，保存为一个字符串。
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		createdate = sdf.format(date);
		// 打开excel文件进行读取和写入操作。
		rootpath = System.getProperty("user.dir");
		// 打开用例文件
		caseExcel = new ExcelReader(rootpath + "\\Cases\\TestNGHTTPLogin.xlsx");
		// 基于用例文件输出的结果文件。
		resultExcel = new ExcelWriter(rootpath + "\\Cases\\TestNGHTTPLogin.xlsx",
				rootpath + "\\Cases\\Result\\result-" + createdate + "TestNGHTTPLogin");
		// 关键字实现
		inter = new DDTOfInter(resultExcel);
	}

	// 通过switch映射driver和
	private void runHttp(String keywords, String param1, String param2, String assertMethod, String JsonPath,
			String expectedResult) {
		// TODO Auto-generated method stub
		// 执行关键字相应操作
		try {
//			 通过Excel表中填写的关键字判断调用哪个方法执行
			switch (keywords) {
			case "post":
				inter.doPostUrl(param1, param2);
				break;
			case "savecookie":
				inter.useCookie();
				break;
			case "clearcookie":
				inter.notUseCookie();
				break;
			case "addHeader":
				inter.addHeader(param1);
				break;
			case "clearHeader":
				inter.clearHeader();
				break;
			case "saveParam":
				inter.saveParam(param1, param2);
				break;
			}
			// 通过excel表中填写的校验方法确定
			switch (assertMethod) {
			case "equal":
				inter.jsonValueCheck(JsonPath, expectedResult);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// aftersuite方法，完成脚本执行完成之后的环境清理工作。
	@AfterSuite
	public void afterSuite() {
		caseExcel.close();
		resultExcel.save();
	}

}
