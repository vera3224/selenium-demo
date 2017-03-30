package com.test.bean;

public class DBInfo {
	private String driver;
	private String host ;
	private String port;
	private String user;
	private String pwd;
	private String dataBase;
	
	public DBInfo() {
		this.driver="com.mysql.jdbc.Driver";
		this.host = "localhost";
		this.port = "3306";
		this.user = "root";
		this.pwd = "zq3224";
		this.dataBase = "TestAutomation";		
	}
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

}
