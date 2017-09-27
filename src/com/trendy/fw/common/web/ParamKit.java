package com.trendy.fw.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.util.ListKit;
import com.trendy.fw.common.util.StringKit;

public class ParamKit {
	public static String[] getArrayParameter(HttpServletRequest request, String paramName) {
		return request.getParameterValues(paramName);
	}

	public static String getAttribute(HttpServletRequest request, String name) {
		return getAttribute(request, name, false);
	}

	public static String getAttribute(HttpServletRequest request, String name, boolean emptyStringsOK) {
		String temp = (String) request.getAttribute(name);
		if (temp != null) {
			if ((temp.equals("")) && (!(emptyStringsOK))) {
				return null;
			}
			return temp;
		}

		return null;
	}

	public static boolean getBooleanAttribute(HttpServletRequest request, String name) {
		String temp = (String) request.getAttribute(name);
		return ((temp != null) && (temp.equals("true")));
	}

	public static boolean getBooleanParameter(HttpServletRequest request, String name) {
		return getBooleanParameter(request, name, false);
	}

	public static boolean getBooleanParameter(HttpServletRequest request, String name, boolean defaultVal) {
		String temp = request.getParameter(name);
		if ((temp != null) && (!(temp.equals("")))) {
			temp = temp.toLowerCase();
			if (("true".equals(temp)) || ("on".equals(temp)) || (String.valueOf(Constants.STATUS_VALID).equals(temp))) {
				return true;
			} else if (("false".equals(temp)) || ("off".equals(temp))
					|| (String.valueOf(Constants.STATUS_NOT_VALID).equals(temp))) {
				return false;
			}
		}
		return defaultVal;
	}

	public static double getDoubleParameter(HttpServletRequest request, String name, double defaultNum) {
		String temp = request.getParameter(name);
		if ((temp != null) && (!(temp.equals("")))) {
			double num = defaultNum;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return num;
		}
		return defaultNum;
	}

	public static int getIntAttribute(HttpServletRequest request, String name, int defaultNum) {
		String temp = (String) request.getAttribute(name);
		if ((temp != null) && (!(temp.equals("")))) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return num;
		}
		return defaultNum;
	}

	public static int getIntParameter(HttpServletRequest request, String name, int defaultNum) {
		String temp = request.getParameter(name);
		if ((temp != null) && (!(temp.equals("")))) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return num;
		}
		return defaultNum;
	}

	public static int[] getIntParameters(HttpServletRequest request, String name, int defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new int[0];
		}
		int[] values = new int[paramValues.length];
		for (int i = 0; i < paramValues.length; ++i) {
			try {
				values[i] = Integer.parseInt(paramValues[i]);
			} catch (Exception e) {
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static long getLongAttribute(HttpServletRequest request, String name, long defaultNum) {
		String temp = (String) request.getAttribute(name);
		if ((temp != null) && (!(temp.equals("")))) {
			long num = defaultNum;
			try {
				num = Long.parseLong(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return num;
		}
		return defaultNum;
	}

	public static long getLongParameter(HttpServletRequest request, String name, long defaultNum) {
		String temp = request.getParameter(name);
		if ((temp != null) && (!(temp.equals("")))) {
			long num = defaultNum;
			try {
				num = Long.parseLong(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return num;
		}
		return defaultNum;
	}

	public static long[] getLongParameters(HttpServletRequest request, String name, long defaultNum) {
		String[] paramValues = request.getParameterValues(name);
		if (paramValues == null) {
			return null;
		}
		if (paramValues.length < 1) {
			return new long[0];
		}
		long[] values = new long[paramValues.length];
		for (int i = 0; i < paramValues.length; ++i) {
			try {
				values[i] = Long.parseLong(paramValues[i]);
			} catch (Exception e) {
				values[i] = defaultNum;
			}
		}
		return values;
	}

	public static String getParameter(HttpServletRequest request, String name) {
		return getParameter(request, name, false);
	}

	public static String getParameter(HttpServletRequest request, String name, boolean emptyStringsOK) {
		String temp = request.getParameter(name);
		if (temp != null) {
			if ((temp.equals("")) && (!(emptyStringsOK))) {
				return null;
			}
			return temp;
		}

		return null;
	}

	public static String getParameter(HttpServletRequest request, String paramName, String defaultValue) {
		String temp = request.getParameter(paramName);
		if (temp != null) {
			return temp;
		}
		return defaultValue;
	}

	public static HashMap<String, String> getParameterMap(HttpServletRequest request, String paramNames) {
		return getParameterMap(request, paramNames, false, null);
	}

	public static HashMap<String, String> getParameterMap(HttpServletRequest request, String paramNames,
			String defaultValue) {
		return getParameterMap(request, paramNames, true, defaultValue);
	}

	public static HashMap<String, String> getParameterMap(HttpServletRequest request, String paramNames,
			boolean allowNull, String defaultValue) {
		HashMap<String, String> map = new HashMap<String, String>();
		List<String> list = ListKit.string2List(paramNames, ",");
		for (String paramName : list) {
			String value = request.getParameter(paramName);
			if (value != null) {
				map.put(paramName, value);
			} else {
				if (allowNull) {
					map.put(paramName, defaultValue);
				}
			}
		}
		return map;
	}

	public static boolean isValidRequest(HttpServletRequest request, String regex) {
		return isValidUrl(request, regex) && isValidParam(request, regex);
	}

	public static boolean isValidUrl(HttpServletRequest request, String regex) {
		StringBuffer url = request.getRequestURL();
		return StringKit.isMatch(url.toString(), regex);
	}

	public static boolean isValidParam(HttpServletRequest request, String regex) {
		Enumeration<String> names = request.getParameterNames();
		List<String> list = new ArrayList<String>();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = getParameter(request, name);
			list.add(value);
		}
		return StringKit.isMatch(list, regex);
	}

	public static String getInputStream(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}