package com.trendy.fw.common.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.trendy.fw.common.util.StringKit;

public class BrowserKit {
	public final static String MSIE = "MSIE";
	public final static String FIREFOX = "Firefox";
	public final static String OPERA = "Opera";
	public final static String CHROME = "Chrome";
	public final static String SAFARI = "Safari";
	public final static String OTHER = "Other";

	private final static String mobileRegex = "\\b(ip(hone|od)|android|opera m(ob|in)i"
			+ "|windows (phone|ce)|blackberry" + "|s(ymbian|eries60)|samsung|playbook|palm|profile/midp"
			+ "|playstation portable|nokia|fennec|htc[-_]" + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	private static Pattern mobilePattern = Pattern.compile(mobileRegex, Pattern.CASE_INSENSITIVE);

	private final static String tabletRegex = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	private static Pattern tabletPattern = Pattern.compile(tabletRegex, Pattern.CASE_INSENSITIVE);

	public static String[] parseBrowserInfo(HttpServletRequest request) {
		String[] result = new String[] { "", "" };

		String userAgent = request.getHeader("User-Agent");
		if (StringKit.isValid(userAgent)) {
			if (userAgent.indexOf(MSIE) > 0) {
				result[0] = MSIE;
				result[1] = parseMsieVersion(userAgent);
			} else if (userAgent.indexOf(FIREFOX) > 0) {
				result[0] = FIREFOX;
				result[1] = parseFirefoxVersion(userAgent);
			} else if (userAgent.indexOf(OPERA) > 0) {
				result[0] = OPERA;
				result[1] = parseOperaVersion(userAgent);
			} else if (userAgent.indexOf(CHROME) > 0) {
				result[0] = CHROME;
				result[1] = parseChromeVersion(userAgent);
			} else if (userAgent.indexOf(SAFARI) > 0) {
				result[0] = SAFARI;
				result[1] = parseSafariVersion(userAgent);
			} else {
				result[0] = OTHER;
			}
		} else {
			result[0] = OTHER;
		}

		return result;
	}

	private static String parseMsieVersion(String userAgent) {
		String result = "";
		String regex = "MSIE ((.*?));";
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(userAgent);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String parseFirefoxVersion(String userAgent) {
		String result = "";
		String regex = "Firefox/((.*))";
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(userAgent);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String parseChromeVersion(String userAgent) {
		String result = "";
		String regex = "Chrome/((.*?)) ";
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(userAgent);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
		}
		return result;
	}

	private static String parseSafariVersion(String userAgent) {
		String result = "";
		String regex = "Version/((.*)) ";
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(userAgent);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String parseOperaVersion(String userAgent) {
		String result = "";
		String regex = "Version/((.*)) ";
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(userAgent);
			if (m.find()) {
				result = m.group(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean isMobileBrowser(HttpServletRequest request) {
		boolean result = false;

		String userAgent = request.getHeader("User-Agent").toLowerCase();
		if (StringKit.isValid(userAgent)) {
			try {
				Matcher matcher = mobilePattern.matcher(userAgent);// 匹配
				result = matcher.find();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static boolean isTabletBrowser(HttpServletRequest request) {
		boolean result = false;

		String userAgent = request.getHeader("User-Agent").toLowerCase();
		if (StringKit.isValid(userAgent)) {
			try {
				Matcher matcher = tabletPattern.matcher(userAgent);// 匹配
				result = matcher.find();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
