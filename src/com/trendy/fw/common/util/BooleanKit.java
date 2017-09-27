package com.trendy.fw.common.util;

public class BooleanKit {
	public static Integer toInteger(Boolean b) {
		if (b == Boolean.TRUE) {
			return 1;
		}
		return 0;
	}

	public static Boolean toBoolean(Integer i) {
		if (i == 1) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
