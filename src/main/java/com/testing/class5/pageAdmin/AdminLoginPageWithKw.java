package com.testing.class5.pageAdmin;

import com.testing.web.WebKeyword;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class AdminLoginPageWithKw {
	public WebKeyword kw;
	public String Url="http://www.testingedu.com.cn:8000/Admin/Admin/login";
//	@FindBy(xpath = "//input[@name='username']")
	@FindBy(name="username")
	public WebElement user;

	@FindBy(xpath = "//input[@name='password']")
	public WebElement password;

	@FindBy(xpath = "//*[@id='vertify']")
	public WebElement verifyCode;

	@FindBy(xpath = "//input[@name='submit']")
	public WebElement submitBtn;
	
	
	public AdminLoginPageWithKw(WebKeyword key) {
		kw=key;
		PageFactory.initElements(kw.driver, this);
	}

	public void load() {
		kw.visitURL(Url);

	}

	public void login() {
		user.sendKeys("admin");
		password.sendKeys("123456");
		verifyCode.sendKeys("1");
		submitBtn.click();
		kw.halt("2");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void login(String username,String pwd,String vCode) {
		user.sendKeys(username);
		password.sendKeys(pwd);
		verifyCode.sendKeys(vCode);
		submitBtn.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
