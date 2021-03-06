package com.test.bean;

import com.test.util.ParseXml;


// 将config静态化，持久化
public class Config {
	public static String browser;
	public static int waitTime;
	
	/* static{}, 这种用法，代表在用到Config 这个类时， static{} 里面的内容会被执行一次，且最多一次
	 * 就是多次用到Config类，也只执行一次
	 * 一般用于记载一些配置文件， 类似于单例模式
	 * */
	static {
		ParseXml px = new ParseXml("config/config.xml");//给定config.xml的路径
		browser =px.getElementText("/config/browser");
		waitTime = Integer.valueOf(px.getElementText("/config/waitTime"));
	}
	
	public static void main(String[] args) {
		System.out.println(Config.browser);
		System.out.println(Config.waitTime);
	}
}
