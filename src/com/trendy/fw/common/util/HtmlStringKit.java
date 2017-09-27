package com.trendy.fw.common.util;

public class HtmlStringKit {

	/**
	 * 将字符串中的<转为&lt; >转为&gt;
	 * 
	 * @param oldstr
	 * @return
	 */
	public static String chopTag(String str) {
		if (!StringKit.isValid(str)) {
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
		}
		return str;
	}

	/**
	 * 将字符串中的‘转为’，\"转为“
	 * 
	 * @param oldstr
	 * @return
	 */
	public static String chopQuot(String str) {
		if (!StringKit.isValid(str)) {
			str = str.replaceAll("\'", "`");
			str = str.replaceAll("\"", "`");

		}
		return str;
	}

	/**
	 * 将字符串格式化成 HTML 代码输出
	 * 
	 * @param str
	 *            要格式化的字符串
	 * @return 格式化后的字符串
	 */
	static public String toHtml(String str) {
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\r\n", "<br>");
		str = str.replaceAll("\n", "<br>");
		str = str.replaceAll("\t", "&nbsp&nbsp&nbsp&nbsp");
		str = str.replaceAll(" ", "&nbsp");

		return str;
	}
}
