package com.trendy.fw.common.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapKit {

	/**
	 * 从Map中获取值
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, K, V> String getValueFromMap(T key, Map<K, V> map) {
		String result = "";
		try {
			if (key == null || map == null || map.size() == 0) {
				return result;
			}
			K mapKey = map.keySet().iterator().next();
			K keyValue = null;
			if (key.getClass() == mapKey.getClass()) {
				keyValue = (K) key;
			} else {
				try {
					Method m = mapKey.getClass().getMethod("valueOf", String.class);
					keyValue = (K) m.invoke(null, String.valueOf(key));
				} catch (Exception e) {
					Method m = mapKey.getClass().getMethod("valueOf", Object.class);
					keyValue = (K) m.invoke(null, key);
				}
			}

			if (map.containsKey(keyValue)) {
				result = String.valueOf(map.get(keyValue));
			}
		} catch (Exception e) {
			result = "";
		}
		return result;
	}

	/**
	 * 从Map中获取int类型值
	 * 
	 * @param key
	 * @param map
	 * @return
	 */
	public static <T, K, V> int getIntValueFromMap(T key, Map<K, V> map) {
		int result = 0;
		try {
			String str = getValueFromMap(key, map);
			result = Integer.parseInt(str);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}

	/**
	 * 将HashMap<String, String>转换成List<String[]>
	 * 
	 * @param map
	 * @return
	 */
	public static <K, V> List<String[]> map2List(Map<K, V> map) {
		List<String[]> list = new ArrayList<String[]>();
		Set<Entry<K, V>> entrys = map.entrySet();
		for (Entry<K, V> entry : entrys) {
			String[] array = new String[] { entry.getKey().toString(), entry.getValue().toString() };
			list.add(array);
		}
		return list;
	}

	/**
	 * 将map转换成键值对字符串
	 * 
	 * @param map
	 * @param regex1
	 *            标识1，行之间分隔
	 * @param regex2
	 *            标识2，行内分隔
	 * @return
	 */
	public static <K, V> String map2String(Map<K, V> map, String regex1, String regex2) {
		StringBuilder sb = new StringBuilder();
		for (Entry<K, V> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append(regex1);
			}
			sb.append(entry.getKey().toString() + regex2 + entry.getValue().toString());
		}
		return sb.toString();
	}

	/**
	 * 将键值对字符串转换成map
	 * 
	 * @param str
	 *            字符串
	 * @param regex1
	 *            标识1，行之间分隔
	 * @param regex2
	 *            标识2，行内分隔
	 * @return
	 */
	public static Map<String, String> string2Map(String str, String regex1, String regex2) {
		Map<String, String> map = new HashMap<String, String>();
		String[] array1 = str.split(regex1);
		for (String line : array1) {
			String[] array2 = line.split(regex2, 2);
			map.put(array2[0], array2[1]);
		}
		return map;
	}
}
