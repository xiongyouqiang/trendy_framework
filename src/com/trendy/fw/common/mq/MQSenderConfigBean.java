package com.trendy.fw.common.mq;

public class MQSenderConfigBean {
	/** 是否支持事务 */
	private boolean transacted = true;
	/** 确认模式 */
	private int acknowledgeMode = 0;
	/** 发送模式 */
	private int deliveryMode = 0;

	public boolean isTransacted() {
		return transacted;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public int getAcknowledgeMode() {
		return acknowledgeMode;
	}

	public void setAcknowledgeMode(int acknowledgeMode) {
		this.acknowledgeMode = acknowledgeMode;
	}

	public int getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(int deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
}
