package com.trendy.fw.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.config.Constants;

public class EncodeKit {
	private static Logger log = LoggerFactory.getLogger(EncodeKit.class);

	/**
	 * 转换字符串为ISO
	 * 
	 * @param str
	 * @return
	 */
	public static String toIso(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(), Constants.CODE_ISO);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("字符串转换ISO出错：", e);
		}
		return str;
	}

	/**
	 * 转换字符串为GBK
	 * 
	 * @param str
	 * @return
	 */
	public static String toGbk(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(), Constants.CODE_GBK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("字符串转换GBK出错：", e);
		}
		return str;
	}

	/**
	 * 转换字符串为GBK
	 * 
	 * @param str
	 * @return
	 */
	public static String toUnicode(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(), Constants.CODE_UNICODE);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("字符串转换UTF8出错：", e);
		}
		return str;
	}

	public static String toSystemCode(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		if (Constants.SYSTEM_CODE.equals(Constants.CODE_ISO)) {
			return toIso(str);
		} else if (Constants.SYSTEM_CODE.equals(Constants.CODE_UNICODE)) {
			return toUnicode(str);
		} else {
			return toGbk(str);
		}
	}

	/**
	 * 将ISO字符串转换为UTF
	 * 
	 * @param str
	 * @return
	 */
	public static String iso2Unicode(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(Constants.CODE_ISO), Constants.CODE_UNICODE);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ISO转换UTF8出错：", e);
		}
		return str;
	}

	/**
	 * 将UTF字符串转换为ISO
	 * 
	 * @param str
	 * @return
	 */
	public static String unicode2Iso(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(Constants.CODE_UNICODE), Constants.CODE_ISO);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("UTF8转换ISO出错：", e);
		}

		return str;
	}

	/**
	 * 将ISO字符串转换为GBK
	 * 
	 * @param str
	 * @return
	 */
	public static String iso2Gbk(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(Constants.CODE_ISO), Constants.CODE_GBK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ISO转换GBK出错：", e);
		}
		return str;
	}

	/**
	 * 将GBK字符串转换为ISO
	 * 
	 * @param str
	 * @return
	 */
	public static String gbk2Iso(String str) {
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		try {
			str = new String(str.getBytes(Constants.CODE_GBK), Constants.CODE_ISO);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("GBK转换ISO出错：", e);
		}

		return str;
	}

	/**
	 * 将UTF字符串转换为GBK
	 * 
	 * @param str
	 * @return
	 */
	public static String unicode2Gbk(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(Constants.CODE_UNICODE), Constants.CODE_GBK);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("UTF8转换GBK出错：", e);
		}
		return str;
	}

	/**
	 * 将GBK字符串转换为UTF
	 * 
	 * @param str
	 * @return
	 */
	public static String gbk2Unicode(String str) {
		if (!StringKit.isValid(str)) {
			return "";
		}
		try {
			str = new String(str.getBytes(Constants.CODE_GBK), Constants.CODE_UNICODE);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("GBK转换UTF8出错：", e);
		}

		return str;
	}
}
