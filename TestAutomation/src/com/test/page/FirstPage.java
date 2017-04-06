package com.test.page;

import org.openqa.selenium.WebDriver;

import com.test.base.Page;

public class FirstPage extends Page {

	public FirstPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	
	public void linkToMobileList() {
		this.getAction().moveToElement(this.getElement("手机数码京东通信")).perform();
		this.getElement("手机品类入口").click();
	}

}
