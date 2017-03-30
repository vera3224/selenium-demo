package com.test.util;

public class RandomUtil {
//	随机数的生成
	
	/*
	 * 返回一个 0-count（包含count）的随机数
	 * @param count
	 * @return
	 * */
	public int getRandom(int count) {
		return (int)Math.round(Math.random()*(count));
	}
	
	private String string="0123456789abcdefghijklmnopqrstuvwxyz";
	/*
	 * 从 string 中随机生成长度为length 的字符串
	 * @param lenght
	 * @return
	 * */
	public String getRandomString(int length) {
		StringBuffer sb =new StringBuffer();
		int len=string.length();
		for (int i = 0; i < length; i++) {
			sb.append(string.charAt(this.getRandom(len-1)));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RandomUtil ru =new RandomUtil();
		for (int i = 0; i <10; i++) {
			Log.logInfo(ru.getRandomString(6));
		}

	}

}
