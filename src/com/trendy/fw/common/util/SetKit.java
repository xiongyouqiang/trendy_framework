package com.trendy.fw.common.util;

import java.util.HashSet;

public class SetKit {
	/**
	 * 将String转换成Set
	 * 
	 * @param str
	 *            字符串
	 * @param regex
	 *            分隔符
	 * @return
	 */
	public static HashSet<String> string2Set(String str, String regex) {
		HashSet<String> set = new HashSet<String>();
		if (StringKit.isValid(str)) {
			String array[] = str.split(regex);
			for (String s : array) {
				set.add(s);
			}
		}
		return set;
	}

	/**
	 * 将Set转换成String
	 * 
	 * @param set
	 *            集合
	 * @param regex
	 *            分隔符
	 * @return
	 */
	public static <E> String set2String(HashSet<E> set, String regex) {
		StringBuffer sb = new StringBuffer();
		for (E element : set) {
			if (sb.length() > 0) {
				sb.append(regex);
			}
			sb.append(element);
		}
		return sb.toString();
	}
}
