package com.trendy.fw.common.mq;

public class MQListenerConfigBean {
	/** queue名称 */
	private String queueName = "";
	/** 是否启动 */
	private boolean isStartup = false;
	/** 消息接收实现类 */
	private String receiverClass = "";

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean isStartup() {
		return isStartup;
	}

	public void setStartup(boolean isStartup) {
		this.isStartup = isStartup;
	}

	public String getReceiverClass() {
		return receiverClass;
	}

	public void setReceiverClass(String receiverClass) {
		this.receiverClass = receiverClass;
	}
}
