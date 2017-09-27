package com.trendy.fw.common.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.util.StringKit;

public class ExcelWriteKit {
	protected static Logger log = LoggerFactory.getLogger(ExcelWriteKit.class);

	private short fontSize = 11;

	private CellStyle cellStyleCommon = null;
	private CellStyle cellStyleHeader = null;
	private CellStyle cellStyleNumeric = null;
	private CellStyle cellStyleDate = null;

	private Font fontStyleCommon = null;
	private Font fontStyleBolder = null;

	private boolean isNeedStyle = true;

	public ExcelWriteKit() {

	}

	public Workbook createWorkbook(String fileType) {
		Workbook wb = null;
		if (fileType.equals(ExcelConfig.FT_XLS)) {
			wb = new HSSFWorkbook();
		} else {
			wb = new SXSSFWorkbook(-1);
		}
		return wb;
	}

	/**
	 * 创建一个内容格，字符串格式
	 * 
	 * @param wb
	 *            工作表
	 * @param row
	 *            行
	 * @param cellIndex
	 *            列
	 * @param cellValue
	 *            内容值
	 * @return
	 */
	public Cell createCell(Workbook wb, Row row, int cellIndex, String cellValue) {
		Cell cell = row.createCell(cellIndex);
		cell.setCellType(ExcelConfig.CT_STRING);
		cell.setCellValue(cellValue);
		cell.setCellStyle(getCommonCellStyle(wb));
		return cell;
	}

	private CellStyle getCommonCellStyle(Workbook wb) {
		if (isNeedStyle) {
			if (cellStyleCommon == null) {
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle = addCellBorder(cellStyle);
				cellStyle.setFont(getCellFont(wb));
				cellStyleCommon = cellStyle;
			}
		}
		return cellStyleCommon;
	}

	/**
	 * 创建一个表头内容格
	 * 
	 * @param wb
	 *            工作表
	 * @param row
	 *            列
	 * @param cellIndex
	 *            行
	 * @param cellValue
	 *            内容值
	 * @return
	 */
	public Cell createHeaderCell(Workbook wb, Row row, int cellIndex, String cellValue) {
		Cell cell = row.createCell(cellIndex);
		cell.setCellValue(cellValue);
		cell.setCellStyle(getHeaderCellStyle(wb));
		return cell;
	}

	private CellStyle getHeaderCellStyle(Workbook wb) {
		if (isNeedStyle) {
			if (cellStyleHeader == null) {
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle = addCellBorder(cellStyle);
				cellStyle.setFont(getCellBoldFont(wb));
				cellStyleHeader = cellStyle;
			}
		}
		return cellStyleHeader;
	}

	/**
	 * 创建一个数字内容格
	 * 
	 * @param wb
	 *            内容表
	 * @param row
	 *            列
	 * @param cellIndex
	 *            行
	 * @param cellValue
	 *            内容值
	 * @param formatStr
	 *            格式
	 * @return
	 */
	public Cell createNumericCell(Workbook wb, Row row, int cellIndex, double cellValue, String formatStr) {
		Cell cell = row.createCell(cellIndex, ExcelConfig.CT_NUMERIC);
		cell.setCellValue(cellValue);
		cell.setCellStyle(getNumericCellStyle(wb, formatStr));
		return cell;
	}

	private CellStyle getNumericCellStyle(Workbook wb, String formatStr) {
		if (isNeedStyle) {
			if (cellStyleNumeric == null) {
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle = addCellBorder(cellStyle);
				cellStyle.setFont(getCellFont(wb));

				DataFormat format = wb.createDataFormat();
				cellStyle.setDataFormat(format.getFormat(formatStr));
				cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);

				cellStyleNumeric = cellStyle;
			}
		}
		return cellStyleNumeric;
	}

	/**
	 * 创建一个日期内容格
	 * 
	 * @param wb
	 *            内容表
	 * @param row
	 *            列
	 * @param cellIndex
	 *            行
	 * @param cellValue
	 *            内容值
	 * @param formatStr
	 *            格式
	 * @return
	 */
	public Cell createDateCell(Workbook wb, Row row, int cellIndex, Date cellValue, String formatStr) {
		Cell cell = row.createCell(cellIndex);
		cell.setCellValue(cellValue);
		cell.setCellStyle(getDateCellStyle(wb, formatStr));
		return cell;
	}

	private CellStyle getDateCellStyle(Workbook wb, String formatStr) {
		if (isNeedStyle) {
			if (cellStyleDate == null) {
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle = addCellBorder(cellStyle);
				cellStyle.setFont(getCellFont(wb));
				cellStyleDate = cellStyle;
			}
		}
		return cellStyleDate;
	}

	/**
	 * 增加内容格边线
	 * 
	 * @param cellStyle
	 * @return
	 */
	public CellStyle addCellBorder(CellStyle cellStyle) {
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 获取普通字体
	 * 
	 * @param wb
	 *            工作表
	 * @return
	 */
	public Font getCellFont(Workbook wb) {
		if (fontStyleCommon == null) {
			Font font = wb.createFont();
			font.setFontHeightInPoints(getFontSize());
			fontStyleCommon = font;
		}
		return fontStyleCommon;
	}

	/**
	 * 获取加粗字体
	 * 
	 * @param wb
	 *            工作表
	 * @return
	 */
	public Font getCellBoldFont(Workbook wb) {
		if (fontStyleBolder == null) {
			Font font = wb.createFont();
			font.setFontHeightInPoints(getFontSize());
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			fontStyleBolder = font;
		}
		return fontStyleBolder;
	}

	/**
	 * 写Excel工作簿
	 * 
	 * @param bean
	 *            WriteExcelBean
	 * @return Workbook工作簿对象
	 */
	public Workbook writeExcel(ExcelWriteBean bean) {
		Workbook wb = createWorkbook(bean.getFileType());
		String sheetName = bean.getSheetName();
		if (!StringKit.isValid(sheetName)) {
			sheetName = "sheet1";
		}
		Sheet sheet = wb.createSheet(sheetName);

		// 处理表头内容
		if (bean.getHeaderList().size() > 0) {
			Row row = sheet.createRow(0);
			for (int i = 0; i < bean.getHeaderList().size(); i++) {
				String headerValue = bean.getHeaderList().get(i);
				createHeaderCell(wb, row, i, headerValue);
			}
		}

		// 处理表中内容
		if (bean.getContentList().size() > 0) {
			int rowCount = 1;// 行计数器
			// 没有表头的情况
			if (bean.getHeaderList().size() == 0) {
				rowCount = 0;
			}

			for (List<Object> contentList : bean.getContentList()) {
				Row row = sheet.createRow(rowCount);
				for (int i = 0; i < contentList.size(); i++) {
					Object cellValue = contentList.get(i);
					if (getCellType(i, bean.getCellTypeMap()) == ExcelConfig.CT_NUMERIC) {
						if (cellValue == null) {// 如果值为空，默认填0
							cellValue = new Integer(0);
						}
						createNumericCell(wb, row, i, Double.valueOf(cellValue.toString()),
								getCellFormat(i, bean.getCellFormatMap()));
					} else {
						if (cellValue == null) {// 如果值为空，默认空字符串
							cellValue = new String("");
						}
						createCell(wb, row, i, cellValue.toString());
					}
				}
				rowCount++;

				if (rowCount % 100 == 0) {
					try {
						((SXSSFSheet) sheet).flushRows(100);
					} catch (Exception e) {
						log.error("", e);
					}
				}
			}

		}
		return wb;
	}
	
	/**
	 * 写Excel工作簿多个sheet
	 * 
	 * @param bean
	 *            WriteExcelBean
	 * @return Workbook工作簿对象
	 */
	public Workbook writeExcel(List<ExcelWriteBean> excelWriteList) {
		if(excelWriteList == null || excelWriteList.size()==0){
			return null;
		}
		Workbook wb = createWorkbook(excelWriteList.get(0).getFileType());
		
		int sheetNumber = 0;
		for(ExcelWriteBean bean : excelWriteList){
			sheetNumber++;
			String sheetName = bean.getSheetName();
			if (!StringKit.isValid(sheetName)) {
				sheetName = "sheet"+sheetNumber;
			}
			Sheet sheet = wb.createSheet(sheetName);
	
			// 处理表头内容
			if (bean.getHeaderList().size() > 0) {
				Row row = sheet.createRow(0);
				for (int i = 0; i < bean.getHeaderList().size(); i++) {
					String headerValue = bean.getHeaderList().get(i);
					createHeaderCell(wb, row, i, headerValue);
				}
			}
	
			// 处理表中内容
			if (bean.getContentList().size() > 0) {
				int rowCount = 1;// 行计数器
				// 没有表头的情况
				if (bean.getHeaderList().size() == 0) {
					rowCount = 0;
				}
	
				for (List<Object> contentList : bean.getContentList()) {
					Row row = sheet.createRow(rowCount);
					for (int i = 0; i < contentList.size(); i++) {
						Object cellValue = contentList.get(i);
						if (getCellType(i, bean.getCellTypeMap()) == ExcelConfig.CT_NUMERIC) {
							if (cellValue == null) {// 如果值为空，默认填0
								cellValue = new Integer(0);
							}
							createNumericCell(wb, row, i, Double.valueOf(cellValue.toString()),
									getCellFormat(i, bean.getCellFormatMap()));
						} else {
							if (cellValue == null) {// 如果值为空，默认空字符串
								cellValue = new String("");
							}
							createCell(wb, row, i, cellValue.toString());
						}
					}
					rowCount++;
	
					if (rowCount % 100 == 0) {
						try {
							((SXSSFSheet) sheet).flushRows(100);
						} catch (Exception e) {
							log.error("", e);
						}
					}
				}
	
			}
		}
		return wb;
	}

	/**
	 * 输出Excel文件
	 * 
	 * @param bean
	 *            WriteExcelBean
	 * @param filePath
	 *            文件全路径
	 */
	public void outputExcel(ExcelWriteBean bean, String filePath) {
		FileOutputStream fos = null;
		try {
			Workbook wb = writeExcel(bean);
			String fileName = bean.getFileName() + "." + bean.getFileType();
			fos = new FileOutputStream(filePath + Constants.FILE_SEPARATOR + fileName);
			wb.write(fos);
			fos.close();
		} catch (IOException e) {
			log.error("输出文件[{}]出错：", filePath, e);
		} catch (Exception e) {
			log.error("输出文件[{}]出错：", filePath, e);
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (Exception e) {
				log.error("输出文件[{}]出错：", filePath, e);
			}
		}
	}

	/**
	 * 获取cell的类型
	 * 
	 * @param cellIndex
	 * @param cellTypeMap
	 * @return
	 */
	private int getCellType(int cellIndex, HashMap<Integer, Integer> cellTypeMap) {
		int cellType = ExcelConfig.CT_STRING;
		try {
			if (!cellTypeMap.isEmpty()) {
				cellType = cellTypeMap.get(cellIndex);
			}
		} catch (Exception e) {
			cellType = ExcelConfig.CT_STRING;
		}
		return cellType;
	}

	/**
	 * 获取cell的格式
	 * 
	 * @param cellIndex
	 * @param cellFormatMap
	 * @return
	 */
	private String getCellFormat(int cellIndex, HashMap<Integer, String> cellFormatMap) {
		String cellFormat = "";
		try {
			if (!cellFormatMap.isEmpty()) {
				cellFormat = cellFormatMap.get(cellIndex);
			}
		} catch (Exception e) {
			cellFormat = "";
		}
		return cellFormat;
	}

	public short getFontSize() {
		return fontSize;
	}

	public void setFontSize(short fontSize) {
		this.fontSize = fontSize;
	}

	public CellStyle getCellStyleCommon() {
		return cellStyleCommon;
	}

	public void setCellStyleCommon(CellStyle cellStyleCommon) {
		this.cellStyleCommon = cellStyleCommon;
	}

	public CellStyle getCellStyleHeader() {
		return cellStyleHeader;
	}

	public void setCellStyleHeader(CellStyle cellStyleHeader) {
		this.cellStyleHeader = cellStyleHeader;
	}

	public CellStyle getCellStyleNumeric() {
		return cellStyleNumeric;
	}

	public void setCellStyleNumeric(CellStyle cellStyleNumeric) {
		this.cellStyleNumeric = cellStyleNumeric;
	}

	public CellStyle getCellStyleDate() {
		return cellStyleDate;
	}

	public void setCellStyleDate(CellStyle cellStyleDate) {
		this.cellStyleDate = cellStyleDate;
	}

	public Font getFontStyleCommon() {
		return fontStyleCommon;
	}

	public void setFontStyleCommon(Font fontStyleCommon) {
		this.fontStyleCommon = fontStyleCommon;
	}

	public Font getFontStyleBolder() {
		return fontStyleBolder;
	}

	public void setFontStyleBolder(Font fontStyleBolder) {
		this.fontStyleBolder = fontStyleBolder;
	}

	public boolean getIsNeedStyle() {
		return isNeedStyle;
	}

	public void setIsNeedStyle(boolean isNeedStyle) {
		this.isNeedStyle = isNeedStyle;
	}
}
