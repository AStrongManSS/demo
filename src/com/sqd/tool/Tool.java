package com.sqd.tool;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {
	
	/**
	 * 日期時間格式: yyyy-MM-dd HH:mm:ss
	 */
	public static final String TIMESTR24 = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期時間格式: yyyy-MM-dd hh:mm:ss
	 */
	public static final String TIMESTR12 = "yyyy-MM-dd hh:mm:ss";
	
	/***
	 * 将字符串转换为数字
	 * @param clazz 要转换的数据类型
	 * @param numStr 要转换的字符串
	 * @param defaultVal 默认值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Number> T valueOf(Class<T> clazz ,String numStr,T defaultVal){
		try {
			Method valueOf = clazz.getMethod("valueOf", String.class);
			return (T)valueOf.invoke(null, numStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultVal;
	}
	
	/***
	 * 将时间转换成字符串
	 * @param timeFormat
	 * @param date
	 * @return
	 */
	public static String timeToString(String timeFormat,Date date){
		if(timeFormat == null|| date == null) return "";
		try {
			return new SimpleDateFormat(timeFormat).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/***
	 * 下划线命名转为驼峰命名
	 * 
	 * @param para
	 *        下划线命名的字符串
	 */
	public static String UnderlineToHump(String para) {
		StringBuilder result = new StringBuilder();
		String a[] = para.split("_");
		for (String s : a) {
			if (result.length() == 0) {
				result.append(s.toLowerCase());
			} else {
				result.append(s.substring(0, 1).toUpperCase());
				result.append(s.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}

	/***
	 * 驼峰命名转为下划线命名
	 * 
	 * @param para
	 *        驼峰命名的字符串
	 */
	public static String HumpToUnderline(String para) {
		StringBuilder sb = new StringBuilder(para);
		int temp = 0;// 定位
		for (int i = 1; i < para.length(); i++) {
			if (Character.isUpperCase(para.charAt(i))) {
				sb.insert(i + temp, "_");
				temp += 1;
			}
		}
		return sb.toString().toLowerCase();
	}

	public static void main(String[] args) {
		System.out.println(Tool.HumpToUnderline("SSUserDaoAAA"));
	}
}
