package com.trendy.fw.common.mq.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.mq.MQObject;
import com.trendy.fw.common.util.DateKit;

public class ActiveMQObject extends MQObject {
	private static Logger log = LoggerFactory.getLogger(ActiveMQObject.class);
	private ActiveMQConfigBean activeMQConfigBean = null;	

	public ActiveMQObject(String brokerId) {
		this.brokerId = brokerId;

		ActiveMQInitializer initializer = new ActiveMQInitializer();
		this.activeMQConfigBean = initializer.getActiveMQConfig(brokerId);
		this.configBean = activeMQConfigBean;
		
		sign = DateKit.getCurrentDateTime();
	}

	@Override
	public Connection createConnection() {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(activeMQConfigBean.getUserName(),
					activeMQConfigBean.getPassword(), activeMQConfigBean.getBrokerUrl());// 构造ConnectionFactory实例对象
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
		} catch (Exception e) {
			log.error("ActiveMQObject createConnection error:", e);
		}
		return connection;
	}

}
