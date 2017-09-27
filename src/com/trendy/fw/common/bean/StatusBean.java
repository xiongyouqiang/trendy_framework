package com.trendy.fw.common.bean;

public class StatusBean {
	private String status = "";
	private String value = "";
	private int intStatus = 0;

	public StatusBean(String status, String value) {
		this.setStatus(status);
		this.setValue(value);
	}

	public StatusBean(int status, String value) {
		this.setIntStatus(status);
		this.setStatus(String.valueOf(status));
		this.setValue(value);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIntStatus() {
		return intStatus;
	}

	public void setIntStatus(int intStatus) {
		this.intStatus = intStatus;
	}
}
