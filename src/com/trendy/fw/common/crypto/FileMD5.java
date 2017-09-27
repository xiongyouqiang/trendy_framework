package com.trendy.fw.common.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMD5 {
	private static Logger log = LoggerFactory.getLogger(FileMD5.class);

	private static MessageDigest messageDigest = null;
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			log.error("初始化失败，MessageDigest不支持MD5Util：" + e);
		}
	}

	public static String getFileMD5(String filePath) throws IOException {
		File file = new File(filePath);
		return getFileMD5(file);
	}

	synchronized public static String getFileMD5(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[1024 * 1024 * 10];
		int len = 0;
		while ((len = in.read(buffer)) > 0) {
			messageDigest.update(buffer, 0, len);
		}
		in.close();

		return CryptoKit.bytesToHex(messageDigest.digest());
	}
}
