package com.trendy.fw.common.crypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CryptoKit {
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static String hexString = "0123456789abcdef";
	private static int paddingBlockSize = 32;

	/**
	 * byte数组转化为16进制的String
	 * 
	 * @param bytes
	 * @param m
	 * @param n
	 * @return
	 */
	public static String bytesToHex(byte bytes[], int m, int n) {
		StringBuffer sb = new StringBuffer(2 * n);
		int k = m + n;
		for (int i = m; i < k; i++) {
			appendHexPair(bytes[i], sb);
		}
		return sb.toString();
	}

	public static String bytesToHex(byte bytes[]) {
		return bytesToHex(bytes, 0, bytes.length);
	}

	private static void appendHexPair(byte bt, StringBuffer sb) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		sb.append(c0);
		sb.append(c1);
	}

	public static byte[] hexToBytes(String hex) {
		int len = (hex.length() / 2);
		byte[] bytes = new byte[len];
		char[] charArray = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			bytes[i] = (byte) (toByte(charArray[pos]) << 4 | toByte(charArray[pos + 1]));
		}
		return bytes;
	}

	private static byte toByte(char c) {
		return (byte) hexString.indexOf(c);
	}

	public static byte[] addPadding(byte[] bytes) {
		int length = bytes.length;

		List<Byte> list = new ArrayList<Byte>();
		for (byte b : bytes) {
			list.add(b);
		}

		int amountToPad = paddingBlockSize - (length % paddingBlockSize);
		if (amountToPad == 0) {
			amountToPad = paddingBlockSize;
		}

		for (int i = 0; i < amountToPad; i++) {
			list.add((byte) (amountToPad & 0xFF));
		}
		byte[] result = new byte[list.size()];
		for (int j = 0; j < list.size(); j++) {
			result[j] = list.get(j);
		}
		return result;
	}

	public static byte[] removePadding(byte[] bytes) {
		int pad = (int) bytes[bytes.length - 1];
		if (pad < 1 || pad > paddingBlockSize) {
			pad = 0;
		}
		return Arrays.copyOfRange(bytes, 0, bytes.length - pad);
	}	
}
