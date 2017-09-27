package com.trendy.fw.common.mq.webspheremq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.mq.MQReceiver;

public abstract class WebSphereMQReceiver extends MQReceiver {
	private static Logger log = LoggerFactory.getLogger(WebSphereMQReceiver.class);
	
	public WebSphereMQReceiver(String brokerId){
		this.mqObject = new WebSphereMQObject(brokerId);
	}

	@Override
	public abstract void process(String message);

}
