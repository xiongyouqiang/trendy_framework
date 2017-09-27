package com.trendy.fw.common.mq.webspheremq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.mq.MQSender;

public class WebSphereMQSender extends MQSender {
	private static Logger log = LoggerFactory.getLogger(WebSphereMQSender.class);

	public WebSphereMQSender(String brokerId) {
		WebSphereMQObjectPool pool = new WebSphereMQObjectPool();
		this.mqObjectPool = pool.getMQObjectPool(brokerId);
	}
}
