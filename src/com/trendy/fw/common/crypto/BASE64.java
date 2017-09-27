package com.trendy.fw.common.crypto;

import org.apache.commons.codec.binary.Base64;

public class BASE64 {
	public static byte[] decodeBase64(String str) {
		return Base64.decodeBase64(str.getBytes());
	}

	public static String encodeBase64(byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}
}
