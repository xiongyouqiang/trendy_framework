package com.trendy.fw.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesKit {
	private static Logger log = LoggerFactory.getLogger(PropertiesKit.class);

	/**
	 * 获取配置文件中的内容
	 * 
	 * @param fileName
	 *            文件名（含全路径，绝对路径）
	 * @param key
	 *            内容名称
	 * @return 内容
	 */
	public static String getProperties(String fileName, String key) {
		String result = null;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));
			Properties properties = new Properties();
			properties.load(in);
			result = properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("读取配置内容[{}]出错：", fileName + "-" + key, e);
		}
		return result;
	}

	/**
	 * 获取配置文件中所有配置项
	 * 
	 * @param fileName
	 *            文件名（含全路径，绝对路径）
	 * @return
	 */
	public static Map<String, String> getAllProperties(String fileName) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));
			Properties properties = new Properties();
			properties.load(in);
			Set<Object> keySet = (Set<Object>) properties.keySet();
			Iterator<Object> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				map.put(key, properties.getProperty(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("读取配置文件[{}]出错：", fileName, e);
		}
		return map;
	}

	/**
	 * 获取配置文件中的内容（通过PropertyResourceBundle，更改时需重启服务）
	 * 
	 * @param fileName
	 *            文件名（含全路径，相对路径，从classes开始，文件名不需要后缀）
	 * @param key
	 *            内容名称
	 * @return 内容
	 */
	public static String getBundleProperties(String fileName, String key) {
		String result = null;
		try {
			PropertyResourceBundle configBundle = (PropertyResourceBundle) ResourceBundle.getBundle(fileName);
			result = configBundle.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("读取配置内容[{}]出错：", fileName + "-" + key, e);
		}
		return result;
	}

	/**
	 * 获取配置文件中所有配置项（通过PropertyResourceBundle，更改时需重启服务）
	 * 
	 * @param fileName
	 *            文件名（含全路径，相对路径，从classes开始，文件名不需要后缀）
	 * @return
	 */
	public static Map<String, String> getBundleAllProperties(String fileName) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			PropertyResourceBundle configBundle = (PropertyResourceBundle) ResourceBundle.getBundle(fileName);
			Enumeration<String> emun = configBundle.getKeys();
			while (emun.hasMoreElements()) {
				String key = emun.nextElement();
				map.put(key, configBundle.getString(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("读取配置文件[{}]出错：", fileName, e);
		}
		return map;
	}
}
