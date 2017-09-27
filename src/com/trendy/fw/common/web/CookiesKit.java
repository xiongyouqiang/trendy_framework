package com.trendy.fw.common.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trendy.fw.common.util.StringKit;

public class CookiesKit {
	/**
	 * 设置cookie
	 * 
	 * @param response
	 *            HttpResponse对象
	 * @param name
	 *            cookie名
	 * @param value
	 *            cookie值
	 * @param domain
	 *            域名
	 * @param path
	 *            路径
	 * @param expireSecond
	 *            失效时间，-1表示关闭浏览器窗口后失效
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String domain, String path,
			int expireTime) {
		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		cookie.setPath(path);
		cookie.setMaxAge(expireTime);
		if (StringKit.isValid(domain))
			cookie.setDomain(domain);
		response.addCookie(cookie);
	}

	/**
	 * 设置cookie，关闭浏览器窗口后失效
	 * 
	 * @param response
	 *            HttpResponse对象
	 * @param name
	 *            cookie名
	 * @param value
	 *            cookie值
	 * @param domain
	 *            域名
	 * @param path
	 *            路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String domain, String path) {
		setCookie(response, name, value, domain, path, -1);
	}

	/**
	 * 设置根路径（/）上的cookie，关闭浏览器窗口后失效
	 * 
	 * @param response
	 *            HttpResponse对象
	 * @param name
	 *            cookie名
	 * @param value
	 *            cookie值
	 * @param domain
	 *            域名
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String domain) {
		setCookie(response, name, value, domain, "/", -1);
	}

	/**
	 * 设置当前域名的根路径（/）上的cookie，关闭浏览器窗口后失效
	 * 
	 * @param response
	 *            HttpResponse对象
	 * @param name
	 *            cookie名
	 * @param value
	 *            cookie值
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, null, "/", -1);
	}

	/**
	 * 删除指定的cookie
	 * 
	 * @param response
	 *            HttpResponse对象
	 * @param cookie
	 */
	public static void deleteCookie(HttpServletResponse response, Cookie cookie) {
		if (cookie != null) {
			cookie.setMaxAge(0); // Delete the cookie by setting its maximum age
			// to zero
			response.addCookie(cookie);
		}
	}

	/**
	 * 删除指定的cookie
	 * 
	 * @param response
	 *            HttpResponse对象
	 * @param name
	 *            cookie名
	 */
	public static void deleteCookie(HttpServletResponse response, String name) {
		setCookie(response, name, null, null, "/", 0);
	}

	public static void deleteCookie(HttpServletResponse response, String name, String domain) {
		setCookie(response, name, null, domain, "/", 0);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		// cookies不为空，则清除
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
					break;
				}
			}
		}
	}

	/**
	 * 获取cookies的值
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookies(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name))
				return cookies[i].getValue();
		}
		return null;
	}
}
