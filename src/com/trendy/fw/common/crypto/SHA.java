package com.trendy.fw.common.crypto;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SHA {
	private static Logger log = LoggerFactory.getLogger(SHA.class);

	public static final String SHA = "SHA-1";
	public static final String SHA256 = "SHA-256";
	public static final String SHA384 = "SHA-384";
	public static final String SHA512 = "SHA-512";

	public static String getSHA(String algorithm, byte[] bytes) {
		String result = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(bytes);
			result = CryptoKit.bytesToHex(messageDigest.digest());
		} catch (Exception e) {
			log.error("生成SHA失败：", e);
		}
		return result;
	}

	public static String getSHA(String algorithm, String str) {
		return getSHA(algorithm, str, "");
	}

	public static String getSHA(String algorithm, String str, String key) {
		if (str == null) {
			return null;
		}

		if (key == null || key.trim().equals("")) {
			key = "";
		}

		String result = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(str.getBytes());
			result = CryptoKit.bytesToHex(messageDigest.digest(key.getBytes()));
		} catch (Exception e) {
			log.error("生成SHA失败：", e);
		}
		return result;
	}
}
