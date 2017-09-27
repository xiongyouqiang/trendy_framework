package com.trendy.fw.common.db;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trendy.fw.common.util.DateKit;

public class SqlKit {
	private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DateKit.DEFAULT_DATE_TIME_FORMAT);

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串
	 */
	private static String fieldValue(String value) {
		if (null == value) {
			return "''";
		}
		String v = value.trim();
		int vs = v.length();
		StringBuilder sb = new StringBuilder();
		char c = 0;
		sb.append('\'');
		for (int i = 0; i < vs; i++) {
			c = v.charAt(i);
			// 防止sql注入处理，替换'为''，替换\为\\
			if ('\'' == c) {
				sb.append('\'');
				sb.append('\'');
			} else if ('\\' == c) {
				sb.append('\\');
				sb.append('\\');
			} else {
				sb.append(c);
			}
		}
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值，默认日期格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串
	 */
	private static String fieldValue(Date value) {
		return "'" + SIMPLE_DATE_FORMAT.format(value) + "'";
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串
	 */
	private static String fieldValue(Timestamp value) {
		return "'" + value + "'";
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * 
	 * @param value
	 *            -- 数据
	 * @return -- SQL片段字符串，如果value为null，返回字符串：''
	 */
	public static String fieldValue(Object value) {
		if (null == value) {
			return "''";
		} else if (value instanceof String) {
			return fieldValue((String) value);
		} else if (value instanceof Date) {
			return fieldValue((Date) value);
		} else if (value instanceof Timestamp) {
			return fieldValue((Timestamp) value);
		} else if (value instanceof Integer || value instanceof Long || value instanceof Short
				|| value instanceof Float || value instanceof Double) {
			// 基本数字类型
			return fieldValuePrimitive(value);
		} else if (value instanceof List) {
			return fieldValueArray(((List) value).toArray());
		} else if (value instanceof Set) {
			return fieldValueArray(((Set) value).toArray());
		} else if (value.getClass().isArray()) {
			// 数组类型，（基本数据类型没法进行autoboxing，需要进行额外处理）
			Class ct = value.getClass().getComponentType();
			if (ct == String.class) {
				return fieldValueArray(String[].class.cast(value));
			} else if (ct == int.class) {
				return fieldValueArray(boxedPrimitiveArray((int[]) value));
			} else if (ct == long.class) {
				return fieldValueArray(boxedPrimitiveArray((long[]) value));
			} else if (ct == short.class) {
				return fieldValueArray(boxedPrimitiveArray((short[]) value));
			} else if (ct == float.class) {
				return fieldValueArray(boxedPrimitiveArray((float[]) value));
			} else if (ct == double.class) {
				return fieldValueArray(boxedPrimitiveArray((double[]) value));
			}
			// 默认,转成Object对象数组
			return fieldValueArray((Object[]) value);
		} else {
			return "'" + value.toString() + "'";
		}
	}

	/**
	 * 拼接SQL语法的字段字符串值，适用于基本数据类型
	 * 
	 * @param value
	 * @return
	 */
	private static <T> String fieldValuePrimitive(T value) {
		return value.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值，适用于数组类型
	 * 
	 * @param value
	 * @return
	 */
	private static <T> String fieldValueArray(T[] value) {
		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder();
		for (int i = 0; i < value.length; i++) {
			sql.append(fieldValue(value[i]));
			if (i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 将int数组转换成Integer数组
	 * 
	 * @param array
	 * @return
	 */
	private static Integer[] boxedPrimitiveArray(int[] array) {
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * 将short数组转换成Short数组
	 * 
	 * @param array
	 * @return
	 */
	private static Short[] boxedPrimitiveArray(short[] array) {
		Short[] result = new Short[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * 将long数组转换成Long数组
	 * 
	 * @param array
	 * @return
	 */
	private static Long[] boxedPrimitiveArray(long[] array) {
		Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * 将float数组转换成Float数组
	 * 
	 * @param array
	 * @return
	 */
	private static Float[] boxedPrimitiveArray(float[] array) {
		Float[] result = new Float[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	/**
	 * 将double数组转换成Double数组
	 * 
	 * @param array
	 * @return
	 */
	private static Double[] boxedPrimitiveArray(double[] array) {
		Double[] result = new Double[array.length];
		for (int i = 0; i < array.length; i++)
			result[i] = array[i];
		return result;
	}

	public static String parseCountSql(String sql) {
//		Pattern pattern = Pattern.compile("(?<=select).*?(?=from)",Pattern.CASE_INSENSITIVE);
//		sql = sql.replace(matcher.group(0), " count(1) ");
		
		Pattern pattern = Pattern.compile("select .*? from", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sql);
		if (matcher.find()) {
			Pattern groupPattern = Pattern.compile("(?=sum|avg|count|distinct|max|min|union)", Pattern.CASE_INSENSITIVE);
			Matcher groupMatcher = groupPattern.matcher(sql);
			if (groupMatcher.find()) {
				//group by 分组的 sql，保留嵌套方法
				sql = "select count(1) from (" + sql + ") t";
			}else{
				//普通sql，替换原有字段
				sql = sql.replace(matcher.group(0), "select count(1) from ");
			}
		}
		return sql;
	}
	
//	public static String parseCountSql(String sql) {
//		sql = sql.toLowerCase();
//		
//		int iFromIndex = sql.indexOf("from");
//		sql = sql.replace(sql.substring(0,iFromIndex), "select count(1) ");
//		System.out.println(sql);
//		
//		return sql;
//	}

	public static String parseCondList2String(List<String> condList, boolean isNeedWhere) {
		StringBuilder sb = new StringBuilder();
		if (condList.size() > 0) {
			if (isNeedWhere) {
				sb.append(" where ");
			}
			for (int i = 0; i < condList.size(); i++) {
				String cond = condList.get(i);
				if (i > 0) {
					sb.append(" and ");
				}
				sb.append(cond);
			}
		}
		return sb.toString();
	}

	public static String parseInQuery(int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append("?");
		}
		return sb.toString();
	}
}
