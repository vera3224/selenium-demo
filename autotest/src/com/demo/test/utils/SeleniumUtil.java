package com.demo.test.utils;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;



public class SeleniumUtil {
//	使用Log4j，第一步就是获取日志记录器，这个记录器将负责控制日志信息
	public static Logger logger = Logger.getLogger(SeleniumUtil.class.getName());
	public ITestResult it = null;
	public WebDriver driver = null;
	public WebDriver window = null;
	
//	启动浏览器
	public void launchBrowser(String browserName, ITestContext context, String webUrl, int timeOut) {
		SelectBrowser select = new SelectBrowser();
		driver = select.selectExplorerByName(browserName, context);
		try {
			maxWindow(browserName);
			waitForPageLoading(timeOut);
			get(webUrl);
		} catch (TimeoutException e) {
			// TODO: handle exception
			logger.warn("注意：页面没有完全加载出来，刷新重试!!");
			refresh();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String status = (String) js.executeScript("return document.readyState");
			logger.info("打印状态:"+status);
		}
	}
	
	
	public void close() {
		driver.close();
	}
	
	public void refresh() {
		// TODO Auto-generated method stub
		driver.navigate().refresh();
		logger.info("页面刷新成功");
	}
	
	public void back() {
		driver.navigate().back();
		}
	public void forward() {
		driver.navigate().forward();
		}
	
	public void get(String url) {
	// TODO Auto-generated method stub
		driver.get(url);
		logger.info("打开测试页面:["+url+"]");
		}

	
	public void maxWindow(String browserName) {
	// TODO Auto-generated method stub
		logger.info("最大化浏览器"+browserName);
		driver.manage().window().maximize();
		}
	
	
	public void setBrowserSize(int width, int height) {
		driver.manage().window().setSize(new Dimension(width, height));
	}
	
//	查找元素
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}
	
//	查找所有相同元素
	public List<WebElement>  findElementsBy(By by) {
		return driver.findElements(by);
	}
//	在相同elements中选择其中一个
	public WebElement getOneElement(By bys, By by,int index) {
		return findElementsBy(bys).get(index).findElement(by);
	}

	public void click(By byElement) {
		try {
			clickTheClickable(byElement,System.currentTimeMillis(),2500);
		} catch (StaleElementReferenceException e) {
			// TODO: handle exception
			logger.error("The element you clicked:["+byElement+"] is no longer exist!");
			Assert.fail("The element you clicked:["+byElement+"] is no longer exist!");
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to click element ["+byElement+"]");
			Assert.fail("Failed to click element ["+byElement+"]",e);
		}
		logger.info("点击元素["+byElement+"]");
	}

//	不能点击时候，重试点击操作
	public void clickTheClickable(By byElement, long stratTime, int timeOut) throws Exception{
		try {
			findElement(byElement).click();
			} catch (Exception e) {
			// TODO: handle exception
				if (System.currentTimeMillis()- stratTime >timeOut) {
					logger.warn(byElement+" is unclickable");
					throw new Exception(e);
				}else {
					Thread.sleep(500);
					logger.warn(byElement+" is unclickable, try again");
					clickTheClickable(byElement, stratTime, timeOut);
				}
		}
	}
	
//	获得页面标题
	public String getTitle() {
		return driver.getTitle();
	}
	
//	获得元素的文本
	public String getText(By elementLocator) {
		return driver.findElement(elementLocator).getText().trim();
	}
	
//	获得元素 属性的文本
	public String getAttributeText(By elementLocator, String attribute) {
		return driver.findElement(elementLocator).getAttribute(attribute).trim();
	}
	
//	清楚操作
	public void clear(By byElement) {
		try {
			findElement(byElement).clear();
		} catch (Exception e) {
			logger.error("清除元素["+byElement+"]上的内容失败！");
		}
		logger.info("清除元素["+byElement+"]上的内容");
	}
	
//	向输入框输入内容
	public void type(By byElement, String key) {
		try {
			findElement(byElement).sendKeys(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("输入 ["+key+"]到元素["+byElement+"]失败");
			Assert.fail("输入 ["+key+"]到元素["+byElement+"]失败");
		}
		logger.info("输入 ["+key+"]到["+byElement+"]");
	}
	
	/*模拟键盘操作，比如Ctrl+A，Ctrl+C
	 * 1. WebElement element - 要被操作的元素
	 * 2. Keys key - 键盘上的功能键
	 * 3. String keyword - 键盘上的字母
	 * */
	public void pressKeysOnKeyboard(WebElement element, Keys key,String keyword) {
		element.sendKeys(Keys.chord(key,keyword));
	}
	
//	在给定的时间内去查找元素，没找到就超时，抛出异常
	public void waitForElementToLoad(int timeOut , final By By) {
		logger.info("开始查找元素["+By+"]");
		try {
			(new WebDriverWait(driver, timeOut)).until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver driver) {
					WebElement element = driver.findElement(By);
					return element.isDisplayed();
				}
			});
		} catch (TimeoutException e) {
			logger.error("超时！！"+timeOut+" 秒之后还没找到元素["+By+"]");
			Assert.fail("超时！！"+timeOut+" 秒之后还没找到元素["+By+"]");
		}
		logger.info("找到元素["+By+"]");
	}
	
//	判断文本是否和需求要求的一致
	public void isTextCorrect(String actual, String expected) {
		try {
			Assert.assertEquals(actual, expected);
		} catch (AssertionError e) {
			logger.error("期望的文本是["+expected+"] ，实际找到了["+actual+"]");
			Assert.fail("期望的文本是["+expected+"] ，实际找到了["+actual+"]");
		}
		logger.info("找到了期望的文本：["+expected+"]");
	}
	
	
//	判断编辑框是否可编辑
	public void inInput(WebElement element) {
	}
	
//	获得输入框的值： 针对某些input输入框没有value属性，但又想取得input的值
	public String getInputValue(String chose,String choseValue) {
		String value = null;
		switch (chose.toLowerCase()) {
		case "name":
			String jsName = "return document.getElementsByName('"+choseValue+"')[0].value;";
			value = (String)((JavascriptExecutor)driver).executeScript(jsName);
			break;

		case "id":
			String jsId = "return document.getElementsById('"+choseValue+"').value;";
			value = (String)((JavascriptExecutor)driver).executeScript(jsId);
			break;
			
		default:
			logger.error("未定义的chose："+chose);
			Assert.fail("未定义的chose："+chose);
		}
		return value;
	}
	
//	等待alert 出现
	public Alert switchToPromptedAlertAfterWait(long waitMillisecondsForAlert) throws NoAlertPresentException {
		final int ONE_ROUND_WAIT = 200;
		NoAlertPresentException lastException = null;
		long endTime = System.currentTimeMillis() + waitMillisecondsForAlert;
		for (int i = 0; i < waitMillisecondsForAlert; i += ONE_ROUND_WAIT) {
			try {
				Alert alert = driver.switchTo().alert();
				return alert;
			} catch (NoAlertPresentException e) {
				lastException = e;
			}
			pause(ONE_ROUND_WAIT);
			if (System.currentTimeMillis() > endTime) {
				break;
			}
		}
		throw lastException;
	}

// 暂停当前的用例执行，暂停时间为：sleepTime
	public void pause(int sleepTime) {
		if (sleepTime <= 0) {
			return;
			}
		try {
			Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				}
		}
	
//	退出
	public void quit() {
		driver.quit();
	}
	
//	切换 iframe - 根据string 类型（frame名字）
	public void inFrame(String frameId) {
		driver.switchTo().frame(frameId);
	}
//	切换iframe - 根据frame在页面中的顺序
	public void inFrame(int frameNum) {
		driver.switchTo().frame(frameNum);
	}
//	切换frame - 根据页面元素定位
	public void switchFrame(WebElement element) {
		try {
			logger.info("正在切换frame:["+getLocatorByElement(element,">")+"]");
			driver.switchTo().frame(element);
		} catch (Exception e) {
			logger.info("切换frame:["+getLocatorByElement(element,">")+"]失败");
			Assert.fail("切换frame:["+getLocatorByElement(element,">")+"]失败");
		}
		logger.info("切换frame:["+getLocatorByElement(element,">")+"]成功");
	}

//	跳出frame
	public void outFrame() {
		driver.switchTo().defaultContent();
	}
	
	
//	选择下拉选项 - 根据value
	public void selectByValue(By by, String value) {
		Select s = new Select(driver.findElement(by));
		s.selectByValue(value);
	}
	
//	下拉选项 - 根据index
	public void selectByIndex(By by, int index) {
		Select s = new Select(driver.findElement(by));
		s.selectByIndex(index);
	}
	
//	下拉选项 - 根据文本内容
	public void selectByText(By by,String text) {
		Select s = new Select(driver.findElement(by));
		s.selectByVisibleText(text);
	}
	
//	获得当前select 选择的值
	public List<WebElement> getCurrentSelectValue(By by) {
		List<WebElement> options = null;
		Select s = new Select(driver.findElement(by));
		options = s.getAllSelectedOptions();
		return options;
	}
	
//	检查checkbox是否勾选
	public boolean isCheckboxSelected(By elementLocator) {
		if (findElement(elementLocator).isSelected() == true) {
			logger.info("Checkbox:"+getLocatorByElement(findElement(elementLocator), ">")+" 被勾选");
			return true;
		}else
			logger.info("Checkbox:"+getLocatorByElement(findElement(elementLocator), ">")+" 没有被勾选");
		return false;		
	}
	
//	根据元素来获取此元素的定位值
	public String getLocatorByElement(WebElement element, String expectText) {
		String text = element.toString();
		String expect = null;
		try {
			expect = text.substring(text.indexOf(expectText)+1, text.length()-1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to find the string ["+expectText+"]");
		}
		return expect;
	}
	
//	执行 Javascript 方法
	public void executeJS(String js) {
		((JavascriptExecutor)driver).executeScript(js);
		logger.info("执行JavaScript语句：["+js+"]");
	}
	
 /*执行JavaScript 方法和对象
  * 用法 seleniumUtil.executeJS("arguments[0].click();",
  seleniumUtil.findElementBy(MyOrdersPage>MOP_TAB_ORDERCLOSE));
  * */
	public void executeJS(String js, Object... args) {
		((JavascriptExecutor)driver).executeScript(js, args);
		logger.info("执行Javascript语句:["+js+"]");
	}
	
//	selenium模拟鼠标操作 - 鼠标移动到指定元素
	public void mouseMoveToElement(By by) {
		Actions builder = new Actions(driver);
		Actions mouse = builder.moveToElement(driver.findElement(by));
		mouse.perform();
	}
	
//	selenium模拟鼠标操作 - 鼠标移动到指定元素
	public void mouseMoveToElement(WebElement element) {
		Actions builder = new Actions(driver);
		Actions mouse = builder.moveToElement(element);
		mouse.perform();
	}
	
//	selenium模拟鼠标操作 - 鼠标右击
	public void mouseRightClick(By element) {
		Actions builder = new Actions(driver);
		Actions mouse = builder.contextClick(findElement(element));
		mouse.perform();
	}
	
//	添加cookies，做自动登录的必要方法
	public void addCookies(int sleepTime) {
		pause(sleepTime);
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie c : cookies) {
			System.out.println(c.getName()+"->"+c.getValue());
			if (c.getName().equals("logisticSessionid")) {
				Cookie cook = new Cookie(c.getName(), c.getValue());
				driver.manage().addCookie(cook);
				System.out.println(c.getName()+"->"+c.getValue());
				System.out.println("添加成功");
			}else{
				System.out.println("没有找到logisticSessionid");
			}
		}
	}
	
//	获得CSS value
	public String getCSSValue(WebElement e, String key) {
		return e.getCssValue(key);
	}
	
//	使用testng 的assertTrue 方法
	public void assertTrue(WebElement e, String content) {
		String str = e.getText();
		Assert.assertTrue(str.contains(content), "字符串数组中不含有"+ content);
	}
	
//	webdriver 中可以设置很多超时时间
	/*
	 * implicitlWait,识别对象的超时时间，过了这个时间如果对象还没找到就会抛出 NoSuchElement异常
	 * */
	public void implicitlyWait(int timeOut) {
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
	}
	
//	setScriptTimeOut 异步脚本超时时间
	public void setScriptTimeOut(int timeOut) {
		driver.manage().timeouts().setScriptTimeout(timeOut, TimeUnit.SECONDS);
	}
	
	/*
	 * pageLoadTimeout，页面加载超时时间，webdriver 会等待页面加载完毕再进行后面的操作
	 * 如果页面在这个超时时间内没有加载完成，webdriver就会抛出异常
	 * */
	public void waitForPageLoading(int pageLoadTime) {
		driver.manage().timeouts().pageLoadTimeout(pageLoadTime, TimeUnit.SECONDS);
	}
	
	/*
	 * 上传文件，需要点击弹出上传照片的窗口才行
	 * @param browser  使用的浏览器名称
	 * @param file  需要上传的文件及文件名
	 * 
	 * */
	public void handleUpload(String browser, File file) {
		String filePath = file.getAbsolutePath();
		String executeFile = "res/script/autoit/Upload.exe";
		String cmd = "\""+executeFile+"\""+""+"\""+browser+"\""+""+"\""+filePath+"\"";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	对于windows GUI弹框，要求输入用户名，selenium不能直接操作
	public void loginOnWinGUI(String username, String password,String url) {
		driver.get(username+":"+password+"@"+url);
	}
	
//	检查元素是否显示
	public boolean isDisplayed(WebElement element) {
		boolean isDisplay = false;
		if (element.isDisplayed()) {
			logger.info("The element:["+getLocatorByElement(element, ">")+"] is displayed");
			isDisplay = true;
		}else if (element.isDisplayed() == false) {
			logger.warn("The element:["+getLocatorByElement(element, ">")+"] is not displayed");
			isDisplay = false;
		}
		return isDisplay;
	}
	
//	检查元素是不是存在
	public boolean doesElementsExist(By byElement) {
		try {
			findElement(byElement);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
//	检查元素是否被勾选
	public boolean isSelected(WebElement element) {
		boolean flag = false;
		if (element.isSelected()== true) {
			logger.info("The element:["+getLocatorByElement(element, ">"+"] is selected"));
			flag = true ;
		}else if (element.isSelected() == false) {
			logger.info("The element:["+getLocatorByElement(element, ">"+"] is not selected"));
			flag = false;
		}
		return flag;
	}
	
	/*
	 * 判断实际文本，期望文本
	 * @param actual 实际文本
	 * @param expect 期望文本
	 * */
	public void isContains(String actual,String expect) {
		try {
			Assert.assertTrue(actual.contains(expect));
		} catch (AssertionError e) {
			logger.error("The ["+actual+"] is not contains ["+expect+"]");
			Assert.fail("The ["+actual+"] is not contains ["+expect+"]");
		}
		logger.info("The ["+actual+"] is contains ["+expect+"]");
	}
	
//	获得屏幕的分辨率 - 宽
	public static double getScreenWidth() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
//	获得屏幕的分辨率 - 高
	public static double getScreenHeight() {
		return java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
}
