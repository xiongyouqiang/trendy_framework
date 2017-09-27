package com.trendy.fw.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.trendy.fw.common.config.Constants;

public class XmlKit {
	private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><dataset>";
	private static final String xmlFooter = "</dataset>";

	/**
	 * 将list转换成xml
	 * 
	 * @param list
	 * @return
	 */
	public static <E> String toXml(List<E> list) {
		if (list == null || list.isEmpty()) {
			// 结果集为空，返回空xml封装
			return getEmptyXml();
		}
		List<Field> fieldList = BeanKit.getDeclaredFieldList(list.get(0));
		StringBuilder sb = new StringBuilder();
		sb.append(xmlHeader);
		for (Object bean : list) {
			sb.append("<row>");
			sb.append(getRowString(bean, fieldList));
			sb.append("</row>");
		}
		sb.append(xmlFooter);
		return sb.toString();
	}

	/**
	 * 将bean转换成xml
	 * 
	 * @param bean
	 * @return
	 */
	public static <E> String toXml(E bean) {
		if (bean == null) {// 结果集为空，返回空xml封装
			return getEmptyXml();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(xmlHeader);
		sb.append(getRowString(bean));
		sb.append(xmlFooter);
		return sb.toString();
	}

	private static <E> String getRowString(E bean) {
		return getRowString(bean, BeanKit.getDeclaredFieldList(bean));
	}

	private static <E> String getRowString(E bean, List<Field> fieldList) {
		StringBuffer sb = new StringBuffer();
		try {
			for (Field field : fieldList) {
				String fieldName = field.getName();
				Object fieldValue = ReflectKit.getPropertyValue(bean, fieldName);
				sb.append("<" + fieldName + "><![CDATA[" + fieldValue + "]]></" + fieldName + ">");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 将list转换成xml
	 * 
	 * @param list
	 * @return
	 */
	public static <K, V> String map2Xml(List<Map<K, V>> list) {
		if (list == null || list.isEmpty()) {// 结果集为空，返回空xml封装
			return getEmptyXml();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(xmlHeader);
		Iterator<Map<K, V>> it = list.iterator();
		while (it.hasNext()) {
			sb.append("<row>");
			Map<K, V> map = (Map<K, V>) it.next();
			sb.append(getRowString(map));
			sb.append("</row>");
		}
		sb.append(xmlFooter);
		return sb.toString();
	}

	/**
	 * 将map转换成xml
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V> String map2Xml(Map<K, V> map) {
		if (map == null || map.isEmpty()) {// 结果集为空，返回空xml封装
			return getEmptyXml();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(xmlHeader);
		sb.append(getRowString(map));
		sb.append(xmlFooter);
		return sb.toString();
	}

	private static <K, V> String getRowString(Map<K, V> map) {
		StringBuffer sb = new StringBuffer();
		for (Entry<K, V> entry : map.entrySet()) {
			sb.append("<" + entry.getKey() + "><![CDATA[" + entry.getValue() + "]]></" + entry.getKey() + ">");
		}
		return sb.toString();
	}

	/**
	 * 返回空的xml
	 * 
	 * @return
	 */
	public static String getEmptyXml() {
		return xmlHeader + xmlFooter;
	}

	/**
	 * 根据String获取xml的Document对象
	 * 
	 * @param xmlStr
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public static Document getDocumentByString(String xmlStr) throws UnsupportedEncodingException, DocumentException {
		return getDocumentByString(xmlStr, Constants.CODE_UNICODE);
	}

	/**
	 * 根据String获取xml的Document对象
	 * 
	 * @param xmlStr
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws DocumentException
	 */
	public static Document getDocumentByString(String xmlStr, String charset) throws UnsupportedEncodingException,
			DocumentException {
		SAXReader reader = new SAXReader();
		InputStream is = new ByteArrayInputStream(xmlStr.getBytes(charset));
		Document document = reader.read(is);
		return document;
	}

	/**
	 * 根据文件路径获取xml的Document对象
	 * 
	 * @param filePath
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocumentByFile(String filePath) throws DocumentException {
		return getDocumentByFile(filePath, Constants.CODE_UNICODE);
	}

	/**
	 * 根据文件路径获取xml的Document对象
	 * 
	 * @param filePath
	 * @param charSet
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocumentByFile(String filePath, String charset) throws DocumentException {
		SAXReader reader = new SAXReader();
		reader.setEncoding(charset);
		Document document = reader.read(new File(filePath));
		return document;
	}

	/**
	 * 根据Url获取xml的Document对象
	 * 
	 * @param url
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocumentByUrl(String url) throws DocumentException {
		return getDocumentByUrl(url, Constants.CODE_UNICODE);
	}

	/**
	 * 根据Url获取xml的Document对象
	 * 
	 * @param url
	 * @param charSet
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocumentByUrl(String url, String charset) throws DocumentException {
		SAXReader reader = new SAXReader();
		reader.setEncoding(charset);
		Document document = reader.read(url);
		return document;
	}
}
