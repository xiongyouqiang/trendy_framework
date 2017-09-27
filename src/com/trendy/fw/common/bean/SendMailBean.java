package com.trendy.fw.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.trendy.fw.common.config.Constants;
import com.trendy.fw.common.util.StringKit;

public class SendMailBean {
	private static String DEFAULT_CHARSET = Constants.CODE_UNICODE;// 默认编码
	public static final String DEFAULT_TIMEOUT = String.valueOf(10 * 60 * 1000);// 默认超时时间

	private String smtpServer = "";// stmp服务器
	private String sendUserName = "";// 邮件发送帐号
	private String sendUserPwd = "";// 邮件发送密码
	private String sendFrom = "";// 邮件发送人
	private List<String> toUserList = null;// 收件人列表
	private List<String> ccUserList = null;// 抄送人列表
	private List<String> bccUserList = null;// 密送人列表

	private String charset = DEFAULT_CHARSET;// 编码
	private String connectionTimeout = DEFAULT_TIMEOUT;// 链接超时时间
	private String socketTimeout = DEFAULT_TIMEOUT;// socket超时时间

	private String subject = "";// 邮件主题
	private String content = "";// 内容
	private String contentType = "";// 内容类型
	private List<String> attachmentList = null;// 附件列表

	/**
	 * 增加一个收件人
	 * 
	 * @param toUserName
	 */
	public void setToUser(String toUserName) {
		if (toUserList == null) {
			toUserList = new ArrayList<String>();
		}
		toUserList.add(toUserName);
	}

	/**
	 * 增加一个抄送人
	 * 
	 * @param ccUserName
	 */
	public void setCcUser(String ccUserName) {
		if (ccUserList == null) {
			ccUserList = new ArrayList<String>();
		}
		toUserList.add(ccUserName);
	}

	/**
	 * 增加一个密送人
	 * 
	 * @param bccUserName
	 */
	public void setBccUser(String bccUserName) {
		if (bccUserList == null) {
			bccUserList = new ArrayList<String>();
		}
		toUserList.add(bccUserName);
	}

	/**
	 * 增加一个附件
	 * 
	 * @param fileName
	 */
	public void setAttachment(String fileName) {
		if (attachmentList == null) {
			attachmentList = new ArrayList<String>();
		}
		attachmentList.add(fileName);
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserPwd() {
		return sendUserPwd;
	}

	public void setSendUserPwd(String sendUserPwd) {
		this.sendUserPwd = sendUserPwd;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		this.sendFrom = sendFrom;
	}

	public List<String> getToUserList() {
		return toUserList;
	}

	public void setToUserList(List<String> toUserList) {
		this.toUserList = toUserList;
	}

	public List<String> getCcUserList() {
		return ccUserList;
	}

	public void setCcUserList(List<String> ccUserList) {
		this.ccUserList = ccUserList;
	}

	public List<String> getBccUserList() {
		return bccUserList;
	}

	public void setBccUserList(List<String> bccUserList) {
		this.bccUserList = bccUserList;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(String socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<String> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<String> attachmentList) {
		this.attachmentList = attachmentList;
	}
}
