package com.trendy.fw.common.mq.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.mq.MQReceiver;

public abstract class ActiveMQReceiver extends MQReceiver {
	private static Logger log = LoggerFactory.getLogger(ActiveMQReceiver.class);

	public ActiveMQReceiver(String brokerId) {
		this.mqObject = new ActiveMQObject(brokerId);
	}

	@Override
	public abstract void process(String message);

}
