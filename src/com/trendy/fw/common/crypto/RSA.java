package com.trendy.fw.common.crypto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.util.FileKit;

public class RSA {
	private static Logger log = LoggerFactory.getLogger(RSA.class);

	/**
	 * 加密算法RSA
	 */
	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 获取密匙对（公钥和私钥）
	 * 
	 * @return
	 */
	public static KeyPair getKeyPair() {
		KeyPair keyPair = null;
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(1024);
			keyPair = keyPairGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			log.error("RSA genKeyPair Error :", e);
		}
		return keyPair;
	}

	/**
	 * 获取密匙对的Map
	 * 
	 * @return
	 */
	public static Map<String, Object> getKeyPairMap() {
		Map<String, Object> keyMap = new HashMap<String, Object>(2);

		KeyPair keyPair = getKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 生成密匙对文件，密匙对以Base64方式编码
	 * 
	 * @param filePath
	 *            生成路径
	 * @throws Exception
	 */
	public static void createKeyPair(String filePath) throws Exception {
		Map<String, Object> keyMap = getKeyPairMap();
		FileKit.writeFile(filePath + PUBLIC_KEY + ".key", getPublicKeyContent(keyMap), Constants.SYSTEM_CODE);
		FileKit.writeFile(filePath + PRIVATE_KEY + ".key", getPrivateKeyContent(keyMap), Constants.SYSTEM_CODE);
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            生成签名的数据
	 * @param privateKeyContent
	 *            私钥内容，base64编码
	 * @return
	 */
	public static byte[] sign(byte[] data, String privateKeyContent) {
		PrivateKey privateKey = getPrivateKey(privateKeyContent);
		return sign(data, privateKey);
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            生成签名的数据
	 * @param privateKey
	 *            密钥
	 * @return
	 */
	public static byte[] sign(byte[] data, PrivateKey privateKey) {
		byte[] result = null;
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateKey);
			signature.update(data);
			result = signature.sign();
		} catch (Exception e) {
			log.error("RSA sign Error :", e);
		}
		return result;
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            检验内容
	 * @param publicKeyContent
	 *            公钥内容
	 * @param sign
	 *            签名内容
	 * @return
	 */
	public static boolean verify(byte[] data, String publicKeyContent, byte[] sign) {
		PublicKey publicKey = getPublicKey(publicKeyContent);
		return verify(data, publicKey, sign);
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            检验内容
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            签名内容
	 * @return
	 */
	public static boolean verify(byte[] data, PublicKey publicKey, byte[] sign) {
		boolean result = false;
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (Exception e) {
			log.error("RSA verify Error :", e);
		}
		return result;
	}

	/**
	 * 私钥解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKeyContent
	 *            私钥内容，Base64编码
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKeyContent) {
		PrivateKey privateKey = getPrivateKey(privateKeyContent);
		return decryptByPrivateKey(encryptedData, privateKey);
	}

	/**
	 * 私钥解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKeyContent
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, PrivateKey privateKey) {
		Cipher cipher = getCipherByKey(Cipher.DECRYPT_MODE, privateKey);
		return decryptByCipher(encryptedData, cipher);
	}

	/**
	 * 用Ciper解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param cipher
	 *            Ciper实例
	 * @return
	 */
	private static byte[] decryptByCipher(byte[] encryptedData, Cipher cipher) {
		byte[] decryptedData = null;
		try {
			int inputLen = encryptedData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			decryptedData = out.toByteArray();
			out.close();
		} catch (Exception e) {
			log.error("RSA decryptByCipher Error :", e);
		}
		return decryptedData;
	}

	/**
	 * 公钥解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKeyContent
	 *            公钥内容，Base64编码
	 * @return
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKeyContent) {
		PublicKey publicKey = getPublicKey(publicKeyContent);
		return decryptByPublicKey(encryptedData, publicKey);
	}

	/**
	 * 公钥解密
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥
	 * @return
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, PublicKey publicKey) {
		Cipher cipher = getCipherByKey(Cipher.DECRYPT_MODE, publicKey);
		return decryptByCipher(encryptedData, cipher);
	}

	/**
	 * 公钥加密
	 * 
	 * @param encryptedData
	 *            源数据
	 * @param publicKeyContent
	 *            公钥内容，Base64编码
	 * @return
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKeyContent) {
		PublicKey publicKey = getPublicKey(publicKeyContent);
		return encryptByPublicKey(data, publicKey);
	}

	/**
	 * 公钥加密
	 * 
	 * @param encryptedData
	 *            源数据
	 * @param publicKeyContent
	 *            公钥
	 * @return
	 */
	public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) {
		Cipher cipher = getCipherByKey(Cipher.ENCRYPT_MODE, publicKey);
		return encryptByCipher(data, cipher);
	}

	/**
	 * 用Cipher加密
	 * 
	 * @param data
	 *            源数据
	 * @param cipher
	 *            Cipher实例
	 * @return
	 */
	private static byte[] encryptByCipher(byte[] data, Cipher cipher) {
		byte[] encryptedData = null;
		try {
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			encryptedData = out.toByteArray();
			out.close();
		} catch (Exception e) {
			log.error("RSA encryptByCipher Error :", e);
		}
		return encryptedData;
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 *            源数据
	 * @param privateKeyContent
	 *            私钥内容，Base64编码
	 * @return
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKeyContent) {
		PrivateKey privateKey = getPrivateKey(privateKeyContent);
		return encryptByPrivateKey(data, privateKey);
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 *            源数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) {
		Cipher cipher = getCipherByKey(Cipher.ENCRYPT_MODE, privateKey);
		return encryptByCipher(data, cipher);
	}

	/**
	 * 获取Cipher，通过Key
	 * 
	 * @param mode
	 *            模式
	 * @param key
	 *            Key实例
	 * @return
	 */
	private static Cipher getCipherByKey(int mode, Key key) {
		Cipher cipher = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(mode, key);
		} catch (Exception e) {
			log.error("RSA getCipherByKey Error :", e);
		}
		return cipher;
	}

	/**
	 * 获取私钥内容，从KeyMap中
	 * 
	 * @param keyMap
	 *            KeyMap
	 * @return
	 */
	public static String getPrivateKeyContent(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return BASE64.encodeBase64(key.getEncoded());
	}

	/**
	 * 获取私钥，从KeyMap中
	 * 
	 * @param keyMap
	 * @return
	 */
	public static PrivateKey getPrivateKey(Map<String, Object> keyMap) {
		PrivateKey key = (PrivateKey) keyMap.get(PRIVATE_KEY);
		return key;
	}

	/**
	 * 私钥内容转换成私钥
	 * 
	 * @param privateKeyContent
	 *            私钥内容
	 * @return
	 */
	public static PrivateKey getPrivateKey(String privateKeyContent) {
		PrivateKey privateKey = null;
		try {
			byte[] keyBytes = BASE64.decodeBase64(privateKeyContent);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (Exception e) {
			log.error("RSA getPrivateKey Error :", e);
		}
		return privateKey;
	}

	/**
	 * 从文件中获取私钥
	 * 
	 * @param filePath
	 *            私钥文件路径
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyByFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			return getPrivateKey(FileKit.readFile(file));
		} else {
			return getPrivateKey(FileKit.readFile(RSA.class.getResourceAsStream(filePath)));
		}
	}

	/**
	 * 获取公钥内容，从KeyMap中
	 * 
	 * @param keyMap
	 *            KeyMap
	 * @return
	 */
	public static String getPublicKeyContent(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return BASE64.encodeBase64(key.getEncoded());
	}

	/**
	 * 获取公钥内容，从KeyMap中
	 * 
	 * @param keyMap
	 * @return
	 */
	public static PublicKey getPublicKey(Map<String, Object> keyMap) {
		PublicKey key = (PublicKey) keyMap.get(PUBLIC_KEY);
		return key;
	}

	/**
	 * 公钥内容转换成公钥
	 * 
	 * @param publicKeyContent
	 *            公钥内容
	 * @return
	 */
	public static PublicKey getPublicKey(String publicKeyContent) {
		PublicKey publicKey = null;
		try {
			byte[] keyBytes = BASE64.decodeBase64(publicKeyContent);
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		} catch (Exception e) {
			log.error("RSA getPublicKey Error :", e);
		}
		return publicKey;
	}

	/**
	 * 从文件中获取公钥
	 * 
	 * @param filePath
	 *            公钥文件路径
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyByFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			return getPublicKey(FileKit.readFile(file));
		} else {
			return getPublicKey(FileKit.readFile(RSA.class.getResourceAsStream(filePath)));
		}
	}
}
