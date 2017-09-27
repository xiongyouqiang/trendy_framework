package com.trendy.fw.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.util.DateKit;

public class ExcelReadKit {

	protected static Logger log = LoggerFactory.getLogger(ExcelReadKit.class);

	/**
	 * 
	 * @param filePath
	 *            文件路径
	 * @param sheetIndex
	 *            第x个sheet
	 * @return
	 */
	public ExcelReadResultBean readExcel(String filePath, int sheetIndex) {
		ExcelReadResultBean resultBean = new ExcelReadResultBean();
		try {
			Sheet sheet = null;
			if (filePath.endsWith(ExcelConfig.FT_XLS)) {
				FileInputStream fis = new FileInputStream(filePath); // 根据excel文件路径创建文件流
				POIFSFileSystem fs = new POIFSFileSystem(fis); // 利用poi读取excel文件流
				HSSFWorkbook wb = new HSSFWorkbook(fs); // 读取excel工作簿
				sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个
			} else {
				OPCPackage pkg = OPCPackage.open(new File(filePath));
				XSSFWorkbook wb = new XSSFWorkbook(pkg);// 读取excel工作簿
				sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个
				pkg.close();
			}

			resultBean = realSheetValue(sheet);
		} catch (Exception e) {
			log.error("[读取Excel<{}>出错]：", filePath, e);
			resultBean.setResult(false);
			resultBean.setErrMsg("读取Excel文件出错");
		}
		return resultBean;
	}

	/**
	 * 
	 * @param fis
	 *            输入的文件流
	 * @param sheetIndex
	 *            第x个sheet
	 * @return
	 */
	public ExcelReadResultBean readExcel(InputStream fis, int sheetIndex) {
		ExcelReadResultBean resultBean = new ExcelReadResultBean();
		try {
			Sheet sheet = null;
			Workbook wb = null;
			try {
				POIFSFileSystem fs = new POIFSFileSystem(fis); // 利用poi读取excel文件流
				wb = new HSSFWorkbook(fs); // 读取excel工作簿
				sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个
			} catch (Exception e) {
				wb = new XSSFWorkbook(fis); // 读取excel工作簿
				sheet = wb.getSheetAt(sheetIndex); // 读取excel的sheet，0表示读取第一个
			}

			resultBean = realSheetValue(sheet);

			wb.cloneSheet(sheetIndex);
			fis.close();
		} catch (Exception e) {
			log.error("读取Excel文件流时出错：", e);
			resultBean.setResult(false);
			resultBean.setErrMsg("读取Excel文件流出错");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		return resultBean;
	}

	private ExcelReadResultBean realSheetValue(Sheet sheet) {
		ExcelReadResultBean resultBean = new ExcelReadResultBean();
		boolean result = true;
		String errMsg = "";
		List<List<String>> list = new ArrayList<List<String>>();

		int i = 0, j = 0;
		for (i = 0; i <= sheet.getLastRowNum(); i++) {
			try {
				Row row = sheet.getRow(i); // 取出sheet中的某一行数据
				if (row != null) {
					List<String> rowList = new ArrayList<String>(row.getPhysicalNumberOfCells());
					// 获取该行中总共有多少列数据row.getLastCellNum()
					for (j = 0; j < row.getLastCellNum(); j++) {
						try {
							Cell cell = row.getCell(j); // 获取该行中的一个单元格对象
							/*
							 * 当取某一行中的数据的时候，需要判断数据类型，否则会报错
							 * java.lang.NumberFormatException: You cannot get a
							 * string value from a numeric cell等等错误
							 */
							if (cell != null) {// 判断cell是否为空
								if (cell.getCellType() == ExcelConfig.CT_NUMERIC) {
									if (HSSFDateUtil.isCellDateFormatted(cell)) {// 判断是否日期类型
										Date dateValue = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
										rowList.add(DateKit.formatDate(dateValue, DateKit.DEFAULT_DATE_TIME_FORMAT));
									} else {
										rowList.add(String.valueOf(cell.getNumericCellValue()));
									}
								} else if (cell.getCellType() == ExcelConfig.CT_FORMULA) {// 读取公式的值
									try {
										rowList.add(String.valueOf(cell.getNumericCellValue()));
									} catch (IllegalStateException e) {
										rowList.add(String.valueOf(cell.getRichStringCellValue()));
									}
								} else {
									rowList.add(cell.getStringCellValue());
								}
							} else {// 如果cell为空，用空格字段代替
								rowList.add("");
							}
						} catch (Exception e) {
							log.error("读取{}行{}列时出错", i + 1, j + 1);
							result = false;
							errMsg = errMsg + "读取" + (i + 1) + "行" + (j + 1) + "列时出错;";
							rowList.add("");
						}
					}
					list.add(rowList);
				}
			} catch (Exception e) {
				log.error("读取{}行时出错", i + 1);
				result = false;
				errMsg = errMsg + "读取" + (i + 1) + "行时出错";
			}
		}

		resultBean.setResult(result);
		resultBean.setErrMsg(errMsg);
		resultBean.setContentList(list);
		return resultBean;
	}
}
