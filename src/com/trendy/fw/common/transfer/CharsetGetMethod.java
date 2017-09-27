package com.trendy.fw.common.transfer;

import org.apache.commons.httpclient.methods.GetMethod;

public class CharsetGetMethod extends GetMethod {
	private String charSet;

	public CharsetGetMethod(String url, String charSet) {
		super(url);
		this.charSet = charSet;
	}

	public String getRequestCharSet() {
		return charSet;
	}
}
