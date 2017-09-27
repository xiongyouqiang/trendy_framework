package com.trendy.fw.common.mq;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MQSender { 
	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	protected GenericObjectPool<MQObject> mqObjectPool;
	protected static final Object[] mqObjectArr = new Object[2];

	private class SenderBean implements Serializable{
		private static final long serialVersionUID = -3997955074618641931L;
		MQObject mqObject = null;
		MQSenderConfigBean mqSenderConfig = null;
		Connection mqConnection = null;
		Session mqSession = null;
		MessageProducer mqProducer = null;
		public MQObject getMqObject() { 
			return mqObject;
		}
		public void setMqObject(MQObject mqObject) {
			this.mqObject = mqObject;
		}
		public MQSenderConfigBean getMqSenderConfig() {
			return mqSenderConfig;
		}
		public void setMqSenderConfig(MQSenderConfigBean mqSenderConfig) {
			this.mqSenderConfig = mqSenderConfig;
		}
		public Session getMqSession() {
			return mqSession;
		}
		public void setMqSession(Session mqSession) {
			this.mqSession = mqSession;
		}
		public MessageProducer getMqProducer() {
			return mqProducer;
		}
		public void setMqProducer(MessageProducer mqProducer) {
			this.mqProducer = mqProducer;
		}
		public Connection getMqConnection() {
			return mqConnection;
		}
		public void setMqConnection(Connection mqConnection) {
			this.mqConnection = mqConnection;
		}		
	}
	
	public void send(String queueName, String message) throws Exception {
		int nextId = -1;
		try {
			while(nextId<0 || nextId>=mqObjectArr.length){
				nextId = new Random().nextInt(mqObjectArr.length);
			}
			
			SenderBean senderBean = prepareSend(nextId, queueName);
			if(senderBean!=null){
				MQSenderConfigBean senderConfigBean = senderBean.getMqSenderConfig();
				Session session = senderBean.getMqSession();
				MessageProducer producer = senderBean.getMqProducer();
				
				TextMessage textMessage = session.createTextMessage(message);
				log.info("准备向队列[{}]发送消息[{}]", queueName, message);
				producer.send(textMessage);
				log.info("向队列[{}]发送消息[{}]", queueName, message);
				if (senderConfigBean.isTransacted()) {
					session.commit();
				}
			}else{
				log.error("发送消息到MQ[{}]失败：", queueName);
				afterSend(nextId, queueName);
			}
			
		} catch (Exception e) {
			afterSend(nextId, queueName);
			log.error("发送消息到MQ[{}]失败：", queueName, e);
			throw e;
		}
	}
	
	public SenderBean prepareSend(int nextId, String queueName) throws Exception {
		if(nextId<0) return null;
		Map<String, SenderBean> sendBeanMap = (Map<String, SenderBean>)mqObjectArr[nextId];
		if(sendBeanMap!=null && sendBeanMap.get(queueName)!=null ) {
			return sendBeanMap.get(queueName);
		}
		
		MQObject mqObject = null;
		try {
			mqObject = mqObjectPool.borrowObject();
			// Connection ：客户端到JMS Provider 的连接
			Connection connection = mqObject.getConnection();
			// 启动
			// connection.start();

			MQSenderConfigBean senderConfigBean = mqObject.configBean.getSenderConfig();
			// Session： 一个发送或接收消息的线程
			Session session = connection.createSession(senderConfigBean.isTransacted(), senderConfigBean.getAcknowledgeMode());
			// 消息的目的地
			Destination destination = session.createQueue(queueName);
			// 消息发送者
			MessageProducer producer = session.createProducer(destination);

			// 设置是否持久化
			producer.setDeliveryMode(senderConfigBean.getDeliveryMode());
			
			SenderBean senderBean = new SenderBean();
			senderBean.setMqObject(mqObject);
			senderBean.setMqProducer(producer);
			senderBean.setMqSenderConfig(senderConfigBean);
			senderBean.setMqSession(session);
			senderBean.setMqConnection(connection);
			
			if(sendBeanMap==null){
				sendBeanMap = new ConcurrentHashMap<String, SenderBean>(); 
				mqObjectArr[nextId]=sendBeanMap;
			}
			sendBeanMap.put(queueName, senderBean);
			
			return senderBean;
		} catch (Exception e) {
			log.error("发送消息到MQ[{}]失败：", queueName, e);
			throw e;
		} finally{
			try {
				if(mqObject!=null){
					mqObjectPool.returnObject(mqObject);
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void afterSend(int nextId, String queueName) throws Exception {
		if (nextId < 0)
			return;
		if (mqObjectArr[nextId] == null)
			return;
		
		Map<String, SenderBean> sendBeanMap = (Map<String, SenderBean>)mqObjectArr[nextId];
		try {
			if(sendBeanMap!=null){
				SenderBean senderBean = sendBeanMap.get(queueName);
				if(senderBean!=null && senderBean.getMqSession()!=null){
					try {
						senderBean.getMqSession().close();
						senderBean.setMqSession(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(senderBean!=null && senderBean.getMqConnection()!=null){
					try {
						senderBean.getMqConnection().close();
						senderBean.setMqConnection(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sendBeanMap.remove(queueName);
				if(sendBeanMap.size()<=0){
					mqObjectArr[nextId]=null;
				}
			}
		} catch (Exception e) {
			log.error("MQ队列关闭错误", e);
		}
	}
	 
	
	
	/**
	 * 发送消息到指定队列中
	 * 
	 * @param queueName
	 *            队列名称
	 * @param message
	 *            消息内容
	 */
	public void send1(String queueName, String message) throws Exception {
		MQObject mqObject = null;
		Session session = null;
		try {
			mqObject = mqObjectPool.borrowObject();
			// Connection ：客户端到JMS Provider 的连接
			Connection connection = mqObject.getConnection();
			// 启动
			// connection.start();

			MQSenderConfigBean senderConfigBean = mqObject.configBean.getSenderConfig();
			// Session： 一个发送或接收消息的线程
			session = connection.createSession(senderConfigBean.isTransacted(), senderConfigBean.getAcknowledgeMode());
			// 消息的目的地
			Destination destination = session.createQueue(queueName);
			// 消息发送者
			MessageProducer producer = session.createProducer(destination);

			// 设置是否持久化
			producer.setDeliveryMode(senderConfigBean.getDeliveryMode());

			TextMessage textMessage = session.createTextMessage(message);
			producer.send(textMessage);
			log.info("向队列[{}]发送消息[{}]", queueName, message);
			if (senderConfigBean.isTransacted()) {
				session.commit();
			}
		} catch (Exception e) {
			log.error("发送消息到MQ[{}]失败：", queueName, e);
			throw e;
		} finally {
			try {
				if (null != session) {
					session.close();
				}
			} catch (Exception e) {
				log.error("MQ队列关闭错误", e);
			}
			try {
				mqObjectPool.returnObject(mqObject);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 发送多个消息到指定队列中
	 * 
	 * @param queueName
	 *            队列名称
	 * @param messageList
	 *            消息内容列表
	 */
	public void send(String queueName, List<String> messageList) throws Exception {
		MQObject mqObject = null;
		Session session = null;
		try {
			mqObject = mqObjectPool.borrowObject();
			// Connection ：客户端到JMS Provider 的连接
			Connection connection = mqObject.getConnection();
			// 启动
			// connection.start();

			MQSenderConfigBean senderConfigBean = mqObject.configBean.getSenderConfig();
			// Session： 一个发送或接收消息的线程
			session = connection.createSession(senderConfigBean.isTransacted(), senderConfigBean.getAcknowledgeMode());
			// 消息的目的地
			Destination destination = session.createQueue(queueName);
			// 消息发送者
			MessageProducer producer = session.createProducer(destination);
			// 设置是否持久化
			producer.setDeliveryMode(senderConfigBean.getDeliveryMode());

			for (String message : messageList) {
				TextMessage textMessage = session.createTextMessage(message);
				producer.send(textMessage);
				log.info("向队列[{}]发送消息[{}]", queueName, message);
			}
			if (senderConfigBean.isTransacted()) {
				session.commit();
			}
		} catch (Exception e) {
			log.error("发送消息到MQ[{}]失败：", queueName, e);
			throw e;
		} finally {
			try {
				if (null != session) {
					session.close();
				}
			} catch (Exception e) {
				log.error("MQ队列关闭错误", e);
			}
			try {
				mqObjectPool.returnObject(mqObject);
			} catch (Exception e) {
			}
		}
	}
}