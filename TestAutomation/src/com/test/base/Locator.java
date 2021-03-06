package com.test.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.ho.yaml.Yaml;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.test.bean.Config;
import com.test.util.Log;

public class Locator {
	private String yamlFile;
	protected WebDriver driver;
	
	private Map<String, Map<String, String>> extendLocator;
	
	public void setYamlFile(String yamlFile) {
		this.yamlFile = yamlFile;
	}
	public Locator(WebDriver driver) {
//		yamlFile ="demo";
//		this.getYamlFile();
		this.driver = driver;
		
	}

	private Map<String, Map<String, String>> ml;
	
	@SuppressWarnings("unchecked")
	protected void getYamlFile() {
		File f = new File("locator/"+yamlFile+".yaml");
		try {
			ml = Yaml.loadType(new FileInputStream(f.getAbsolutePath()), HashMap.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
//	将 ml 变量里的value 转换成By对象
	private By getBy(String type, String value) {
		By by = null;
		if (type.equals("id")) {
			by =By.id(value);
		}
		if (type.equals("name")) {
			by = By.name(value);
		}
		if (type.equals("xpath")) {
			by = By.xpath(value);
		}
		if (type.equals("className")) {
			by = By.className(value);
		}
		if (type.equals("linkText")) {
			by = By.linkText(value);
		}
		if (type.equals("css")) {
			by = By.cssSelector(value);
		}
		return by;	
	}
	
//	添加元素等待
	private WebElement waitForElement(final By by) {
		WebElement element = null;
		int waitTime = Config.waitTime;
		try {
			element = new WebDriverWait(driver, waitTime).until(new ExpectedCondition<WebElement>() {

				@Override
				public WebElement apply(WebDriver d) {
					return d.findElement(by);
				}
			});
		} catch (Exception e) {
			Log.logInfo(by.toString()+" is not exist until"+waitTime);
		}
		return element;
	}
	
//	判断元素是否显示
	private boolean waitElementToBeDisplayed(final WebElement element) {
		boolean wait = false;
		if (element == null) {
			return wait;
		}
		try {
			wait = new WebDriverWait(driver, Config.waitTime).until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver d) {
					return element.isDisplayed();
				}
			});
		} catch (Exception e) {
			Log.logInfo(element.toString()+"is not displayed");
		}
		return wait;
	}
	
//	有元素的显示，就有元素的消失，添加元素对象消失的方法
	public Boolean waitElementToBeNotDisplayed(final WebElement element) {
		boolean wait = false;
		if (element == null) {
			return wait;
		}
		try {
			wait = new WebDriverWait(driver, Config.waitTime).until(new ExpectedCondition<Boolean>() {

				@Override
				public Boolean apply(WebDriver d) {
					return element.isDisplayed();
				}
			});
		} catch (Exception e) {
			Log.logInfo("Locator ["+element.toString()+"] is also displayed");
		}
		return wait;
	}
	
	/*
	 * 如果yaml 文件中允许使用参数 %s
	 * 在调用 getElement 和 getElementNoWait 方法时就需要把参数 穿进去
	 * 添加一个处理方法
	 * */
	private String getLocatorString(String locatorString, String[] ss) {
//		通过数组循环去替代里面的 %s。 再把该方法结合到 getLocator 方法中
		for (String s : ss) {
			locatorString = locatorString.replaceFirst("%s", s);
		}
		return locatorString;
	}
	
	
	/*
	 * 1. 由于 getElement 和 getElementNowait 方法体接近. 重构这两个方法
	 * 2.
	 * */
	private WebElement getLocator(String key,String[] replace, boolean wait) {
		WebElement element = null;
		if (ml.containsKey(key)) {
			Map<String, String> m = ml.get(key);
			String type = m.get("type");
			String value = m.get("value");
//			By by = this.getBy(type, value);
			if(replace != null)
				value = this.getLocatorString(value, replace);
			By by = this.getBy(type, value);
			if (wait) {
				element = this.waitForElement(by);
				boolean flag = this.waitElementToBeDisplayed(element);
				if (!flag) {
					element= null;
				}
			}else {
				try {
					element = driver.findElement(by);
				} catch (Exception e) {
					element = null;
				}
			}
		}else {
			Log.logInfo("Locator"+key+"is not exist in "+ yamlFile+".yaml");
		}
		return element;
		
	}
	
	
	

	public WebElement getElement(String key) {
		/*
		String type = ml.get(key).get("type");
		String value = ml.get(key).get("value");
//		return this.waitForElement(this.getBy(type, value));
//		添加元素显示判断后，更改代码
		WebElement element = this.waitForElement(this.getBy(type, value));
		if (!this.waitElementToBeDisplayed(element)) {
			element = null;
		}
		return element;
		*/
		return this.getLocator(key, null, true);
	}
	
//	增加一个元素不出现在页面上的验证
	public WebElement getElementNoWait(String key) {
		/*
		WebElement element = null;
		String type = ml.get(key).get("type");
		String value = ml.get(key).get("value");
		try {
			element = driver.findElement(this.getBy(type, value));
		} catch (Exception e) {
			element = null;
		}
		return element;
		*/
		return this.getLocator(key, null, false);
	}
	
	
	public WebElement getElement(String key, String[] replace) {
		return this.getLocator(key, replace, true);
	}
	public WebElement getElementNoWait(String key , String[] replace) {
		// TODO Auto-generated method stub
		return this.getLocator(key, replace, false);

	}
	
	

	
	/*
	 * 如果yaml 文件中有productId
	 * 例如：value:"//div[@id='%productId%']//input[@name='button']"
	 * 添加替换方法
	 * */
	public void setLocatorVariableValue(String variable,String value) {
		if (ml !=null) {
			Set<String> keys = ml.keySet();
			for (String key : keys) {
				String v = ml.get(key).get("value").replaceAll("%"+variable+"%s", value);
				ml.get(key).put("value", v);
			}
		}
	}
	
	
	/*
	 * 一些元素是公共的，每个页面上都会出现。 这些公共的locator只是有时候用到，大部分时间不用
	 * 就把这些公共的防在一个特定的文件里，在需要的时候通过外部加载来使用这些公共的locator
	 * */
//	private HashMap<String, HashMap<String, String>> extendLocator;
	
	@SuppressWarnings("unchecked")
	public void loadExtendLocator(String fileName) {
		File f = new File("locator/"+fileName+".yaml");
		try {
			extendLocator = Yaml.loadType(new FileInputStream(f.getAbsolutePath()), HashMap.class);
			ml.putAll(extendLocator);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	

//	public static void main(String[] args) {
//		SeleniumDriver selenium = new SeleniumDriver();
//		Locator d = new Locator(selenium.getDriver());
//		selenium.getDriver().navigate().to("http://www.baidu.com");
//		selenium.analyzeLog();
//		WebElement element = d.getElement("baidu_input");
//		element.sendKeys("百度");
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		selenium.getDriver().close();
//		selenium.getDriver().quit();
//	}
}
