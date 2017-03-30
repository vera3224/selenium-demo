package com.test.bean;

import java.util.Map;

import com.test.util.ParseXml;


//参照 config.xml的方式
public class Global {
	public static Map<String, String> global;
	
	static{
		ParseXml px = new ParseXml("test-data/global.xml");
		global = px.getChildrenInfoByElement(px.getElementObject("/*"));
	}

	public static void main(String[] args) {
		System.out.println(global);
	}
}
