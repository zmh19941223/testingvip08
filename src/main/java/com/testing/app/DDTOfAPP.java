package com.testing.app;

import com.google.common.io.Files;
import com.testing.common.AutoLogger;
import com.testing.common.ExcelWriter;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;


public class DDTOfAPP {
	// 声明成员变量driver，在所有的方法当中都需要调用
	public AndroidDriver driver;

	//excelwriter对象，用于每个方法执行的时候，完成结果的写入。
	public ExcelWriter results;
	//用于记录当前操作的行号
	public int writeLine;
	//记录写入的列，固定为10
	public static final int RES_COL = 10;
	public static final String PASS = "pass";
	public static final String FAIL = "fail";

	//写入成功
	public void setPass() {
		results.writeCell(writeLine, RES_COL, PASS);
	}
	//写入失败结果
	public void setFail(Exception e) {
		AutoLogger.log.error(e, e.fillInStackTrace());
		results.writeFailCell(writeLine, RES_COL, FAIL);
	}

	//设置当前操作的行号
	public void setLine(int line) {
		writeLine = line;
	}
	//构造方法，完成excelwriter对象的实例化
	public DDTOfAPP(ExcelWriter result) {
		results = result;
	}

	public AndroidDriver getDriver(){
		return driver;
	}

	// 强制等待
	public void halt(String time) {
		int t = 0;
		try {
			t = Integer.parseInt(time);
			Thread.sleep(t * 1000);
			setPass();
		} catch (Exception e) {
			setFail(e);
			e.printStackTrace();
		}
	}

	// 脚本执行CMD命令的函数
	public void runCmd(String str) {
		// 打开新的cmd窗口执行指定的dos命令，注意空格别丢了，否则拼出来的bat命令会连在一起。
		String cmd = "cmd /c start " + str;
		Runtime runtime = Runtime.getRuntime();
		try {
			AutoLogger.log.info("执行cmd命令:" + str);
			runtime.exec(cmd);
			setPass();
		} catch (Exception e) {
			setFail(e);
			AutoLogger.log.error("cmd命令执行失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	// 通过cmd启动appium的服务
	public void startAppiumServer(String port, String time) {
		// 启动appium的服务端
		AutoLogger.log.info("在"+port+"端口启动appiumserver服务");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		// 当前时间的字符串
		String createdate = sdf.format(date);
		// 拼接文件名，形式为：工作目录路径+执行时间+AppiumLog.txt
		String appiumLogFile = "logs/" + createdate + "AppiumLog.txt";
		// -g 参数指定日志文件保存的路径  ——local-timezone指定本地时间  --log-timestamp 日志中记录时间戳
		String startAppiumCMD = "appium -p " + port + " -g " + appiumLogFile + " --local-timezone --log-timestamp";
		runCmd(startAppiumCMD);
		try {
			double t = 1000;
			t = Double.parseDouble(time);
			Thread.sleep((int)(t * 1000));
			setPass();
		} catch (InterruptedException e) {
			setFail(e);
			e.printStackTrace();
		}
	}

	// 启动被测APP
	public void runAPP(String device, String version, String appPackage, String appActivity,
			String appiumServerPort, String time) {
		try {
			AutoLogger.log.info("启动待测App");
			AppDriver app = new AppDriver(device,version,  appPackage, appActivity, appiumServerPort);
			driver = app.getDriver();
			//等待app应用完成启动
			halt(time);
			setPass();
		} catch (Exception e) {
			setFail(e);
			AutoLogger.log.error("启动待测App失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	/**
	 * 基于某个元素的坐标位置，找到需要点击的坐标的位置进行操作。
	 * xpath表达式定位id的方法：//*[@resource-id='com.tencent.mobileqq:id/ws']
	 * 
	 * @param xpath 元素基于xpath定位的表达式
	 * @param xrate X轴的比例
	 * @param yrate y轴的比例
	 */
	public void tapByRelativeCoordinate(String xpath, String xrate, String yrate) {
		try {
			WebElement topTab = driver.findElement(By.xpath(xpath));
			// 获取元素的左上角起始点
			Point topStartP = topTab.getLocation();
			// 获取元素的区域范围，整个元素的宽和高
			Dimension topD = topTab.getSize();
			System.out.println("起始点：" + topStartP + "范围：" + topD);
			// 计算需要点击的位置处于这个元素的什么地方
			int startx = topStartP.getX();
			int starty = topStartP.getY();
			int rangex = topD.getWidth();
			int rangey = topD.getHeight();
			int x = Integer.parseInt(xrate);
			int y = Integer.parseInt(yrate);
			// x轴的十分之一的位置，和y轴的2分之一
			int targetX = startx + rangex / x;
			int targetY = starty + rangey / y;
			// 使用appium的touchAction类来完成坐标点击
			TouchAction act = new TouchAction(driver);
			// 完成对于坐标位置的点击，注意传递的参数是PointOption中的point静态方法完成构造的点位。
			act.tap(PointOption.point(targetX, targetY)).perform();
			setPass();
		} catch (NumberFormatException e) {
			setFail(e);
			e.printStackTrace();
		}
	}

	/**
	 * 基于屏幕分辨率的比例进行点击
	 * @param xrate x轴坐标的比例
	 * @param yrate y坐标的比例。
	 */
	public void tapByRate(String xrate,String yrate){
		try {
			//1、获取屏幕分辨率大小
			int height=driver.manage().window().getSize().getHeight();
			int width=driver.manage().window().getSize().getWidth();
			//2、计算基于比例算出来的坐标位置
			float x=width * Float.parseFloat(xrate);
			float y=height * Float.parseFloat(yrate);
			//调用进行点击
			adbTap(x+"",y+"");
			setPass();
		} catch (NumberFormatException e) {
			setFail(e);
			e.printStackTrace();
		}
	}

	/**
	 * 由于app自动化测试中，xpath的定位效率确实较低，所以考虑使用其它方法。 补充使用不同的定位方式的click方法
	 * 
	 * @param method  定位用的方法
	 * @param locator 定位用的表达式
	 */
	public void multiByclick(String method, String locator) {
		try {
			switch (method) {
			//content-desc属性
			case "accessbilityId":
				driver.findElement(MobileBy.AccessibilityId(locator)).click();
				break;
				//resource-id属性
			case "id":
				((MobileElement)driver.findElement(By.id(locator))).click();
				break;
				//class属性
			case "classname":
				driver.findElement(By.className(locator)).click();
				break;
			case "xpath":
				driver.findElement(By.xpath(locator)).click();
				break;
				//uiautomator的定位方式：new UiSelector().className(\"android.widget.TextView\").textContains(\"逛商场\")
			case "uiautomator":
				driver.findElement(MobileBy.AndroidUIAutomator(locator));
				break;
			default:
				driver.findElement(By.xpath(locator)).click();
				break;
			}
			setPass();
			AutoLogger.log.info("点击通过"+method+"方式定位的元素"+locator);
		} catch (Exception e) {
			setFail(e);
			AutoLogger.log.error("点击通过"+method+"方式定位的元素"+locator+"失败");
			e.printStackTrace();
		}
	}

	public void input(String xpath, String text) {
		try {
			driver.findElement(By.xpath(xpath)).clear();
			driver.findElement(By.xpath(xpath)).sendKeys(text);
			AutoLogger.log.info(xpath + "元素中输入" + text);
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("input");
			setFail(e);
		}
	}

	public void click(String xpath) {
		try {
			driver.findElement(By.xpath(xpath)).click();
			AutoLogger.log.info(xpath + "元素进行点击");
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			saveScrShot("click");
			setFail(e);
		}
	}

	/**
	 * 针对于某些可能出现也可能不出现的元素，基于trycatch的机制，尝试点击，如果没有就输出日志，没有广告不需要关闭，继续执行后续代码。
	 * 
	 * @param xpath
	 */
	public void closeAd(String xpath) {
		try {
			driver.findElement(By.xpath(xpath)).click();
			AutoLogger.log.info(xpath + "元素进行点击，关闭广告");
			setPass();
		} catch (Exception e) {
			AutoLogger.log.info("没有广告，不需要关闭");
			setFail(e);
		}
	}

	// 调用adb滑动
	public void adbSwipe(String i, String j, String k, String l, String m) {
		try {
			this.runCmd("adb shell input swipe " + i + " " + j + " " + k + " " + l + " " + m);
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error("通过adb执行滑动失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}
	
	//通过adb输入文本内容。
	public void adbText(String text) {
		try {
			runCmd("adb shell input text "+text);
			setPass();
		} catch (Exception e) {
			e.printStackTrace();
			setFail(e);
		}
	}

	// 调用adb模拟按键
	public void adbPressKey(String keycode) {
		try {
			int k = Integer.parseInt(keycode);
			String cmd = " adb shell input keyevent " + k;
			runCmd(cmd);
			Thread.sleep(2000);
			setPass();
		} catch (InterruptedException e) {
			AutoLogger.log.error("通过adb执行按键事件失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	public void adbTap(String xAxis, String yAxis) {
		try {
			runCmd("adb shell input tap " + xAxis + " " + yAxis);
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error("通过adb执行点击失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	// 通过appium的方法进行滑屏
	public void appiumSwipe(String iniX, String iniY, String finX, String finY) {
		try {
			// string型的参数转换为int型
			int x = Integer.parseInt(iniX);
			int y = Integer.parseInt(iniY);
			int x1 = Integer.parseInt(finX);
			int y1 = Integer.parseInt(finY);
			TouchAction action = new TouchAction(driver);
			// 设置起点和终点
			PointOption pressPoint = PointOption.point(x,y);
			PointOption movePoint = PointOption.point(x1, y1);
			// 滑动操作由长按起点->移动到终点->松开组成。
			action.longPress(pressPoint).moveTo(movePoint).release().perform();
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	// 通过appium的方法基于元素定位进行滑屏
	public void appiumSwipeElement(String xpath, String finX, String finY) {
		try {
			// string型的参数转换为int型
			int x1 = Integer.parseInt(finX);
			int y1 = Integer.parseInt(finY);
			TouchAction action = new TouchAction(driver);
			// 设置起点和终点
			PointOption movePoint = PointOption.point(x1, y1);
			// 滑动操作由长按起点->移动到终点->松开组成。
			action.longPress(LongPressOptions.longPressOptions()
					.withElement(ElementOption.element(driver.findElement(By.xpath(xpath))))).moveTo(movePoint)
					.release().perform();
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	// 使用appium的方法点击坐标
	public void appiumTap(String x, String y) {
		try {
			int xAxis = Integer.parseInt(x);
			int yAxis = Integer.parseInt(y);
			TouchAction action = new TouchAction(driver);
			PointOption pressPoint = PointOption.point(xAxis, yAxis);
			// action类分解动作，先点击，然后松开
			action.tap(pressPoint).release().perform();
			setPass();
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium点击坐标方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	// 使用appium方法长按
	public void appiumHold(String x, String y, String time) {
		try {
			// string转int
			int xAxis = Integer.parseInt(x);
			int yAxis = Integer.parseInt(y);
			int t = Integer.parseInt(time);
			// 指定长按的坐标
			PointOption pressPoint = PointOption.point(xAxis, yAxis);
			// 长按的时间，使用java提供的time类库中的duration类
			Duration last = Duration.ofSeconds(t);
			WaitOptions wait = WaitOptions.waitOptions(last);
			TouchAction action = new TouchAction(driver);
			// 长按坐标->指定按住的时间进行等待
			action.longPress(pressPoint).waitAction(WaitOptions.waitOptions(last)).release().perform();
			setPass();
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	// 长按页面上的某一个元素
	public void appiumHoldEl(String xpath, String time) {
		try {
			int t = Integer.parseInt(time);
			Duration last = Duration.ofSeconds(t);
			TouchAction action = new TouchAction(driver);
			// 定位到一个元素，并且在该元素上长按指定的时长
			action.longPress(LongPressOptions.longPressOptions()
					.withElement(ElementOption.element(driver.findElementByXPath(xpath))).withDuration(last))
					.waitAction(WaitOptions.waitOptions(last)).release().perform();
			setPass();
		} catch (NumberFormatException e) {
			AutoLogger.log.error("执行Appium滑动方法失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	// 执行keyevent 返回键
	public void appiumKeyeventBack(){
		try {
			KeyEvent key=new KeyEvent();
			driver.pressKey(key.withKey(AndroidKey.BACK));
			setPass();
		} catch (Exception e) {
			setFail(e);
		}
	}

	//通过key名字执行安卓按键事件，如BACK
	public void appiumKeyeventByName(String keyName){
		try {
			KeyEvent key=new KeyEvent();
			AndroidKey akey=AndroidKey.valueOf(keyName);
			driver.pressKey(key.withKey(akey));
			setPass();
		} catch (IllegalArgumentException e) {
			setFail(e);
		}
	}

	/**
	 * 实现显式等待的方法，在每次定位元素时，先尝试找元素，给10秒钟的最长等待。
	 */
	public void explicityWait(String xpath) {
		try {
			WebDriverWait eWait = new WebDriverWait(driver, 10);
			eWait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					return d.findElement(By.xpath(xpath));
				}
			});
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	/**
	 * 错误截图方法
	 * 
	 * @param method
	 */
	public void saveScrShot(String method) {
		// 获取当前的执行时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd+HH-mm-ss");
		// 当前时间的字符串
		String createdate = sdf.format(date);
		// 拼接文件名，形式为：工作目录路径+方法名+执行时间.png
		String scrName = "logs/screenshot/" + method + createdate + ".png";
		// 以当前文件名创建文件
		File scrShot = new File(scrName);
		// 将截图保存到临时文件
		File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			Files.copy(tmp, scrShot);
		} catch (IOException e) {
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("截图失败！");
		}
	}

	/**
	 * 双指操作，需要分别指定两个手指的动作起止坐标。
	 */
	public void doubleFinger() {
		try {
			// multitouchaction，用于拼接多个touchaction，让他们同时发生。
			MultiTouchAction multiAction = new MultiTouchAction(driver);
			// 创建两个touchaction，分别实现两个手指的动作。
			TouchAction actionone = new TouchAction(driver);
			// 创建等待时间的对象。
			Duration last = Duration.ofMillis(300);
			WaitOptions waitOptions = WaitOptions.waitOptions(last);
			// 创建第一个手指的移动起止点。x坐标增大，y坐标减小，往右上方划
			PointOption pressPointone = PointOption.point(300, 900);
			PointOption movePointone = PointOption.point(400, 800);
			// 滑动操作由长按起点->移动到终点->松开组成。
			actionone.press(pressPointone).waitAction(waitOptions).moveTo(movePointone).waitAction(waitOptions).release();
			// 创建第二个手指的动作。
			TouchAction actiontwo = new TouchAction(driver);
			// x坐标减小，y坐标增大，往左下方划
			PointOption pressPointtwo = PointOption.point(500, 600);
			PointOption movePointtwo = PointOption.point(400, 800);
			// 滑动操作由长按起点->移动到终点->松开组成。
			actiontwo.press(pressPointtwo).waitAction(waitOptions).moveTo(movePointtwo).waitAction(waitOptions).release();
			// 将创建好的两个不同的touchaction，添加到multiaction里，形成一组同步动作，从而完成操作。
			multiAction.add(actionone).add(actiontwo).perform();
			setPass();
		} catch (Exception e) {
			e.printStackTrace();
			setFail(e);
		}

	}

	public String getEleText(String xpath) {
		try {
			String text=driver.findElement(By.xpath(xpath)).getText();
			setPass();
			return text;
		} catch (Exception e) {
			setFail(e);
			return "获取元素文本失败";
		}
	}
	
	public String getEleContent(String xpath) {
		String content=driver.findElement(By.xpath(xpath)).getAttribute("content-desc");
		return content;
	}
	
	// 断言
	public void assertSame(String xpath, String paramRes) {
		try {
			String result = driver.findElement(By.xpath(xpath)).getText();
			System.out.println(result);
			if (result.equals(paramRes)) {
				AutoLogger.log.info("测试用例执行成功");
				setPass();
			} else {
				AutoLogger.log.info("测试用例执行失败");
				setFail(new Exception("实际结果是"+result+"预期结果是"+paramRes));
			}
		} catch (Exception e) {
			setFail(e);
			AutoLogger.log.error("执行断言时报错");
			AutoLogger.log.error(e, e.fillInStackTrace());
		}
	}

	public void quitApp() {
		try {
			driver.closeApp();
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error("关闭app失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

	/**
	 * 通过taskkill命令杀掉appiumserver的进程。
	 */
	public void killAppium() {
		try {
			runCmd("taskkill /F /IM node.exe");
			setPass();
		} catch (Exception e) {
			AutoLogger.log.error("关闭appiumserver服务失败");
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}
	
	
	/**
	 * 获取当前手机中所有的context并输出
	 */
	public void printcontexts() {
		AutoLogger.log.info(driver.getContextHandles());
	}

	/**
	 * 切换context
	 * 
	 * @param contextName
	 */
	public void switchContext(String contextName) {
		try {
			driver.context(contextName);
			setPass();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.error(e, e.fillInStackTrace());
			AutoLogger.log.error("切换context失败");
			setFail(e);
		}
	}

	/**
	 * 关闭手机浏览器
	 */
	public void closeBrowser() {
		try {
			driver.quit();
			setPass();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AutoLogger.log.error(e, e.fillInStackTrace());
			setFail(e);
		}
	}

}
