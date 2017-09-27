package com.trendy.fw.common.mq;

import java.util.List;

public class MQConfigBean {
	/** 中间件ID */
	private String brokerId = "";
	/** 发送者配置 */
	private MQSenderConfigBean senderConfig;
	/** 接收者配置 */
	private MQReceiverConfigBean receiverConfig;
	/** 接收者监听器配置 */
	private List<MQListenerConfigBean> listenerConfigList;
	/** MQ对象池配置 */ 
	private MQObjectPoolConfigBean objectPoolConfig;

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public MQSenderConfigBean getSenderConfig() {
		return senderConfig;
	}

	public void setSenderConfig(MQSenderConfigBean senderConfig) {
		this.senderConfig = senderConfig;
	}

	public MQReceiverConfigBean getReceiverConfig() {
		return receiverConfig;
	}

	public void setReceiverConfig(MQReceiverConfigBean receiverConfig) {
		this.receiverConfig = receiverConfig;
	}

	public List<MQListenerConfigBean> getListenerConfigList() {
		return listenerConfigList;
	}

	public void setListenerConfigList(List<MQListenerConfigBean> listenerConfigList) {
		this.listenerConfigList = listenerConfigList;
	}

	public MQObjectPoolConfigBean getObjectPoolConfig() {
		return objectPoolConfig;
	}

	public void setObjectPoolConfig(MQObjectPoolConfigBean objectPoolConfig) {
		this.objectPoolConfig = objectPoolConfig;
	}
}
