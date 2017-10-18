package com.trendy.fw.common.util;

import java.util.Comparator;
import java.util.List;

public class ContentComparator<T> implements Comparator<T> {
	public static final String REGEX_CONDITION = ";";
	public static final String REGEX_DESCRPTION = ",";
	public static final String REGEX_SORT_ASC = "asc";
	public static final String REGEX_SORT_DESC = "desc";

	private List<String> compareKeyList = null;

	public void setCompareKey(String compareKeyStr) {
		compareKeyList = ListKit.string2List(compareKeyStr, REGEX_CONDITION);
	}

	public int compare(T object1, T object2) {
		try {
			for (String keyFieldStr : compareKeyList) {
				String[] keyFieldArray = keyFieldStr.split(REGEX_DESCRPTION);
				String keyFieldName = keyFieldArray[0];
				String sortStr = keyFieldArray[1];

				String fieldType = object1.getClass().getDeclaredField(keyFieldName).getGenericType().toString();
				String methodName = "get" + StringKit.upperFirstChar(keyFieldName);

				Object value1 = object1.getClass().getMethod(methodName).invoke(object1);
				Object value2 = object2.getClass().getMethod(methodName).invoke(object2);
				int result = compareField(fieldType, value1, value2);
				if (result != 0) {
					if (sortStr.equals(REGEX_SORT_DESC)) {
						return -result;
					} else {
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	public int compareField(String fieldType, Object value1, Object value2) {
		int result = 0;
		if (fieldType.equals("int")) {
			result = Integer.valueOf(value1.toString()) - Integer.valueOf(value2.toString());
		} else if (fieldType.equals("double")) {
			double doubleResult = Double.valueOf(value1.toString()) - Double.valueOf(value2.toString());
			if (doubleResult == 0) {
				result = 0;
			} else if (doubleResult > 0) {
				result = 1;
			} else {
				result = -1;
			}
		} else if (fieldType.equals("long")) {
			long longResult = Long.valueOf(value1.toString()) - Long.valueOf(value2.toString());
			if (longResult == 0) {
				result = 0;
			} else if (longResult > 0) {
				result = 1;
			} else {
				result = -1;
			}
		} else {
			result = value1.toString().compareTo(value2.toString());
		}
		return result;
	}
}
