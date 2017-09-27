package com.trendy.fw.common.util;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectKit {
	private static Logger log = LoggerFactory.getLogger(ReflectKit.class);

	/**
	 * 获取getter方法
	 * 
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static String getGetterName(String fieldName) {
		return "get" + StringKit.upperFirstChar(fieldName);
	}

	/**
	 * 获取setter方法
	 * 
	 * @param fieldName
	 *            字段名
	 * @return
	 */
	public static String getSetterName(String fieldName) {
		return "set" + StringKit.upperFirstChar(fieldName);
	}

	/**
	 * 检查对象是否基础类型
	 * 
	 * @param obj
	 *            对象
	 * @return
	 */
	public static boolean isPrimitiveObject(Object obj) {
		return obj instanceof Byte || obj instanceof Integer || obj instanceof Short || obj instanceof Long
				|| obj instanceof Float || obj instanceof Double || obj instanceof String || obj instanceof Timestamp
				|| obj instanceof Boolean;
	}

	/**
	 * 获取bean中指定的属性值
	 * 
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public static <E> Object getPropertyValue(E bean, String fieldName) {
		try {
			return bean.getClass().getMethod(getGetterName(fieldName)).invoke(bean);
		} catch (Exception e) {
			log.error("ReflectKit error:", e);
		}
		return null;
	}
}
