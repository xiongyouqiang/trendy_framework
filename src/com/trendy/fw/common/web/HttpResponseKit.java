package com.trendy.fw.common.web;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trendy.fw.common.util.BeanKit;
import com.trendy.fw.common.util.DateKit;
import com.trendy.fw.common.util.EncodeKit;
import com.trendy.fw.common.util.JsonKit;
import com.trendy.fw.common.util.ReflectKit;
import com.trendy.fw.common.util.StringKit;

public class HttpResponseKit {
	// 响应操作
	public static final int ACTION_NONE = 0;// 无任何操作
	public static final int ACTION_HISTORY_BACK = 1;// 上一页
	public static final int ACTION_WINDOW_CLOSE = 2;// 关闭窗口

	// 返回类型
	public static final String CT_HTML = "text/html";
	public static final String CT_XML = "text/xml";
	public static final String CT_JSON = "application/json";

	public static final String DEFAULT_ENCODE = "UTF-8";

	/**
	 * 在页面上弹出js警告
	 * 
	 * @param response
	 * @param msg
	 * @param action
	 *            ACTION_NONE：无任何操作；ACTION_HISTORY_BACK：上一页；ACTION_WINDOW_CLOSE：
	 *            关闭窗口
	 */
	public static void alertMessage(HttpServletResponse response, String msg, int action) {
		try {
			setResponseHeader(response);
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert(\"" + msg + "\");");
			if (action == ACTION_HISTORY_BACK) {
				out.println("history.back();");
			} else if (action == ACTION_WINDOW_CLOSE) {
				out.println("window.close();");
			}
			out.println("</script>");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在页面上弹出js警告，并跳转
	 * 
	 * @param response
	 * @param msg
	 * @param redirectUrl
	 */
	public static void alertMessage(HttpServletResponse response, String msg, String redirectUrl) {
		try {
			setResponseHeader(response);
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert(\"" + msg + "\");");
			if (StringKit.isValid(redirectUrl)) {
				out.println("location.href=\"" + redirectUrl + "\";");
			}
			out.println("</script>");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印信息到页面
	 * 
	 * @param response
	 * @param msg
	 */
	public static void printMessage(HttpServletResponse response, String msg) {
		try {
			printMessage(response, msg, CT_HTML);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印信息到页面
	 * 
	 * @param response
	 * @param msg
	 * @param contentType
	 *            内容类型，CT_HTML：html；CT_XML：xml
	 */
	public static void printMessage(HttpServletResponse response, String msg, String contentType) {
		try {
			setResponseHeader(response, contentType);
			PrintWriter out = response.getWriter();
			out.println(msg);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印json内容
	 * 
	 * @param request
	 * @param response
	 * @param obj
	 * @param callbackParam
	 *            jsonp回调参数
	 */
	public static <E> void printJson(HttpServletRequest request, HttpServletResponse response, E obj,
			String callbackParam) {
		String msg = "";
		String callback = ParamKit.getParameter(request, callbackParam);
		if (StringKit.isValid(callback)) {
			msg = MessageFormat.format("{0}({1});", callback, JsonKit.toJson(obj));
		} else {
			msg = JsonKit.toJson(obj);
		}
		printMessage(response, msg, CT_JSON);
	}

	/**
	 * 通过map打印form到页面
	 * 
	 * @param response
	 * @param url
	 * @param map
	 */
	public static <E> void printForm(HttpServletResponse response, String url, Map<String, E> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, E> entry : map.entrySet()) {
			sb.append(getInputString(entry.getKey(), entry.getValue()));
		}
		printForm(response, url, sb.toString());
	}

	/**
	 * 通过bean打印form到页面
	 * 
	 * @param response
	 * @param url
	 * @param bean
	 */
	public static <E> void printForm(HttpServletResponse response, String url, E bean) {
		StringBuilder sb = new StringBuilder();
		List<Field> list = BeanKit.getDeclaredFieldList(bean.getClass());
		for (Field field : list) {
			sb.append(getInputString(field.getName(), ReflectKit.getPropertyValue(bean, field.getName())));
		}
		printForm(response, url, sb.toString());
	}

	/**
	 * 通过数组list打印form到页面
	 * 
	 * @param response
	 * @param url
	 * @param list
	 */
	public static <E> void printForm(HttpServletResponse response, String url, List<E[]> list) {
		StringBuilder sb = new StringBuilder();
		for (E[] array : list) {
			sb.append(getInputString(array[0].toString(), array[1].toString()));
		}
		printForm(response, url, sb.toString());
	}

	/**
	 * 组合form内input的行代码
	 * 
	 * @param inputName
	 * @param inputValue
	 * @return
	 */
	private static <E> String getInputString(String inputName, E inputValue) {
		return MessageFormat.format("<input name=\"{0}\" type=\"hidden\" value=\"{1}\" />", inputName, inputValue);
	}

	/**
	 * 打印form内容
	 * 
	 * @param response
	 * @param url
	 * @param content
	 */
	private static void printForm(HttpServletResponse response, String url, String content) {
		StringBuilder sb = new StringBuilder();
		sb.append(MessageFormat.format("<form id=\"formData\" name=\"formData\" method=\"post\" action=\"{0}\">", url));
		sb.append(content);
		sb.append("</form>");
		sb.append("<script>document.getElementById(\"formData\").submit();</script>");
		printMessage(response, sb.toString(), CT_HTML);
	}

	/**
	 * 设置html内容类型
	 * 
	 * @param response
	 */
	public static void setResponseHeader(HttpServletResponse response) {
		response.setCharacterEncoding(DEFAULT_ENCODE);
		response.setContentType(CT_HTML);
	}

	/**
	 * 设置内容类型
	 * 
	 * @param response
	 * @param contentType
	 */
	public static void setResponseHeader(HttpServletResponse response, String contentType) {
		response.setCharacterEncoding(DEFAULT_ENCODE);
		response.setContentType(contentType);
	}

	/**
	 * 设置response响应的过期时间，单位：秒
	 * 
	 * @param response
	 * @param period
	 */
	public static void setCache(HttpServletResponse response, int period) {
		Calendar cal = Calendar.getInstance();
		String modifyTime = DateKit.formatGMTDate(cal.getTime());
		String expireTime = DateKit.formatGMTDate(cal.getTimeInMillis() + period * 1000);

		response.setHeader("Last-Modified", modifyTime);
		response.setHeader("Expires", expireTime);
		response.setHeader("Cache-Control", "public, max-age=" + period);
	}

	/**
	 * 设置response响应为没有缓存
	 * 
	 * @param response
	 */
	public static void setNoCache(HttpServletResponse response) {
		response.setHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "must-revalidate");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
	}

	public static void setAttachmentFile(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws Exception {
		String[] browserInfo = BrowserKit.parseBrowserInfo(request);
		if (browserInfo[0].equals(BrowserKit.FIREFOX)) {
			response.setHeader("Content-disposition", "attachment;filename=\"" + EncodeKit.toSystemCode(fileName)
					+ "\"");
		} else {
			response.setHeader("Content-disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8")
					+ "\"");
		}
	}
}
