package com.testing.class5.pageAdmin;

import com.testing.web.WebKeyword;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddGoodsPage {
	//kw中，操作的时候，是传递元素定位表达式的，
	//在pageFactory的PO模式中，已经把所有元素都写成了WebElement成员变量，并不适用。
	//在这里使用，是为了使用其中的driver来操作浏览器。
	public WebKeyword kw;
	//页面使用的url
	public String Url="http://www.testingedu.com.cn:8000/index.php/Admin/Index/index";
	//声明所有页面中用到的元素，作为类中的变量。
	//将@FindBy注解通过对应的定位方法找到的元素赋值给成员变量
	@FindBy(xpath = "//a[text()='商城']")
	public WebElement shopMenu;
	
	@FindBy(xpath = "//iframe[@id='workspace']")
	public WebElement workSpaceIframe;
	
	@FindBy(xpath = "//div[@title='添加商品']")
	public WebElement addGoodsBtn;
	
	@FindBy(xpath = "//input[@name='goods_name']")
	public WebElement GoodsName;
	
	@FindBy(xpath = "//*[@id='cat_id']")
	public WebElement goodsCat1;
	
	@FindBy(xpath = "//*[@id='cat_id_2']")
	public WebElement goodsCat2;
	
	@FindBy(xpath = "//*[@id='cat_id_3']")
	public WebElement goodsCat3;
	
	@FindBy(xpath = "//input[@name='shop_price']")
	public WebElement shopPrice;
	
	@FindBy(xpath = "//input[@name='market_price']")
	public WebElement marketPrice;
	
	@FindBy(xpath = "//*[@id='is_free_shipping_label_1']")
	public WebElement freeShipping;
	
	@FindBy(xpath = "//a[@id='submit']")
	public WebElement submitBtn;
	
	public AddGoodsPage(WebKeyword keyword) {
		kw=keyword;
	}
	
	public void load() {
		kw.visitURL(Url);
		//使用selenium的pageFactory，完成元素的初始化
		PageFactory.initElements(kw.driver, this);
	}
	
	public void addGoods() {
		shopMenu.click();
		kw.driver.switchTo().frame(workSpaceIframe);
		addGoodsBtn.click();
		GoodsName.sendKeys("Roy老师");
		Select cat1=new Select(goodsCat1);
		cat1.selectByValue("52");
		kw.halt("1");
//		Select cat2=new Select(goodsCat2);
//		cat2.selectByValue("54");
//		kw.halt("1");
//		Select cat3=new Select(goodsCat3);
//		cat3.selectByValue("374");
//		kw.halt("1");
		shopPrice.sendKeys("500");
		marketPrice.sendKeys("5000");
		freeShipping.click();
		submitBtn.click();
	}
	
}
