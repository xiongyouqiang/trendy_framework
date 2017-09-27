package com.trendy.fw.common.excel;

import java.util.List;

public class ExcelReadResultBean {
	private boolean result = true;
	private String errMsg = "";
	private List<List<String>> contentList = null;

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public List<List<String>> getContentList() {
		return contentList;
	}

	public void setContentList(List<List<String>> contentList) {
		this.contentList = contentList;
	}
}
