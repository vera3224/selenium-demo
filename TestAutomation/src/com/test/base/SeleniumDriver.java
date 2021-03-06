package com.test.base;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.test.bean.Config;
import com.test.util.Log;

public class SeleniumDriver {
	private static WebDriver driver;

	public WebDriver getDriver() {
		return driver;
	}
	
	public SeleniumDriver() {
		// TODO Auto-generated constructor stub
		this.initialDriver();
	}

	private void initialDriver() {
		// TODO Auto-generated method stub
		if ("firefox".equals(Config.browser)) {
			driver= new FirefoxDriver();
		}else if ("ie".equals(Config.browser)) {
			System.setProperty("webdriver.ie.driver", "files/IEDriverServer64.exe");
			DesiredCapabilities capabilities= DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			driver =new InternetExplorerDriver(capabilities);
		}else if ("chrome".equals(Config.browser)) {
//			System.setProperty("webdriver.chrome.driver", "files/chromedriver.exe");
//			抓取页面上JS error
			/*
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.BROWSER, Level.ALL);
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
//			ChromeOptions options= new ChromeOptions();
//			options.addArguments("--test-type");
			driver = new ChromeDriver(capabilities);
			LogEntries logEntries=driver.manage().logs().get(LogType.BROWSER);
			for(LogEntry entry:logEntries) {
				Log.logInfo(new Date(entry.getTimestamp())+""+entry.getLevel()+""+entry.getMessage());
			}
			*/
			driver=new ChromeDriver();
		}else {
			Log.logInfo(Config.browser+" 的值不正确，请检查！");
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Config.waitTime, TimeUnit.SECONDS);
		
	}
	public void analyzeLog() {
		LogEntries logEntries=driver.manage().logs().get(LogType.BROWSER);
		for(LogEntry entry:logEntries) {
			Log.logInfo(new Date(entry.getTimestamp())+""+entry.getLevel()+""+entry.getMessage());
		}
	}
//	
//	public static void main(String[] args) {
//		SeleniumDriver selenium = new SeleniumDriver();
//		WebDriver driver =selenium.getDriver();
//		driver.navigate().to("http://www.baidu.com.cn/");
//		analyzeLog();
//		
//		driver.close();
//		driver.quit();
//	}

}
