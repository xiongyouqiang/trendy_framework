package com.trendy.fw.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringKit {

	/**
	 * 判断字符串是否为空字符。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String str) {
		boolean result = false;
		if (str != null && str.equals("")) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断字符串是否为null。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null ? true : false;
	}

	/**
	 * 判断字符串是否为空字符串或者null。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isValid(String str) {
		return !isNull(str) && !isBlank(str);
	}

	/**
	 * 判断字符串是否包含恶意字符
	 * 
	 * @param str
	 * @return
	 */
	static public boolean isBaleful(String str) {
		if (!isValid(str)) {
			return false;
		}

		String[] errorArray = { "\'", "\"", "\\", "/", ",", ":", "?", "<", ">" };

		for (int i = 0; i < errorArray.length; i++) {
			if (str.indexOf(errorArray[i]) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是否非空或含有恶意字符
	 * 
	 * @param str
	 * @return
	 */
	static public boolean isVerified(String str) {
		if (!isValid(str)) {
			return false;
		}

		if (isBaleful(str)) {
			return false;
		}

		return true;
	}

	/**
	 * 过滤字符串中的对象
	 * 
	 * @param str
	 * @return
	 */
	public static String validStr(String str) {
		if (isValid(str)) {
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 将数组转换成字符串
	 * 
	 * @param array
	 * @param regex
	 *            连接符
	 * @return
	 */
	static public String array2String(String[] array, String regex) {
		StringBuffer sb = new StringBuffer();
		for (String str : array) {
			if (sb.length() > 0) {
				sb.append(regex);
			}
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 获取子字符串，按长度
	 * 
	 * @param str
	 * @param length
	 *            长度
	 * @param suffix
	 *            截断后补充后缀
	 * @return
	 */
	public static String substring(String str, int length, String suffix) {
		if (str == null || str.trim().length() == 0) {
			return "";
		}

		if (str.length() <= length) {
			return str;
		}

		String result = str.substring(0, length) + suffix;
		return result;
	}

	/**
	 * 获取子字符串，按位bit
	 * 
	 * @param str
	 * @param length
	 *            长度
	 * @param suffix
	 *            截断后补充后缀
	 * @return
	 */
	public static String substringBit(String str, int length, String suffix) {
		if (str == null || str.trim().length() == 0) {
			return "";
		}

		try {
			byte[] bytes = str.getBytes("Unicode");
			int n = 0; // 表示当前的字节数
			int subLen = 2; // 要截取的字节数，从第3个字节开始
			for (; subLen < bytes.length && n < length; subLen = subLen + 2) {
				n++;
				if (bytes[subLen] != 0) {
					n++;
					if (n > length) {
						break;
					}
				}
			}

			if (subLen == bytes.length) {
				return str;
			}
			return new String(bytes, 0, subLen, "Unicode") + suffix;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取子字符串，含html标签，按位bit，填补html标签内容在字符串前后
	 * 
	 * @param str
	 * @param length
	 *            长度
	 * @param suffix
	 *            截断后补充后缀
	 * @return
	 */
	public static String substringHtmlBit(String str, int length, String suffix) {
		String result = "";
		if (str == null || str.trim().length() == 0) {
			return "";
		}

		String regex = "<.[^>]*>";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		List<String> htmlTagList = new ArrayList<String>();
		while (matcher.find()) {
			htmlTagList.add(matcher.group(0));
		}
		String temp = matcher.replaceAll("");

		try {
			byte[] bytes = temp.getBytes("Unicode");
			int n = 0; // 表示当前的字节数
			int subLen = 2; // 要截取的字节数，从第3个字节开始
			for (; subLen < bytes.length && n < length; subLen = subLen + 2) {
				n++;
				if (bytes[subLen] != 0) {
					n++;
					if (n > length) {
						break;
					}
				}
			}

			if (subLen == bytes.length) {
				result = temp;
			} else {
				result = new String(bytes, 0, subLen, "Unicode") + suffix;
			}
			String htmlStr = "";
			for (int i = 0; i < htmlTagList.size() / 2; i++) {
				htmlStr = htmlStr + htmlTagList.get(i);
			}
			result = htmlStr + result;
			htmlStr = "";
			for (int i = htmlTagList.size(); i > htmlTagList.size() / 2; i--) {
				htmlStr = htmlTagList.get(i - 1) + htmlStr;
			}
			result = result + htmlStr;
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取子字符串，含html标签，按位bit并过滤html标签
	 * 
	 * @param str
	 * @param length
	 *            长度
	 * @param suffix
	 *            截断后补充后缀
	 * @return
	 */
	public static String substringHtmlBit2(String str, int length, String suffix) {
		return substringBit(filterHtmlTags(str), length, suffix);
	}

	/**
	 * 获取字符串长度，按位bit
	 * 
	 * @param str
	 * @return
	 */
	public static int stringBitLength(String str) {
		if (str == null || str.trim().length() == 0) {
			return 0;
		}

		int length = 0;
		try {
			byte[] bytes = str.getBytes("Unicode");
			for (int i = 2; i < bytes.length; i = i + 2) {
				length++;
				if (bytes[i] != 0) {
					length++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return length;
	}

	/**
	 * 获取字符串长度，含html标签，按位bit
	 * 
	 * @param str
	 * @return
	 */
	public static int stringHtmlBitLength(String str) {
		if (str == null || str.trim().length() == 0) {
			return 0;
		}

		Pattern pattern = Pattern.compile("<.[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		String temp = matcher.replaceAll("");

		int length = 0;
		try {
			byte[] bytes = temp.getBytes("Unicode");
			for (int i = 2; i < bytes.length; i = i + 2) {
				length++;
				if (bytes[i] != 0) {
					length++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return length;
	}

	/**
	 * 过滤字符串中的html标签
	 * 
	 * @param str
	 * @return
	 */
	public static String filterHtmlTags(String str) {
		return str.replaceAll("<.*?>", "");
	}

	/**
	 * 将字符串中的半角字符转换成全角字符
	 * 
	 * @param str
	 * @return
	 */
	public static String toSBC(String str) {
		char c[] = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 将字符串中的全角字符转换成半角字符
	 * 
	 * @param input
	 * @return
	 */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	public static String upperFirstChar(String input) {
		String result = "";
		if (input == null || input.trim().length() == 0) {
			return "";
		}

		String firstStr = input.substring(0, 1);
		String restStr = input.substring(1);
		result = new StringBuffer(firstStr.toUpperCase()).append(restStr).toString();

		return result;
	}

	public static String lowerFirstChar(String input) {
		String result = "";
		if (input == null || input.trim().length() == 0) {
			return "";
		}

		String firstStr = input.substring(0, 1);
		String restStr = input.substring(1);
		result = new StringBuffer(firstStr.toLowerCase()).append(restStr).toString();

		return result;
	}

	public static boolean isMatch(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	public static boolean isMatch(List<String> list, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = null;
		for (String str : list) {
			matcher = pattern.matcher(str);
			if (!matcher.find()) {
				return false;
			}
		}
		return true;
	}
}
