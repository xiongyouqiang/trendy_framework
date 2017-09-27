package com.trendy.fw.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonKit {
	private static JsonFactory jsonFactory = new JsonFactory();

	private static ObjectMapper mapper = null;

	static {
		jsonFactory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		jsonFactory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper = new ObjectMapper(jsonFactory);
	}

	/**
	 * 获取jackson json lib的ObjectMapper对象
	 * 
	 * @return ObjectMapper对象
	 */
	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	/**
	 * 获取jackson json lib的JsonFactory对象
	 * 
	 * @return JsonFactory对象
	 */
	public static JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	/**
	 * 将json转成bean
	 * 
	 * @param <T>
	 *            多态类型
	 * @param json
	 *            json字符串
	 * @param clazz
	 *            java bean类型(Class)
	 * @return java bean对象
	 */
	public static <T> T toBean(String json, Class<T> clazz) {
		T bean = null;
		try {
			bean = mapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return bean;
	}

	/**
	 * 将json转换成List
	 * 
	 * @param json
	 *            json字符串
	 * @param clazz
	 *            java bean类型(Class)
	 * @return
	 */
	public static <T> List<T> toList(String json, Class<T> clazz) {
		List<T> list = new ArrayList<T>();

		List<HashMap<String, Object>> jsonList = toBean(json, ArrayList.class);
		for (HashMap<String, Object> map : jsonList) {
			try {
				list.add(mapper.convertValue(map, clazz));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			}
		}
		return list;
	}

	/**
	 * 将bean转成json
	 * 
	 * @param bean
	 *            java bean
	 * @return json 字符串
	 */
	public static String toJson(Object bean) {

		String rtv = null;
		try {
			rtv = mapper.writeValueAsString(bean);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return rtv;
	}

	/**
	 * 将List转换成json
	 * 
	 * @param list
	 * @return
	 */
	public static String toJson(List<Object> list) {
		String rtv = null;
		StringBuffer sb = new StringBuffer();
		try {
			for (Object bean : list) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(mapper.writeValueAsString(bean));
			}
			rtv = "[" + sb.toString() + "]";
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return rtv;
	}
}
