package com.trendy.fw.common.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5 {
	private static Logger log = LoggerFactory.getLogger(MD5.class);

	private static MessageDigest messageDigest = null;
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			log.error("初始化失败，MessageDigest不支持MD5Util：", e);
		}
	}

	public static String getMD5(byte[] bytes) {
		messageDigest.update(bytes);
		return CryptoKit.bytesToHex(messageDigest.digest());
	}

	/**
	 * 使用信息摘要MD5单向加密技术对字符串进行加密。
	 * <p>
	 * 对字符串进行加密，生成32位的加密字符串，默认加密Key为""
	 * <p>
	 * <p>
	 * 
	 * @param data
	 *            String 源串
	 * @return String 加密串
	 */
	synchronized static public final String getMD5(String str) {
		return getMD5(str, "");
	}

	/**
	 * 使用信息摘要MD5单向加密技术对字符串进行加密。
	 * <p>
	 * 对字符串进行加密，生成32位的加密字符串，<b>加密串的key可自定义</b>
	 * <P>
	 * 
	 * @param data
	 *            String 源串
	 * @return String 加密串
	 */
	synchronized static public final String getMD5(String str, String key) {
		if (str == null) {
			return null;
		}

		if (key == null || key.trim().equals("")) {
			key = "";
		}

		messageDigest.update(str.getBytes());
		return CryptoKit.bytesToHex(messageDigest.digest(key.getBytes()));
	}
}
