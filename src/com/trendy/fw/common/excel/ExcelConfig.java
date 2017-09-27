package com.trendy.fw.common.excel;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelConfig {
	// Excel文件输出格式
	public static final String FT_XLS = "xls";
	public static final String FT_XLSX = "xlsx";

	// Excel格数据类型
	public static final int CT_NUMERIC = Cell.CELL_TYPE_NUMERIC;
	public static final int CT_STRING = Cell.CELL_TYPE_STRING;
	public static final int CT_BOOLEAN = Cell.CELL_TYPE_BOOLEAN;
	public static final int CT_BLANK = Cell.CELL_TYPE_BLANK;
	public static final int CT_FORMULA = Cell.CELL_TYPE_FORMULA;
}
