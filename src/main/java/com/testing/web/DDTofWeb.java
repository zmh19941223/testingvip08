package com.testing.web;

import com.google.common.io.Files;
import com.testing.common.AutoLogger;
import com.testing.common.ExcelWriter;
import com.testing.common.MysqlUtils;
import com.testing.webDriver.FFDriver;
import com.testing.webDriver.GoogleDriver;
import com.testing.webDriver.IEDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Classname WebKeyword
 * @Description web自动化常用关键字
 * @Date 2021/1/10 20:28
 * @Created by 特斯汀Roy
 */
public class DDTofWeb {
    //所有方法都要用的driver对象
    public WebDriver driver = null;

    public ExcelWriter resultcase=null;

    public final int RESULTCOL=10;

    //写入操作的时候，需要告诉wrtier写入的行号
    public int nowline=0;

    public void setLine(int rowNo){
        nowline=rowNo;
    }

    //构造方法中，传递resultwriter，完成excelwriter的赋值操作。其它方法中调用完成写入。
    public DDTofWeb(ExcelWriter resultWriter){
            resultcase=resultWriter;
    }

    public void setDriver(DDTofWeb web){
        driver=web.getDriver();
    }

    /**
     * 返回driver用于进行调试。
     * @return
     */
    public WebDriver getDriver(){
        return  driver;
    }

    /************************************等待类关键字****************************************/
    /**
     * 强制等待指定秒数，为了方便用例调用，统一使用字符串类型参数
     * @param seconds
     */
    public void halt(String seconds) {
        try {
            double time = Double.parseDouble(seconds);
            Thread.sleep((long)(time * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐式等待，
     */
    public void imWait(int seconds){
        driver.manage().timeouts().implicitlyWait(seconds,TimeUnit.SECONDS);
    }

    /**
     * 显式等待。使用预定义的等待条件。
     */
    public void exWaitTextToBe(){
        try {
            //设置一个10秒的超时时间，也就是给的机会有多长。
            WebDriverWait wait=new WebDriverWait(driver,10);
            //指定等待的条件
            wait.until(ExpectedConditions.textToBe(By.xpath("//div[@id='content_left']/div[2]/h3/a"),"特斯汀学院 - "));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义显式等待，元素包含某个内容
     */
    public void exWaitTextToContains(){
        try {
            //设置一个10秒的超时时间，也就是给的机会有多长。
            WebDriverWait wait=new WebDriverWait(driver,10);
            //指定等待的条件
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    try {
                        String text = driver.findElement(By.xpath("//div[@id='content_left']/div[2]/h3/a")).getText();
                        return text.contains("特斯汀学院 - ");
                    } catch (Exception e) {
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************************************浏览器访问类关键字************************************/
    /**
     * 打开浏览器的方法
     * 通过输入浏览器类型，启动对应的driver完成浏览器启动。
     */
    public void openBrowser(String browser) {
        try {
            switch (browser) {
                case "chrome":
                    GoogleDriver gg = new GoogleDriver("E:\\AutoTools\\chromedriver\\87\\chromedriver.exe");
                    //注意，这里不是 Webdriver driver=gg.getdriver();
                    driver = gg.getdriver();
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    AutoLogger.log.info("****************chrome浏览器启动了************");
                    break;
                case "ff":
                    FFDriver ff = new FFDriver("", "webDriverExe\\geckodriver.exe");
                    driver = ff.getdriver();
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    break;
                case "ie":
                    IEDriver ie = new IEDriver("webDriverExe\\IEDriverServer.exe");
                    driver = ie.getdriver();
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    break;
                case "edge":
                    System.setProperty("webdriver.edge.driver", "webDriverExe\\msedgedriver.exe");
                    //创建了一个新的局部变量，成员变量driver没有赋值，是null，所以报空指针。
                    WebDriver driver = new EdgeDriver();
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    break;
                default:
                    GoogleDriver gdefault = new GoogleDriver("E:\\AutoTools\\chromedriver\\87\\chromedriver.exe");
                    //注意，这里不是 Webdriver driver=gg.getdriver();
                    driver = gdefault.getdriver();
                    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                    break;
            }
            resultcase.writeCell(nowline,RESULTCOL,"PASS");
            AutoLogger.log.info("正在启动浏览器");
        } catch (Exception e) {
            e.printStackTrace();
            AutoLogger.log.error("浏览器打开执行失败");
            AutoLogger.log.error(e,e.fillInStackTrace());
            resultcase.writeFailCell(nowline,RESULTCOL,"FAIL");
        }
    }

    /**
     * 访问指定url的网页
     *
     * @param url
     */
    public void visitURL(String url) {
        try {
            driver.get(url);
            resultcase.writeCell(nowline,RESULTCOL,"PASS");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("url访问失败，请检查输入url是否带了协议。");
            resultcase.writeFailCell(nowline,RESULTCOL,"FAIL");
        }
    }

    /****************************************元素操作类关键字***********************************/
    /**
     * 用xpath定位元素，清空并输入内容
     * @param xpath
     * @param content
     */
    public boolean input(String xpath, String content) {
        try {
            WebElement input = driver.findElement(By.xpath(xpath));
            //清理输入框中的内容
            input.clear();
            input.sendKeys(content);
            resultcase.writeCell(nowline,RESULTCOL,"PASS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("向" + xpath + "输入内容的时候，失败。");
            takeScreen("input");
            resultcase.writeFailCell(nowline,RESULTCOL,"FAIL");
            return false;
        }
    }

    /**
     * 使用css选择器定位元素并点击
     * @param cssSelector
     */
    public void clickcss(String cssSelector) {
        try {
            driver.findElement(By.cssSelector(cssSelector)).click();
            resultcase.writeCell(nowline,RESULTCOL,"PASS");
        } catch (Exception e) {
            e.printStackTrace();
            resultcase.writeFailCell(nowline,RESULTCOL,"FAIL");
        }
    }

    /**
     * 使用xpath定位元素并点击
     * @param xpath
     */
    public boolean click(String xpath){
        try {
            driver.findElement(By.xpath(xpath)).click();
            resultcase.writeCell(nowline,RESULTCOL,"PASS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            takeScreen("click");
            resultcase.writeFailCell(nowline,RESULTCOL,"FAIL");
            return false;
        }
    }

    /**
     * 基于selenium 8大定位方法进行选择定位并点击
     * @param by 定位方法，支持id、name、tag、classname、xpath、css，默认使用xpath
     * @param Locator  定位表达式
     */
    public void click(String by, String Locator) {
        try {
            switch (by) {
                case "id":
                    driver.findElement(By.id(Locator)).click();
                    break;
                case "name":
                    driver.findElement(By.name(Locator)).click();
                    break;
                case "tag":
                    driver.findElement(By.tagName(Locator)).click();
                    break;
                case "classname":
                    driver.findElement(By.className(Locator)).click();
                    break;
                case "xpath":
                    driver.findElement(By.xpath(Locator)).click();
                    break;
                case "css":
                    driver.findElement(By.xpath(Locator)).click();
                    break;
                default:
                    driver.findElement(By.xpath(Locator)).click();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 鼠标移动到指定元素，实现悬停效果。
     * @param xpath
     */
    public void hover(String xpath){
        try {
            Actions actions =new Actions(driver);
            actions.moveToElement(driver.findElement(By.xpath(xpath))).perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过via参数来选择select选择选项的方式
     * @param via
     * @param xpath
     * @param content  选择的参数内容。
     */
    public void select(String xpath,String via,String content){
        try {
            WebElement selele = driver.findElement(By.xpath(xpath));
            //通过实例化select对象，将webelement元素，转换为select
            Select sel=new Select(selele);
            switch(via){
                case "value":
                    sel.selectByValue(content);
                    break;
                case "text":
                    sel.selectByVisibleText(content);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 基于value值来进行下拉框选择。
     * @param xpath
     * @param Value
     */
    public void selectByValue(String xpath,String Value){
        try {
            WebElement selele = driver.findElement(By.xpath(xpath));
            Select sel=new Select(selele);
            sel.selectByValue(Value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******************************************窗口与iframe切换*******************************/

    /**
     * 通过浏览器页面的标题来进行窗口的切换
     * @param expectedTitle
     */
    public void switchWindowByTitle(String expectedTitle){
        try {
            //先获取所有的句柄
            Set<String> windowHandles = driver.getWindowHandles();
            //遍历所有的句柄，尝试切换窗口获取它的标题，如果标题和预期一致就是需要的窗口
            for (String windowHandle : windowHandles) {
                //先切换到当前句柄所代表的窗口
                driver.switchTo().window(windowHandle);
                //判断当前窗口的标题是否等于预期标题，如果相等，则break退出循环，不再遍历后续句柄。
                if(driver.getTitle().equals(expectedTitle)){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 基于窗口顺序进行切换
     * @param index 想要切换到的窗口的顺序。
     */
    public void switchWindowByindex(String index){
        try {
            int targetIndex=Integer.parseInt(index);
            Set<String> windowHandles = driver.getWindowHandles();
            int count=0;
            for(String window:windowHandles){
                count++;
                if(count==targetIndex){
                    System.out.println("这是第"+targetIndex+"个句柄"+window);
                    driver.switchTo().window(window);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 基于xpath切换Iframe
     * @param xpath
     */
    public boolean switchIframe(String xpath){
        try {
            driver.switchTo().frame(driver.findElement(By.xpath(xpath)));
            resultcase.writeCell(nowline,RESULTCOL,"PASS");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            resultcase.writeFailCell(nowline,RESULTCOL,"FAIL");
            return false;
        }
    }

    /**
     * 切换到父级iframe
     */
    public void switchUpIframe(){
        try {
            driver.switchTo().parentFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换到html根层级
     */
    public void switchOutIframe(){
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行js脚本方法
     * @param jsScript
     */
    public void runJs(String jsScript){
        try {
            JavascriptExecutor runner=(JavascriptExecutor)driver;
            runner.executeScript(jsScript);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将元素定位表达式通过xpath进行定位，然后再调用要对元素进行的操作，注意是js方法的操作。
     * @param method  调用的方法 注意根据情况带上小括号进行调用  比如click() 或者 innerText="xxx"
     * @param xpath
     */
    public void runJsWithElement(String method,String xpath){
        WebElement element = driver.findElement(By.xpath(xpath));
        JavascriptExecutor runner=(JavascriptExecutor)driver;
        runner.executeScript("arguments[0]."+method,element);
    }

    /************************************断言与信息获取*********************************************/
    /**
     * 获取页面标题
     * @return
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     *  获取单个元素的文本内容
     *  * @param xpath 元素的xpath定位表达式
     * @return
     */
    public String getSingleText(String xpath) {
        try {
            String text = driver.findElement(By.xpath(xpath)).getText();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return "获取元素文本内容失败";
        }
    }

    //获取多个元素的文本内容。
    public List<String> getAllText(String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        //创建结果list。
        List<String> results = new ArrayList<>();
        //遍历list里面的每一个元素，获取文本内容，加到结果list里面
        for (WebElement e : elements) {
            results.add(e.getText());
            e.click();
        }
        return results;
    }

    /**
     * 获取文本内容的同时，可以选择判断是否符合预期。
     * 1、同时返回文本结果和判断结果。 可以选择把其中一个想要返回的结果用成员变量记录。也可以用Map返回。
     * 2、可以选择是否判断。 （通过是否输入了预期值，判断要不要执行断言操作）
     * 通过可变参数，完成选择，如果输入参数个数大于0，则进行判断，否则不进行。
     * 3、业务逻辑过程。
     *
     * @param xpath  元素定位表达式
     * @param expect
     * @return
     */
    public Map<String, Boolean> getText(String xpath, String... expect) {
        try {
            String text = driver.findElement(By.xpath(xpath)).getText();
            //默认返回结果是false，因为可能出现没比较的情况。
            Boolean result = false;
            //可变参数列表长度大于0，就要断言
            if (expect.length > 0) {
                //预期值可以填多个，只要这个结果是预期值中的其中一个，就行。
                for (String ex : expect) {
                    //针对每个预期值，进行断言。只要其中有通过的，结果就置为true，否则不动，因为默认是false。
                    if (text.contains(ex)) {
                        result = true;
                    }
                }
            }
            Map<String, Boolean> textAndreuslt = new HashMap<>();
            textAndreuslt.put(text, result);
            return textAndreuslt;
        } catch (Exception e) {
            e.printStackTrace();
            //返回自己指定内容的结果。
            Map<String, Boolean> noresult = new HashMap<>();
            noresult.put("没有结果", false);
            return noresult;
        }
    }

    /**
     * 断言某个元素文本内容是否包含或者等于预期值。
     */
    public boolean assertText(String xpath, String method, String expect) {
        //获取文本内容
        String text = "";
        boolean result=false;
        try {
            text = driver.findElement(By.xpath(xpath)).getText();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        switch (method) {
            case "=":
                if (text.equals(expect)) {
                    System.out.println("测试成功");
                    result=true;
                } else {
                    System.out.println("测试失败");
                    result=false;
                }
                break;
            case "包含":
                if (text.contains(expect)) {
                    System.out.println("测试成功");
                    result=true;
                } else {
                    System.out.println("测试失败");
                    result=false;
                }
                break;
        }
        return  result;
    }

    /**
     * 断言页面中包含指定内容。
     *
     * @param expect 预期包含的内容。
     * @return 返回结果为断言成功true或者失败false。
     */
    public boolean assertPageContains(String expect) {
        if (driver.getPageSource().contains(expect)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 输入sql语句进行查询，判断结果正确性
     * @param sql
     * @return
     */
    public boolean assertMysqlData(String sql,String expectData) {
        //先通过数据库查询获取结果
        MysqlUtils mysql = new MysqlUtils();
        mysql.createConnector();
        List<Map<String, String>> results = mysql.queryResult(sql);
        System.out.println("完整查询结果是"+results);
        //判断结果是否符合用例预期
        try {
            //尝试在查询结果中，获取预期字段，如果字段不为空，也不为空字符串，说明查到了想要的数据。
            String s = results.get(0).get(expectData);
            if (s != null && !s.equals("")) {
                System.out.println("获取到"+expectData+"的值是"+s);
                System.out.println("测试校验通过");
                return true;
            } else {
                System.out.println("测试失败");
                return false;
            }
        } catch (Exception e) {
            //查询获取数据的时候失败了
            return false;
        }
    }

    /**
     * 进行截图，将浏览器截图保存到指定目录下，文件名格式为指定的时间+报错方法。
     * @param method  报错方法。
     */
    public void takeScreen(String method){
        //截图操作。
        //1.将driver转换为截图对象
        TakesScreenshot screenshot=(TakesScreenshot)(driver);
        //2、截取当前刘浏览器图片
        File screenPic = screenshot.getScreenshotAs(OutputType.FILE);
        File savePic=new File("logs/Screenshot/"+method+createTime("MMdd+HH-mm")+".png");
        //3、把内容填到创建出来的图片文件中。
        try {
            Files.copy(screenPic,savePic);
        } catch (IOException e) {
            System.out.println("截图失败，检查一下文件格式。");
            e.printStackTrace();
        }
    }

    /**
     * 方法用于生成指定格式的日期字符串。
     * @param format  指定格式   yyyy表示年，MM 月 dd天  HH 小时  mm 分钟  ss秒 sss 毫秒。
     * @return
     */
    public String createTime(String format){
        Date now=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String result = sdf.format(now);
        return result;
    }

    /*******************************************关闭浏览器****************************************/

    /**
     * 关闭浏览器和driver释放资源。
     */
    public void closeBrowser() {
        driver.quit();
    }

    //****************************************Robot操作方法*****************************************/
    public void moveAndclickRB(int x,int y){
        RobotUtils rb=new RobotUtils();
        rb.moveToclick(x,y);

    }


}
