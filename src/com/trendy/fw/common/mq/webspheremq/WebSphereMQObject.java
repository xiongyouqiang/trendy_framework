package com.trendy.fw.common.mq.webspheremq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.v6.jms.internal.JMSC;
import com.trendy.fw.common.mq.MQObject;
import com.trendy.fw.common.util.DateKit;

public class WebSphereMQObject extends MQObject {
	private static Logger log = LoggerFactory.getLogger(WebSphereMQObject.class);
	
	WebSphereMQConfigBean webSphereMQConfigBean = null;

	public WebSphereMQObject(String brokerId) {
		this.brokerId = brokerId;

		WebSphereMQInitializer initializer = new WebSphereMQInitializer();
		webSphereMQConfigBean = initializer.getWebSphereMQConfig(brokerId);
		this.configBean = webSphereMQConfigBean;
		
		sign = DateKit.getCurrentDateTime();
	}

	@Override
	public Connection createConnection() {
		try {
			MQConnectionFactory factory = new MQConnectionFactory();// 构造ConnectionFactory实例对象
			factory.setHostName(webSphereMQConfigBean.getHostName());
			factory.setChannel(webSphereMQConfigBean.getChannel());
			factory.setCCSID(webSphereMQConfigBean.getCcsid());
			factory.setPort(webSphereMQConfigBean.getPort());
			factory.setQueueManager(webSphereMQConfigBean.getQueueManager());
			factory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
			factory.setBrokerVersion(JMSC.MQJMS_BROKER_V2);
			factory.setFailIfQuiesce(JMSC.MQJMS_FIQ_YES);

			ConnectionFactory connectionFactory = factory;
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
		} catch (Exception e) {
			log.error("WebSphereMQObject createConnection error:", e);
		}
		return connection;
	}

}
