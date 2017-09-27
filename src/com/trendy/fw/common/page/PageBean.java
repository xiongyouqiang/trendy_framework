package com.trendy.fw.common.page;

public class PageBean {
	// 当前页数，第n页
	private int curPageNum = 0;

	// 每页的记录条数
	private int perPageSize = 20;

	// 总页数
	private int totalPageNum = 0;

	// 上一页
	private int prePageNum = 0;

	// 下一页
	private int nextPageNum = 0;

	// 起始记录条数
	private int startRecordNum = 0;

	// 终止记录条数
	private int endRecordNum = 0;

	// 总记录条数
	private int totalRecordNum = 0;

	public PageBean() {
	}

	public PageBean(int curPageNum, int perPageSize) {
		init(curPageNum, perPageSize);
	}

	public void init(int curPageNum, int perPageSize) {
		this.setCurPageNum(curPageNum);
		this.setPerPageSize(perPageSize);

		// 计算并设置起始记录条数和终止记录条数
		int startRecordNo = perPageSize * (curPageNum - 1);
		int endRecordNo = startRecordNo + perPageSize;

		this.setStartRecordNum(startRecordNo);
		this.setEndRecordNum(endRecordNo);
	}

	public void init(int curPageNum, int perPageSize, int totalRecordNum) {
		init(curPageNum, perPageSize);
		this.setTotalRecordNum(totalRecordNum);

		// 计算并设置总页数
		int totalPageNum = totalRecordNum / perPageSize;
		if ((totalRecordNum % perPageSize) != 0) {
			totalPageNum = totalPageNum + 1;
		}
		this.setTotalPageNum(totalPageNum);

		// 计算并设置上一页的页数
		int prePageNum = 0;
		if (curPageNum > 1) {
			prePageNum = curPageNum - 1;
		}
		this.setPrePageNum(prePageNum);

		// 计算并设置下一页的页数
		int nextPageNum = 0;
		if (curPageNum < totalPageNum) {
			nextPageNum = curPageNum + 1;
		}
		this.setNextPageNum(nextPageNum);
	}

	public int getCurPageNum() {
		return curPageNum;
	}

	public void setCurPageNum(int curPageNum) {
		this.curPageNum = curPageNum;
	}

	public int getPerPageSize() {
		return perPageSize;
	}

	public void setPerPageSize(int perPageSize) {
		this.perPageSize = perPageSize;
	}

	public int getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public int getPrePageNum() {
		return prePageNum;
	}

	public void setPrePageNum(int prePageNum) {
		this.prePageNum = prePageNum;
	}

	public int getNextPageNum() {
		return nextPageNum;
	}

	public void setNextPageNum(int nextPageNum) {
		this.nextPageNum = nextPageNum;
	}

	public int getStartRecordNum() {
		return startRecordNum;
	}

	public void setStartRecordNum(int startRecordNum) {
		this.startRecordNum = startRecordNum;
	}

	public int getEndRecordNum() {
		return endRecordNum;
	}

	public void setEndRecordNum(int endRecordNum) {
		this.endRecordNum = endRecordNum;
	}

	public int getTotalRecordNum() {
		return totalRecordNum;
	}

	public void setTotalRecordNum(int totalRecordNum) {
		this.totalRecordNum = totalRecordNum;
	}
}
