package com.trendy.fw.common.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trendy.fw.common.util.ListKit;
import com.trendy.fw.common.util.MapKit;

public class UrlKit {
	/**
	 * 从url分析出行为指向
	 * 
	 * @param request
	 * @return
	 */
	static public String getAction(HttpServletRequest request) {
		String url = request.getRequestURI();
		String action = url.substring(url.lastIndexOf("/") + 1);
		action = action.substring(0, action.lastIndexOf("."));
		return action;
	}

	/**
	 * 将请求内容转换成list
	 * 
	 * @param queryString
	 * @return
	 */
	public static List<String[]> queryString2List(String queryString) {
		return ListKit.string2ListOfArray(queryString, "&", "=");
	}

	/**
	 * 将list转换成请求内容字符串
	 * 
	 * @param list
	 * @return
	 */
	public static String list2QueryString(List<String[]> list) {
		return ListKit.listOfArray2String(list, "&", "=");
	}

	/**
	 * 将请求内容转换成map
	 * 
	 * @param queryString
	 * @return
	 */
	public static Map<String, String> queryString2Map(String queryString) {
		return MapKit.string2Map(queryString, "&", "=");
	}

	/**
	 * 将map转换成请求内容字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String map2QueryString(Map<String, String> map) {
		return MapKit.map2String(map, "&", "=");
	}

	/**
	 * 封装URLEncoder.encode
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String urlEncode(String str, String charset) {
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}

	/**
	 * 封装URLDecoder.decode
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String urlDecode(String str, String charset) {
		try {
			return URLDecoder.decode(str, charset);
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}
}
