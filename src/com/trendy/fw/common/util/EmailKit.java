package com.trendy.fw.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailKit {
	private final static String emailAddressRule = "[A-Za-z0-9]+([_\\-\\.]?[A-Za-z0-9]+[_\\-]?){0,10}@[A-Za-z0-9]+([\\.\\-]?[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})+";

	private static Pattern emailAddressPattern = Pattern.compile("^" + emailAddressRule + "$");

	/**
	 * 判断邮箱地址是否合法，不能为空、长度在100以内、包含“@”符号，及符合邮箱地址格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isValidEmailAddress(String email) {
		if (!StringKit.isValid(email) || email.length() > 100 || email.indexOf("@") <= 0) {
			return false;
		}
		Matcher matcher = emailAddressPattern.matcher(email);
		return matcher.matches();
	}

}
