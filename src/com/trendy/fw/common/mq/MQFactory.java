package com.trendy.fw.common.mq;

public abstract class MQFactory {
	/**
	 * 获取MQ对象
	 * 
	 * @return
	 */
	public abstract MQObject getMQObject();

	/**
	 * 获取MQ发送者
	 * 
	 * @return
	 */
	public abstract MQSender getMQSender();
}
