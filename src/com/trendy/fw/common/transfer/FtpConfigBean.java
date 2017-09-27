package com.trendy.fw.common.transfer;

import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPTransferType;

public class FtpConfigBean {
	private String host;
	private int port;
	private String user;
	private String password;
	private FTPTransferType contentType = FTPTransferType.ASCII;
	private FTPConnectMode connectMode = FTPConnectMode.ACTIVE;
	private int timeout = 60 * 1000;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public FTPTransferType getContentType() {
		return contentType;
	}

	public void setContentType(FTPTransferType contentType) {
		this.contentType = contentType;
	}

	public FTPConnectMode getConnectMode() {
		return connectMode;
	}

	public void setConnectMode(FTPConnectMode connectMode) {
		this.connectMode = connectMode;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
