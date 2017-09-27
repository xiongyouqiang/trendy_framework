package com.trendy.fw.common.util;

import com.trendy.fw.common.bean.StatusBean;
import java.util.HashMap;
import java.util.List;

public class StatusKit {
	/**
	 * 将状态列表转换成map
	 * 
	 * @param list
	 * @return
	 */
	public static HashMap<String, String> toMap(List<StatusBean> list) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (list.size() > 0) {
			for (StatusBean bean : list) {
				map.put(bean.getStatus(), bean.getValue());
			}
		}
		return map;
	}

	/**
	 * 获取状态内容
	 * 
	 * @param status
	 * @param map
	 * @return
	 */
	public static String getValueFromMap(int status, HashMap<String, String> map) {
		return MapKit.getValueFromMap(status, map);
	}

	/**
	 * 获取状态内容
	 * 
	 * @param status
	 * @param map
	 * @return
	 */
	public static String getValueFromMap(String status, HashMap<String, String> map) {
		return MapKit.getValueFromMap(status, map);
	}
}