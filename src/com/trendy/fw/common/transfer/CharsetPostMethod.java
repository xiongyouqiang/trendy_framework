package com.trendy.fw.common.transfer;

import org.apache.commons.httpclient.methods.PostMethod;

public class CharsetPostMethod extends PostMethod {
	private String charSet;

	public CharsetPostMethod(String url, String charSet) {
		super(url);
		this.charSet = charSet;
	}

	public String getRequestCharSet() {
		return charSet;
	}
}
