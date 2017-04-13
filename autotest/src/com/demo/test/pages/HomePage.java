/**
 * 
 */
package com.demo.test.pages;

import org.openqa.selenium.By;

/**
 * @author vera
 *2017年4月13日
 */
public class HomePage {
	/**用户名显示区域*/
	public static final By HP_TEXT_USERNAME= By.xpath("//blockquote/b");
	/**Flights按钮*/
	public static final By HP_BUTTON_FLIGHTS = By.xpath("//*[@src='/WebTours/images/flights.gif']");
	/**Itinerary按钮*/
	public static final By HP_BUTTON_ITINERARY = By.xpath("//*[@src='/WebTours/images/itinerary.gif']");
	/**Home按钮*/
	public static final By HP_BUTTON_HOME = By.xpath("//*[@src='/WebTours/images/in_home.gif']");
	/**Sign Off按钮*/
	public static final By HP_BUTTON_SIGNOFF = By.xpath("//*[@src='/WebTours/images/signoff.gif']");
	/**首页完整文本*/
	public static final By HP_TEXT_HOME= By.xpath("//blockquote");

}
