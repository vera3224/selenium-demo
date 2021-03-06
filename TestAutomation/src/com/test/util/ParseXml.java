package com.test.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXml {
	/*
	 * 解析xml文件，需要知道xml文件的路径，然后根据路径加载xml文件后，生成一个Document 的对象
	 * 先定义两个变量 String filePath，Document document
	 * 然后再定义一个load 方法，用来加载xml文件。，从而产生document对象
	 * */
	
	private String filePath;
	private Document document;
	
	/*
	 * 构造器用来 new ParseXml对象时，传一个filePath 的参数进来，初始化filePath的值
	 * 调用load方法，在ParseXml对象产生时就会产生一个document对象
	 * */
	public ParseXml(String filePath) {
		// TODO Auto-generated constructor stub
		this.filePath=filePath;
		this.load(this.filePath);
	}

	/*
	 * 用来加载xml文件，并且产生一个document对象
	 * */
	private void load(String filePath) {
		// TODO Auto-generated method stub
		File file =new File(filePath);
		if (file.exists()) {
			SAXReader saxReader= new SAXReader();
			try {
				document = saxReader.read(file);
			} catch (DocumentException e) {
				// TODO: handle exception
				System.out.println("文件加载异常"+ filePath);
			}
		}else {
			System.out.println("文件不存在"+filePath);
		}
	}
	
	/*Document 对象产生后， dom4j提供一种可以用xpath来解析xml的方法
	 * @param elementPath 是一个xpath路径，比如“／config／driver”
	 * @return 返回的是一个节点 Element对象
	 * */
	public Element getElementObject(String elementPath) {
		return (Element)document.selectSingleNode(elementPath);
	}
	
	/* 添加 用xpath 来判断一个节点对象是否存在
	 * */
	public boolean isExist(String elementPath) {
		boolean flag= false;
		Element element=this.getElementObject(elementPath);
		if (element!=null) {
			flag=true;
		}
		return flag;
	}
	/* 用xpath 来取得节点的值
	 * */
	public String getElementText(String elementPath) {
		Element element=this.getElementObject(elementPath);
		if (element!=null) {
			return element.getText().trim();
		}else {
			return null;
		}
	}
	
//	针对多个结点
	@SuppressWarnings("unchecked")
	public List<Element> getElementObjects(String elementPath){
		return document.selectNodes(elementPath);
	}
//	把子节点的信息再全部取到
	@SuppressWarnings("unchecked")
	public Map<String, String> getChildrenInfoByElement(Element element){
		Map<String, String> map = new HashMap<String,String>();
		List<Element> children = element.elements();
		for (Element e : children) {
			map.put(e.getName(), e.getText());
		}
		return map;

	}
	

	
//	测试这些方法的用途
	public static void main(String[] args) {
		ParseXml px = new ParseXml("config/config.xml");//给定config.xml的路径
		String browser =px.getElementText("/config/browser");
		System.out.println(browser);
		String waitTime = px.getElementText("/config/waitTime");
		System.out.println(waitTime);
 	}

}
