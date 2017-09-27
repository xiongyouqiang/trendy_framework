package com.trendy.fw.common.mq.activemq;

import com.trendy.fw.common.mq.MQConfigBean;

public class ActiveMQConfigBean extends MQConfigBean {
	/** 中间件URL */
	private String brokerUrl = "";
	/** 用户名 */
	private String userName = "";
	/** 密码 */
	private String password = "";

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
