package com.trendy.fw.common.mq;

public class MQReceiverConfigBean {
	/** 是否支持事务，默认不支持 */
	private boolean transacted = false;
	/** 确认模式 */
	private int acknowledgeMode = 0;

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
}
