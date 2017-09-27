package com.trendy.fw.common.bean;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SimpleAuthenticator extends Authenticator {
	private PasswordAuthentication passwordAuthentication = null;

	public SimpleAuthenticator(String userName, String password) {
		passwordAuthentication = new PasswordAuthentication(userName, password);
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return passwordAuthentication;
	}
}
