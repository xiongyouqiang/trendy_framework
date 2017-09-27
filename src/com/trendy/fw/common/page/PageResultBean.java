package com.trendy.fw.common.page;

import java.util.List;

public class PageResultBean<T> {
	private int totalRecordNum = 0;

	private List<T> recordList = null;

	public PageResultBean() {
	}

	public List<T> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<T> recordList) {
		this.recordList = recordList;
	}

	public int getTotalRecordNum() {
		return totalRecordNum;
	}

	public void setTotalRecordNumber(int totalRecordNum) {
		this.totalRecordNum = totalRecordNum;
	}
}
