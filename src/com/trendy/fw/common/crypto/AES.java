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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AES {
	private static Logger log = LoggerFactory.getLogger(AES.class);

	private final static String AES = "AES";
	private final static String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";// 电子密码本模式，PKCS5填充算法
	private final static String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";// 加密分组链接模式，PKCS5填充算法
	private final static String CIPHER_ALGORITHM_CBC_NOPADDING = "AES/CBC/NoPadding";// 加密分组链接模式，不填充算法

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
			KeyGenerator kg = KeyGenerator.getInstance(AES);
			SecureRandom sr = new SecureRandom();
			kg.init(sr);
			SecretKey key = kg.generateKey();
			// 将生成的密钥对象写入文件。
			objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(filePath)));
			objectOutputStream.writeObject(key);
		} catch (Exception e) {
			log.error("AES生成Key异常：", e);
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
			objectInputStream = new ObjectInputStream(new FileInputStream(new File(filePath)));
			key = (Key) objectInputStream.readObject();
		} catch (Exception e) {
			log.error("AES获取Key异常：", e);
		} finally {
			try {
				objectInputStream.close();
			} catch (Exception e) {
			}
		}
		return key;
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
			key = new SecretKeySpec(keyContent.getBytes(), AES);
		} catch (Exception e) {
			log.error("AES加密异常：", e);
		}
		return key;
	}

	/**
	 * 以AES基本方式加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @return
	 */
	public static byte[] encrypt(byte[] bytes, Key key) {
		byte[] target = null;
		try {
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			target = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("AES加密异常：", e);
		}
		return target;
	}

	/**
	 * 以AES基本方式加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            key的byte数组
	 * @return
	 */
	public static byte[] encrypt(byte[] bytes, byte[] key) {
		byte[] target = null;
		try {
			SecretKeySpec dks = new SecretKeySpec(key, AES);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(AES);
			target = encrypt(bytes, keyFactory.generateSecret(dks));
		} catch (Exception e) {
			log.error("AES加密异常：", e);
		}
		return target;
	}

	/**
	 * 以AES基本方式加密
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
	 * 以AES基本方式加密
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
	 * 以AES基本方式加密
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
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.DECRYPT_MODE, key);// 使用私钥解密
			decryptedData = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("AES解密异常：", e);
		}
		return decryptedData;
	}

	/**
	 * 以AES基本方式解密
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
	 * 以AES基本方式解密
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
	 * 以AES ECB方式加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @return
	 */
	public static byte[] encryptByECB(byte[] bytes, Key key) {
		byte[] target = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			target = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("AES加密异常：", e);
		}
		return target;
	}

	/**
	 * 以AES ECB方式解密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @return
	 */
	public static byte[] decryptByECB(byte[] bytes, Key key) {
		byte[] decryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
			cipher.init(Cipher.DECRYPT_MODE, key);// 使用私钥解密
			decryptedData = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("AES解密异常：", e);
		}
		return decryptedData;
	}

	/**
	 * 以AES CBC方式加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @param iv
	 *            IV参量，必须为16位的byte数组
	 * @return
	 */
	public static byte[] encryptByCBC(byte[] bytes, Key key, byte[] iv) {
		byte[] target = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
			target = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("AES加密异常：", e);
		}
		return target;
	}

	/**
	 * 以AES CBC方式解密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @param iv
	 *            IV参量，必须为16位的byte数组
	 * @return
	 */
	public static byte[] decryptByCBC(byte[] bytes, Key key, byte[] iv) {
		byte[] decryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv, 0, 16);
			cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);// 使用私钥解密
			decryptedData = cipher.doFinal(bytes);
		} catch (Exception e) {
			log.error("AES解密异常：", e);
		}
		return decryptedData;
	}

	/**
	 * 以AES CBC非补码方式加密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @param iv
	 *            IV参量，必须为16位的byte数组
	 * @return
	 */
	public static byte[] encryptByCBCNoPadding(byte[] bytes, Key key, byte[] iv) {
		byte[] target = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NOPADDING);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
			target = cipher.doFinal(CryptoKit.addPadding(bytes));
		} catch (Exception e) {
			log.error("AES加密异常：", e);
		}
		return target;
	}

	/**
	 * 以AES CBC非补码方式解密
	 * 
	 * @param bytes
	 *            源byte数组
	 * @param key
	 *            Key实例
	 * @param iv
	 *            IV参量，必须为16位的byte数组
	 * @return
	 */
	public static byte[] decryptByCBCNoPadding(byte[] bytes, Key key, byte[] iv) {
		byte[] decryptedData = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NOPADDING);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv, 0, 16);
			cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);// 使用私钥解密
			decryptedData = cipher.doFinal(bytes);
			decryptedData = CryptoKit.removePadding(decryptedData);
		} catch (Exception e) {
			log.error("AES解密异常：", e);
		}
		return decryptedData;
	}
}
