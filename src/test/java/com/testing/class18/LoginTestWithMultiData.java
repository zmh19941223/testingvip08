package com.testing.class18;

import com.testing.common.ExcelReader;
import com.testing.common.ExcelWriter;
import com.testing.web.DDTofWeb;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginTestWithMultiData {
	public DDTofWeb web;
	public ExcelReader caseExcel;
	public ExcelWriter resultExcel;

	//由于dataprovider自动完成参数逐行传递，所以用到行号来进行当前行的指定
	@Test(dataProvider = "dp")
	public void f(String rowNo, String loginname, String password, String verifycode) throws Exception {
		System.out.println(rowNo + loginname + password + verifycode);
		//rowNo转换为int型，指定操作的行数
		int No = 0;
		No = Integer.parseInt(rowNo);
		web.setLine(No);
		web.openBrowser("chrome");
		web.visitURL("http://www.testingedu.com.cn:8000/Home/user/login.html");
		web.halt("3");
		web.input("//input[@id='username']", loginname);
		web.input("//input[@id='password']", password);
		web.input("//input[@placeholder='验证码']", verifycode);
		web.click("//a[@name='sbtbutton']");
		web.closeBrowser();
	}

	@BeforeSuite
	public void initiallize() {
		//设置当前的日期保存成为excel结果文件的名字
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		String createdate = sdf.format(date);
		String rootpath = System.getProperty("user.dir");
		caseExcel = new ExcelReader(rootpath + "\\Cases\\TestNGLoginTest.xlsx");
		resultExcel = new ExcelWriter(rootpath + "\\Cases\\TestNGLoginTest.xlsx",
				rootpath + "\\Cases\\result-" + createdate + "TestNGLogin");
		web = new DDTofWeb(resultExcel);
	}

	@AfterSuite
	public void afterMethod() {
		caseExcel.close();
		resultExcel.save();
	}

	//把excel文件读取成为二维数组类型。
	@DataProvider
	public Object[][] dp() {
		return caseExcel.readAsMatrix();
	}
}
