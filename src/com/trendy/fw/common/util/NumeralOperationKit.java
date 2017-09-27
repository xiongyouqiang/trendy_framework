package com.trendy.fw.common.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumeralOperationKit {
	/**
	 * 加法
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double add(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));

		return b1.add(b2).doubleValue();
	}

	/**
	 * 减法
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double subtract(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));

		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 乘法
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double multiply(double value1, double value2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));

		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 除法，默认四舍五入取值
	 * 
	 * @param value1
	 * @param value2
	 * @param scale
	 *            小数位
	 * @return
	 */
	public static double divide(double value1, double value2, int scale) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));

		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 除法
	 * 
	 * @param value1
	 * @param value2
	 * @param scale
	 *            小数位
	 * @param roundingMode
	 *            小数保留模式，见BigDecimal.ROUND_XX参数
	 * @return
	 */
	public static double divide(double value1, double value2, int scale, int roundingMode) {
		BigDecimal b1 = new BigDecimal(String.valueOf(value1));
		BigDecimal b2 = new BigDecimal(String.valueOf(value2));

		return b1.divide(b2, scale, roundingMode).doubleValue();
	}

	/**
	 * 四舍五入
	 * 
	 * @param value
	 * @param scale
	 * @return
	 */
	public static double round(double value, int scale) {
		BigDecimal b = new BigDecimal(String.valueOf(value));

		// 以1作为除数
		return b.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 计算百分比
	 * 
	 * @param numerator
	 * @param total
	 * @param scale
	 * @return
	 */
	public static String calculatePercent(double numerator, double total, int scale) {
		String result = "";
		try {
			double rate = 0;
			if (total != 0) {
				rate = multiply(divide(numerator, total, scale + 2), 100);
			}

			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			numberFormat.setMaximumFractionDigits(scale);
			rate = Double.parseDouble(numberFormat.format(rate));

			result = NumberKit.formatDouble(rate, scale);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
