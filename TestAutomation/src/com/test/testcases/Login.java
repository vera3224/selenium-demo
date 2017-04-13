package com.test.testcases;

import java.util.Map;
import org.testng.annotations.Test;

import com.test.base.TestBase;
import com.test.page.FirstPage;
import com.test.page.LoginPage;
import com.test.util.Assertion;
import com.test.util.Log;

public class Login extends TestBase {
	
	@Test(dataProvider="providerMethod")
	public void testLogin(Map<String, String> param){
		Assertion.flag = true;
		this.goTo(param.get("url"));
//		driver.manage().window().maximize();
//		driver.manage().timeouts().pageLoadTimeout(Config.waitTime, TimeUnit.SECONDS);
//		driver.get(param.get("url"));
//		((SeleniumDriver) driver).analyzeLog();
		FirstPage fp = new FirstPage(driver);
		Log.logInfo("在首页点击登录按钮");
		fp.getElement("login_link").click();
		LoginPage lp = new LoginPage(driver);
		Log.logInfo("选择账号登录");
		lp.getElement("tab").click();
		Log.logInfo("登录用户名为:"+ param.get("username"));
		lp.getElement("login_name").sendKeys(param.get("username"));
		Log.logInfo("登录密码为："+param.get("password"));
		lp.getElement("login_pwd").sendKeys(param.get("password"));
		lp.getElement("login_button").click();
		String errorMsg = lp.getElement("errorMsg").getText().trim();
		Log.logInfo(errorMsg);
		
		Assertion.verifyEquals(errorMsg, "账户名与密码不匹配，请重新输入");
		
		
	}

}
