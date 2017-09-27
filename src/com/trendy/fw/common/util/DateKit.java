package com.trendy.fw.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateKit {
	// 日期格式
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String DEFAULT_TIME_FORMAT = "HH:mm:ss";
	public final static String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;
	public final static String DEFAULT_ALL_FORMAT = DEFAULT_DATE_TIME_FORMAT + ".SS";
	public final static String DEFAULT_GMT_FORMAT = "EEE, d MMM yyyy hh:mm:ss z";

	// 日期部分格式
	public final static String REGEX_YEAR = "yyyy";
	public final static String REGEX_MONTH = "MM";
	public final static String REGEX_DATE = "dd";
	public final static String REGEX_HOUR = "HH";
	public final static String REGEX_MINUTE = "mm";
	public final static String REGEX_SECOND = "ss";
	public final static String REGEX_MICROSECOND = "ms";

	/**
	 * 获取当前系统时间
	 * 
	 * @return 当前时间 格式“08:13:36”
	 */
	public static String getCurrentTime() {
		return formatDate(new Date(), DEFAULT_TIME_FORMAT);
	}

	/**
	 * 获取当前系统时间戳
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestamp() {
		Calendar cal = Calendar.getInstance();
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 获取当前系统日期和时间
	 * 
	 * @return 当前系统日期和时间时间格式"2004-08-23 08:34:35"
	 */
	public static String getCurrentDateTime() {
		return getCurrentDateTime(DEFAULT_DATE_TIME_FORMAT);
	}

	/**
	 * 获取当前系统日期和时间
	 * 
	 * @param format
	 *            显示格式
	 * @return
	 */
	public static String getCurrentDateTime(String format) {
		return formatDate(new Date(), format);
	}

	/**
	 * 返回当前日期
	 * 
	 * @return String ： 格式：2004-03-23
	 */
	static public String getCurrentDate() {
		return formatDate(new Date(), DEFAULT_DATE_FORMAT);
	}

	/**
	 * 格式化时间的显示
	 * 
	 * @param objCal
	 *            Calendar Object
	 * @param strFormat
	 *            日期格式化的标准 e.g. "yyyy/MM/dd
	 *            HH:mm:ss"（务必按标准写，可参考java.text.simpleDateFormat.java)
	 * @return String 格式化的时间
	 */
	public static String formatCalendar(Calendar cal, String format) {
		if (cal == null) {
			return null;
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(cal.getTime());
		}
	}

	/**
	 * 格式化时间的显示
	 * 
	 * @param objTS
	 *            Timestamp Object
	 * @param strFormat
	 *            日期格式化的标准 e.g. "yyyy/MM/dd
	 *            HH:mm:ss"（务必按标准写，可参考java.text.simpleDateFormat.java)
	 * @return String 格式化的时间
	 */
	public static String formatTimestamp(Timestamp ts, String format) {
		if (ts == null) {
			return null;
		} else {
			Calendar objCal = Calendar.getInstance();
			Date d = new Date(ts.getTime());
			objCal.setTime(d);
			return formatCalendar(objCal, format);
		}
	}

	/**
	 * 格式化date
	 * 
	 * @param dt
	 *            Date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String formatDate(Date dt, String format) {
		if (dt == null) {
			return null;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			return formatCalendar(cal, format);
		}
	}

	/**
	 * 格式化时间
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static String formatDate(long value, String format) {
		Date date = new Date(value);
		return formatDate(date, format);
	}

	/**
	 * 格式化（转换）日期
	 * 
	 * @param str
	 * @param oriFormat
	 * @param newFormat
	 * @return
	 */
	public static String formatStr(String str, String oriFormat, String newFormat) {
		if (str == null) {
			return null;
		} else {
			Date dt = str2Date(str, oriFormat);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			return formatCalendar(cal, newFormat);
		}
	}

	/**
	 * 以GMT格式化（转换）日期
	 * 
	 * @param dt
	 * @return
	 */
	public static String formatGMTDate(Date dt) {
		return formatGMTDate(dt, DEFAULT_GMT_FORMAT);
	}

	/**
	 * 以GMT格式化（转换）日期
	 * 
	 * @param dt
	 * @param format
	 * @return
	 */
	public static String formatGMTDate(Date dt, String format) {
		Locale locale = Locale.US;
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_GMT_FORMAT, locale);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatter.format(dt);
	}

	/**
	 * 以GMT格式化（转换）日期
	 * 
	 * @param value
	 * @return
	 */
	public static String formatGMTDate(long value) {
		return formatGMTDate(value, DEFAULT_GMT_FORMAT);
	}

	/**
	 * 以GMT格式化（转换）日期
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static String formatGMTDate(long value, String format) {
		return formatGMTDate(new Date(value), format);
	}

	/**
	 * 得到某年某个月的的天数，比如2004。2月的天数为29
	 * 
	 * @param fullYear
	 *            year(eg.2002)
	 * @param month
	 *            month (from 1 to 12)
	 * @return int
	 */
	static public int getLastDateOfMonth(int year, int month) {

		int result = 1;

		Calendar cal = Calendar.getInstance();
		java.util.Date d = new java.util.Date();
		long time = 0;

		if (month == 12) {
			year++;
			month = 0;
		}
		cal.set(year, month, 1);
		d = cal.getTime();
		time = d.getTime();
		time -= 24 * 60 * 60 * 1000;
		d = new java.util.Date(time);
		cal.setTime(d);

		result = cal.get(Calendar.DATE);

		return result;
	}

	/**
	 * 日期增减
	 * 
	 * @param part
	 * @param number
	 * @param dt
	 * @return
	 */
	public static Date addTime(String part, int number, Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		if (part.equals(REGEX_YEAR)) {
			cal.add(Calendar.YEAR, number);
		} else if (part.equals(REGEX_MONTH)) {
			cal.add(Calendar.MONTH, number);
		} else if (part.equals(REGEX_DATE)) {
			cal.add(Calendar.DATE, number);
		} else if (part.equals(REGEX_HOUR)) {
			cal.add(Calendar.HOUR_OF_DAY, number);
		}

		return cal.getTime();
	}

	/**
	 * 计算日期间的间隙
	 * 
	 * @param part
	 *            结果显示部分
	 * @param dtA
	 * @param dtB
	 * @return
	 */
	public static int diffTime(String part, Date dtA, Date dtB) {
		int result = 0;

		Calendar calA = Calendar.getInstance();
		calA.setTime(dtA);

		Calendar calB = Calendar.getInstance();
		calB.setTime(dtB);

		long gap = calB.getTimeInMillis() - calA.getTimeInMillis();

		if (part.equals(REGEX_DATE)) {
			result = (int) (gap / (24 * 60 * 60 * 1000));
		} else if (part.equals(REGEX_HOUR)) {
			result = (int) (gap / (60 * 60 * 1000));
		} else if (part.equals(REGEX_MINUTE)) {
			result = (int) (gap / (60 * 1000));
		} else if (part.equals(REGEX_SECOND)) {
			result = (int) (gap / 1000);
		}

		return result;
	}

	/**
	 * 将字符串转换成日期
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date str2Date(String str, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 将字符串转换成时间戳
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static Timestamp str2Timestamp(String str, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return new Timestamp(sdf.parse(str).getTime());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 从标准时间格式的字符串中取出年、月、日、时、分、秒组成的int类型数组
	 * 
	 * @param DateTimeStr
	 *            String "2004-2-2 3-34-35" (用-或：或/作间隔）
	 * @return int[]
	 */
	static public int[] splitTime(String str) {
		if (str == null || str.length() == 0)
			return null;

		str = str.trim().replaceAll("/", "-");
		str = str.replaceAll(" ", "-").replaceAll(":", "-");
		String[] values = str.split("-");
		int[] timeArray = new int[values.length];
		for (int i = 0; i < timeArray.length; i++) {
			try {
				timeArray[i] = Integer.parseInt(values[i]);
			} catch (NumberFormatException nfe) {
				timeArray[i] = 0;
			}
		}
		return timeArray;
	}

	/**
	 * 将日期分割成数字数组
	 * 
	 * @param dt
	 * @return
	 */
	public static int[] splitTime(Date dt) {
		int[] array = { 0, 0, 0, 0, 0, 0 };

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		array[0] = cal.get(Calendar.YEAR);
		array[1] = cal.get(Calendar.MONTH) + 1;
		array[2] = cal.get(Calendar.DATE);
		array[3] = cal.get(Calendar.HOUR_OF_DAY);
		array[4] = cal.get(Calendar.MINUTE);
		array[5] = cal.get(Calendar.SECOND);

		return array;
	}

	/**
	 * 将日期转换成Date
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date convent2Date(String year, String month, String day) {
		String date = year + "-" + NumberKit.fillPreZero(month, 2) + "-" + NumberKit.fillPreZero(day, 2);
		return str2Date(date, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 将日期转换成sqlDate
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static java.sql.Date convent2SqlDate(String year, String month, String day) {
		Date date = convent2Date(year, month, day);

		return date2SqlDate(date);
	}

	/**
	 * 将Date转换成sqlDate
	 * 
	 * @param utilDate
	 * @return
	 */
	public static java.sql.Date date2SqlDate(java.util.Date utilDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(utilDate);
		java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());

		return sqlDate;
	}

	/**
	 * 将sqlDate转换成Date
	 * 
	 * @param sqlDate
	 * @return
	 */
	public static java.util.Date sqlDate2Date(java.sql.Date sqlDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(sqlDate.getTime());
		java.util.Date utilDate = new java.util.Date(cal.getTimeInMillis());

		return utilDate;
	}

	/**
	 * 比较时间大小 默认和当前时间比较
	 * 
	 * @param time1
	 * @return
	 */
	public static boolean compareTime(String time1) {
		return compareTime(time1, getCurrentDateTime(), DEFAULT_DATE_TIME_FORMAT);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean compareTime(String time1, String time2) {
		return compareTime(time1, time2, DEFAULT_DATE_TIME_FORMAT);
	}

	/**
	 * 比较时间大小
	 * 
	 * @param time1
	 * @param time2
	 * @param dateFormat
	 * @return 如果time1小于time2返回true,否则返回false
	 */
	public static boolean compareTime(String time1, String time2, String dateFormat) {
		SimpleDateFormat t1 = new SimpleDateFormat(dateFormat);
		SimpleDateFormat t2 = new SimpleDateFormat(dateFormat);
		try {
			Date d1 = t1.parse(time1);
			Date d2 = t2.parse(time2);
			return d1.before(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
