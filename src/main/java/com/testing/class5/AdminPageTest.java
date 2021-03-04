package com.testing.class5;

import com.testing.web.AdminPage;
import com.testing.web.UserPage;

/**
 * @Classname AdminPageTest
 * @Description 类型说明
 * @Date 2021/1/16 21:38
 * @Created by 特斯汀Roy
 */
public class AdminPageTest {
    public static void main(String[] args) {
        AdminPage admin=new AdminPage();
        admin.openBrowser("chrome");
        admin.visitURL("http://www.testingedu.com.cn:8000/Admin/Admin/login");
        admin.LoginAdmin("admin","123456","1");
        admin.toAddMenu();
        admin.addRandomGoods();

        UserPage user=new UserPage();
        //调用继承自父类的setDriver方法，将userpage的driver赋值为adminpage的driver，从而使用同一个浏览器。
        user.setDriver(admin);
        user.visitURL("http://www.testingedu.com.cn:8000/");
        user.loginUser();
        user.closeBrowser();
    }
}
