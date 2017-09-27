package com.trendy.fw.common.web;

import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.util.JsonKit;
import com.trendy.fw.common.util.XmlKit;

public class ReturnMessageBean {
	private int code = Constants.STATUS_NOT_VALID;
	private String message = "";
	private String command = "";
	private Object content;

	public ReturnMessageBean() {
	}

	public ReturnMessageBean(int code, String message, String command, Object content) {
		this.setCode(code);
		this.setMessage(message);
		this.setCommand(command);
		this.setContent(content);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String toJson() {
		return JsonKit.toJson(this);
	}

	public String toXml() {
		return XmlKit.toXml(this);
	}
}
