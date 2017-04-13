/**
 * 
 */
package com.demo.test.plugins.arrow;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.demo.test.plugins.arrow.utils.ConfigReader;
import com.demo.test.utils.LogConfiguration;

/**
 * @author vera 2017年4月13日
 */
public class TestngRetry implements IRetryAnalyzer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.IRetryAnalyzer#retry(org.testng.ITestResult)
	 */
	static{
		LogConfiguration.initLog("TestngRetryPage_");
	}
	private static Logger logger = Logger.getLogger(TestngRetry.class);
	private int retryCount = 1;
	private static int maxRetryCount;

	static {
		ConfigReader config = ConfigReader.getInstance();
		maxRetryCount = config.getRetryCount();
		logger.info("retrycount=" + maxRetryCount);
		logger.info("sourceCodeDir=" + config.getSourceCodeDir());
		logger.info("sourceCodeEncoding=" + config.getSourceCodeEncoding());
	}

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount <= maxRetryCount) {
			String message = "Retry for [" + result.getName() + "] on class [" + result.getTestClass().getName()
					+ "] Retry " + retryCount + " times";
			logger.info(message);
			Reporter.setCurrentTestResult(result);
			Reporter.log("RunCount=" + (retryCount + 1));
			retryCount++;
			return true;
		}
		return false;
	}

	public static int getMaxRetryCount() {
		return maxRetryCount;
	}

	public int getRetryCount() {
		return retryCount;
	}

}
