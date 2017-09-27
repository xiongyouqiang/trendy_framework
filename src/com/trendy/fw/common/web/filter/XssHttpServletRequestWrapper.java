package com.trendy.fw.common.web.filter;

import java.io.StringReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.blogspot.radialmind.html.HTMLParser;
import com.blogspot.radialmind.xss.XSSFilter;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	HttpServletRequest httpServletRequest = null;

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		httpServletRequest = request;
	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(xssEncode(name));
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 * getHeaderNames 也可能需要覆盖
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(xssEncode(name));
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	
/*	private static String xssEncode(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}

		StringReader reader = new StringReader(s);
		StringWriter writer = new StringWriter();
		try {
			HTMLParser.process(reader, writer, new XSSFilter(), true);

			return writer.toString();
		} catch (NullPointerException e) {
			return s;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;

	}*/
	
	
	/**
	 * 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 * @param s
	 * @return
	 */
	private static String xssEncode(String s) {
		if (s == null || "".equals(s)) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\'':
				sb.append("&prime;");
				break;
			case '′':
				sb.append("&prime;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '＂':
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '#':
				sb.append("＃");
				break;
			case '\\':
				sb.append("﹨");
				break;
			case '=':
				sb.append("&#61;");
				break;
			default:
				sb.append(c);
				break;
			}
		}

		return sb.toString();
	}

	/**
	 * 获取HttpServletRequest
	 * 
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	/**
	 * 获取HttpServletRequest
	 * 
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest(HttpServletRequest request) {
		if (request instanceof XssHttpServletRequestWrapper) {
			return ((XssHttpServletRequestWrapper) request).getHttpServletRequest();
		}

		return request;
	}
}
