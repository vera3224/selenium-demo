package com.test.testcases;

import java.util.Map;

import org.testng.annotations.Test;

import com.test.base.TestBase;

public class Test1 extends TestBase {
	
	@Test(dataProvider="providerMethod")
	public void testLogin(Map<String, String> param){
		
		System.out.println(param.get("username"));
		System.out.println(param.get("password"));
		System.out.println(param.get("inputValue"));
		System.out.println(param.get("url"));
	}

}
