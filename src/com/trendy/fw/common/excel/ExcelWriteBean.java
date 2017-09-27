package com.trendy.fw.common.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelWriteBean {
	String fileName = "";// 文件名，不带后缀
	String fileType = "";// 文件类型，xsl或xslx
	String sheetName = "";// sheet名
	List<String> headerList = new ArrayList<String>();// 表头列表，可以为空
	List<List<Object>> contentList = new ArrayList<List<Object>>();// 内容列表
	HashMap<Integer, Integer> cellTypeMap = new HashMap<Integer, Integer>();// 表格类型，不填写默认为字符串
	HashMap<Integer, String> cellFormatMap = new HashMap<Integer, String>();// 表格格式，不填写默认为字符串格式

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<String> getHeaderList() {
		return headerList;
	}

	public void setHeaderList(List<String> headerList) {
		this.headerList = headerList;
	}

	public List<List<Object>> getContentList() {
		return contentList;
	}

	public void setContentList(List<List<Object>> contentList) {
		this.contentList = contentList;
	}

	public HashMap<Integer, Integer> getCellTypeMap() {
		return cellTypeMap;
	}

	public void setCellTypeMap(HashMap<Integer, Integer> cellTypeMap) {
		this.cellTypeMap = cellTypeMap;
	}

	public void setCellType(int cellIndex, int cellType) {
		cellTypeMap.put(cellIndex, cellType);
	}

	public HashMap<Integer, String> getCellFormatMap() {
		return cellFormatMap;
	}

	public void setCellFormatMap(HashMap<Integer, String> cellFormatMap) {
		this.cellFormatMap = cellFormatMap;
	}

	public void setCellFormat(int cellIndex, String cellFormat) {
		cellFormatMap.put(cellIndex, cellFormat);
	}
}
