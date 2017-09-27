package com.trendy.fw.common.util;

import java.text.DecimalFormat;

public class NumberKit {
	private static final String POINT = ".";

	/**
	 * 判断字符串是否正整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPositiveInteger(String str) {
		boolean result = false;
		try {
			if (StringKit.isValid(str)) {
				if (str.indexOf(POINT) < 0) {
					if (Integer.parseInt(str) > 0) {
						result = true;
					}
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 判断字符串是否由数字组成
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllDigit(String str) {
		boolean result = false;
		try {
			if (StringKit.isValid(str)) {
				char[] c = str.toCharArray();
				for (int i = 0; i < c.length; i++) {
					if (Character.isDigit(c[i])) {
						result = true;
					} else {
						result = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 格式化double类型数字
	 * 
	 * @param d
	 *            数字
	 * @param scale
	 *            小数点后保留长度
	 * @return
	 */
	public static String formatDouble(double d, int scale) {
		String result = "";
		try {
			DecimalFormat df = new DecimalFormat("0." + fillZero(scale));
			result = df.format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 格式化成double类型的字符串
	 * 
	 * @param d
	 *            数字
	 * @param scale
	 *            小数点后保留长度
	 * @return
	 */
	public static String formatDouble(String d, int scale) {
		String result = "";
		try {
			double temp = Double.parseDouble(d);
			result = formatDouble(temp, scale);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 填充0
	 * 
	 * @param scale
	 *            长度
	 * @return
	 */
	public static String fillZero(int scale) {
		String result = "";
		for (int i = 0; i < scale; i++) {
			result = result + "0";
		}
		return result;
	}

	/**
	 * 在int数字前面填充0
	 * 
	 * @param number
	 *            数字
	 * @param scale
	 *            要求长度
	 * @return
	 */
	public static String fillPreZero(int number, int scale) {
		String result = "";
		try {
			String temp = String.valueOf(number);
			int iLen = scale - temp.length();
			if (iLen >= 0) {
				for (int i = 0; i < iLen; i++) {
					temp = "0" + temp;
				}
			}
			result = temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 在int数字前面填充0
	 * 
	 * @param number
	 *            数字
	 * @param scale
	 *            要求长度
	 * @return
	 */
	public static String fillPreZero(String number, int scale) {
		return fillPreZero(Integer.parseInt(number), scale);
	}
}
