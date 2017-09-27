package com.trendy.fw.common.mq.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.mq.MQSender;

public class ActiveMQSender extends MQSender {
	private static Logger log = LoggerFactory.getLogger(ActiveMQSender.class);

	public ActiveMQSender(String brokerId) {
		ActiveMQObjectPool pool = new ActiveMQObjectPool();
		this.mqObjectPool = pool.getMQObjectPool(brokerId);
	}

}
