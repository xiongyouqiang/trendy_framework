package com.trendy.fw.common.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DES {
	private static Logger log = LoggerFactory.getLogger(DES.class);

	private final static String DES = "DES";

	/**
	 * 生成密钥文件
	 * 
	 * @param filePath
	 *            生成文件路径
	 */
	public static void createKey(String filePath) {
		ObjectOutputStream objectOutputStream = null;
		try {
			// 得到密钥的实例
			KeyGenerator kg = KeyGenerator.getInstance(DES);
			SecureRandom sr = new SecureRandom();
			kg.init(sr);
			SecretKey key = kg.generateKey();
			// 将生成的密钥对象写入文件。
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
			objectOutputStream.writeObject(key);
		} catch (Exception e) {
			log.error("DES生成Key异常：", e);
		} finally {
			try {
				objectOutputStream.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 根据文件生成Key实例
	 * 
	 * @param filePath
	 *            密钥文件路径
	 * @return
	 */
	public static Key getKeyByFile(String filePath) {
		Key key = null;
		ObjectInputStream objectInputStream = null;
		try {
			// 将生成的密钥对象从文件中读取出来，然后再强制转换成一个密钥对象。
			File file = new File(filePath);
			if (file.exists()) {
				objectInputStream = new ObjectInputStream(new FileInputStream(file));
			} else {
				objectInputStream = new ObjectInputStream(DES.class.getResourceAsStream(filePath));
			}
			key = (Key) objectInputStream.readObject();
		} catch (Exception e) {
			log.error("DES获取Key异常：", e);
		} finally {
			try {
				objectInputStream.close();
			} catch (Exception e) {
			}
		}
		return key;
	}

	/**
	 * 加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @return
	 */
	public static byte[] encrypt(byte[] bytes, Key key) {
		byte[] encryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("DES加密异常：", e);
		}
		return encryptedData;
	}

	/**
	 * 加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            key的byte数组
	 * @return
	 */
	public static byte[] encrypt(byte[] bytes, byte[] key) {
		byte[] encryptedData = null;
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			encryptedData = encrypt(bytes, keyFactory.generateSecret(dks));
		} catch (Exception e) {
			log.error("DES加密异常：", e);
		}
		return encryptedData;
	}

	/**
	 * 加密
	 * 
	 * @param source
	 *            源字符串
	 * @param key
	 *            Key实例
	 * @return
	 */
	public static String encrypt(String source, Key key) {
		byte[] bytes = encrypt(source.getBytes(), key);
		return CryptoKit.bytesToHex(bytes);
	}

	/**
	 * 加密
	 * 
	 * @param source
	 *            源字符串
	 * @param key
	 *            key字符串
	 * @return
	 */
	public static String encrypt(String source, String key) {
		byte[] bytes = encrypt(source.getBytes(), getKey(key));
		return CryptoKit.bytesToHex(bytes);
	}

	/**
	 * 加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @return byte数组
	 */
	public static byte[] decrypt(byte[] bytes, Key key) {
		byte[] decryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.DECRYPT_MODE, key);// 使用私钥解密
			decryptedData = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("DES解密异常：", e);
		}
		return decryptedData;
	}

	/**
	 * 解密
	 * 
	 * @param source
	 *            源字符串
	 * @param key
	 *            Key实例
	 * @return
	 */
	public static String decrypt(String source, Key key) {
		byte[] src = CryptoKit.hexToBytes(source);
		byte[] bytes = decrypt(src, key);
		return new String(bytes);
	}

	/**
	 * 解密
	 * 
	 * @param source
	 *            源字符串
	 * @param key
	 *            key字符串
	 * @return
	 */
	public static String decrypt(String source, String key) {
		byte[] src = CryptoKit.hexToBytes(source);
		byte[] bytes = decrypt(src, getKey(key));
		return new String(bytes);
	}

	/**
	 * 通过字符串生成Key实例
	 * 
	 * @param keyContent
	 *            key字符串，长度必须为8的倍数
	 * @return
	 */
	public static Key getKey(String keyContent) {
		Key key = null;
		try {
			DESKeySpec dks = new DESKeySpec(keyContent.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			key = keyFactory.generateSecret(dks);
		} catch (Exception e) {
			log.error("DES加密异常：", e);
		}
		return key;
	}
}
