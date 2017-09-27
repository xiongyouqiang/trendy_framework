package com.trendy.fw.common.mq.webspheremq;

import com.trendy.fw.common.mq.MQConfigBean;

public class WebSphereMQConfigBean extends MQConfigBean {
	/** host名 */
	private String hostName = "";
	/** 渠道 */
	private String channel = "";
	/** 编码 */
	private int ccsid;
	/** 端口 */
	private int port;
	/** queue管理者 */
	private String queueManager = "";

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getCcsid() {
		return ccsid;
	}

	public void setCcsid(int ccsid) {
		this.ccsid = ccsid;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getQueueManager() {
		return queueManager;
	}

	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
}
