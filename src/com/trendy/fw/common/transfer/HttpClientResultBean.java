package com.trendy.fw.common.transfer;

public class HttpClientResultBean {
	private boolean result = false;
	private String resultContent = "";
	private byte[] resultByteContent = null;

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}

	public byte[] getResultByteContent() {
		return resultByteContent;
	}

	public void setResultByteContent(byte[] resultByteContent) {
		this.resultByteContent = resultByteContent;
	}
}