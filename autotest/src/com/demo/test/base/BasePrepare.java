
package com.demo.test.base;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.log4j.pattern.ClassNamePatternConverter;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import com.demo.test.utils.ExcelDataProvider;
import com.demo.test.utils.LogConfiguration;
import com.demo.test.utils.SeleniumUtil;

/**
 * @author vera
 *2017年4月11日
 */
public class BasePrepare {
//	输出本页面日志 初始化
	static Logger logger = Logger.getLogger(BasePrepare.class.getName());
	protected SeleniumUtil seleniumUtil = null;
//	添加成员变量来获取beforeClass 传人的context 参数
	protected ITestContext testContext = null;
	protected String webUrl="";
	protected int timeOut = 0;
	protected int sleepTime = 0;
	protected int waitMillisecondsForAlert = 0;
	
	@BeforeClass
//	启动浏览器并打开测试页面
	public void startTest(ITestContext context){
		LogConfiguration.initLog(this.getClass().getSimpleName());
		seleniumUtil = new SeleniumUtil();
//		这里得到了context值
		this.testContext = context;
//		从testng.xml文件中获取浏览器属性值
		String browserName = context.getCurrentXmlTest().getParameter("browserName");
		timeOut = Integer.valueOf(context.getCurrentXmlTest().getParameter("timeOut"));
		sleepTime = Integer.valueOf(context.getCurrentXmlTest().getParameter("sleepTime"));
		waitMillisecondsForAlert = Integer.valueOf(context.getCurrentXmlTest().getParameter("waitMillisecondsForAlert"));
		webUrl = context.getCurrentXmlTest().getParameter("testurl");
		
		try {
//			启动浏览器 launchBrowser 方法，主要是打开浏览器，输入测试地址，并最大化窗口
			seleniumUtil.launchBrowser(browserName, context, webUrl, timeOut);
		} catch (Exception e) {
			logger.error("浏览器不能正常工作，请检查原因",e);
		}
//		设置一个testng 上下文属性，将driver存起来，之后可以用context随时取到
//		主要是提供arrow获取driver对象使用，因为arrow截图方法需要一个driver对象
		testContext.setAttribute("SELENIUM_DRIVER", seleniumUtil.driver);
	}

	
	@AfterClass
//	结束测试关闭浏览器
	public void endTest(){
		if (seleniumUtil.driver != null) {
//			退出浏览器
			seleniumUtil.quit();
		}else {
			logger.error("浏览器driver没有获得对象，退出操作失败");
			Assert.fail("浏览器driver没有获得对象，退出操作失败");
		}
	}
	
//	测试数据提供者 - 方法
	@DataProvider(name="testData")
	public Iterator<Object[]> dataFortestMethod() throws IOException {
		String moduleName = null; //模块名
		String caseNum = null;
		String className = this.getClass().getName();
		int dotIndexNum = className.indexOf("."); //取得第一个.的index
		int underlineIndexNum = className.indexOf("_");//取得第一个_的index
		
		if (dotIndexNum > 0) {
			moduleName = className.substring(24, className.lastIndexOf("."));
		}
		if (underlineIndexNum > 0) {
//			取到用例编号
			caseNum = className.substring(underlineIndexNum+1,underlineIndexNum+4);
		}
		
//		将模块名称和用例的编号传给 ExcelDataProvider，然后进行读取excel数据
		return new ExcelDataProvider(moduleName,caseNum);

	}
}
