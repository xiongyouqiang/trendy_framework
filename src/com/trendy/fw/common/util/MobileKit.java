package com.trendy.fw.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileKit {
	private final static String mobileNumberRule = "(1(3\\d|5[0-3,5-9]|8[0-3,5-9]|4[57]|7[0,6-8])\\d{8})";//国内
	private final static String mobileNumberHKMacaoRule = "\\d{8}";//港澳
	
	private final static String zoneRule = "((\\+86)|(86))?";//?国内电话区号可选
	private final static String zoneHKMacaoRule = "(((\\+85)|(85))[2|3])";//港澳电话区号必传

	private static Pattern mobileNumberChinaPattern = Pattern.compile("^" + zoneRule + mobileNumberRule + "$");
	private static Pattern mobileNumberHKMacaoPattern = Pattern.compile("^" + zoneHKMacaoRule + mobileNumberHKMacaoRule + "$");
	
	private static Pattern worldMobileNumberPattern = Pattern.compile("^(" + zoneRule + mobileNumberRule 
			+ ")|(" + zoneHKMacaoRule + mobileNumberHKMacaoRule + ")$"); 
	
	/**
	 * 检查港澳电话号码是否合法
	 * 
	 * @param mobile
	 * @return
	 */
	private static boolean isValidMobileNumberHKMacao(String mobile) {
		Matcher matcher = mobileNumberHKMacaoPattern.matcher(mobile);
		return matcher.matches();
	}
	
	/**
	 * 检查国内电话号码是否合法
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isValidMobileNumber(String mobile) {
		Matcher matcher = mobileNumberChinaPattern.matcher(mobile);
		return matcher.matches();
	}
	
	/**
	 * 带区号的电话号码是否合法
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isValidZoneMobileNumber(String mobile) {
		Matcher matcher = worldMobileNumberPattern.matcher(mobile);
		return matcher.matches();
	}
}
