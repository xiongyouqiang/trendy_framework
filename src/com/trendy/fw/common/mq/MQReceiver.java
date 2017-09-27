package com.trendy.fw.common.mq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MQReceiver {
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);

	protected MQObject mqObject = null;

	/**
	 * 从指定队列中获取一条记录
	 * 
	 * @param queueName
	 *            队列名称
	 */
	public void receive(String queueName) {
		Connection connection = null;// Connection ：JMS 客户端到JMS Provider 的连接
		Session session = null;
		try {
			connection = mqObject.getConnection();
			// 启动
			connection.start();

			MQReceiverConfigBean receiverConfigBean = mqObject.configBean.getReceiverConfig();

			// Session： 一个发送或接收消息的线程
			session = connection.createSession(receiverConfigBean.isTransacted(),
					receiverConfigBean.getAcknowledgeMode());
			// 消息的目的地
			Destination destination = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(destination);

			try {
				// 设置接收者接收消息的时间
				TextMessage message = (TextMessage) consumer.receive(100000);
				if (null != message) {
					log.info("从队列[{}]接收到消息[{}]", queueName, message.getText());
					process(message.getText());
				}
			} catch (Exception e) {
				log.error("从队列[{}]接收到消息失败：", queueName, e);
			}
		} catch (Exception e) {
			log.error("从队列[{}]接收到消息失败：", queueName, e);
		} finally {
			try {
				if (null != session) {
					session.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 监听指定队列
	 * 
	 * @param queueName
	 *            队列名称
	 */
	public void receiveBylistener(final String queueName) {
		Connection connection = null;// Connection ：JMS 客户端到JMS Provider 的连接
		Session session = null;
		try {
			connection = mqObject.getConnection();
			// connection.setExceptionListener(this);
			// 启动
			connection.start();

			MQReceiverConfigBean receiverConfigBean = mqObject.configBean.getReceiverConfig();
			// Session： 一个发送或接收消息的线程
			session = connection.createSession(receiverConfigBean.isTransacted(),
					receiverConfigBean.getAcknowledgeMode());
			// 消息的目的地
			Destination destination = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(destination);

			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message arg0) {
					try {
						if (arg0 instanceof TextMessage) {
							TextMessage message = (TextMessage) arg0;
							if (null != message) {
								log.info("从队列[{}]接收到消息[{}]", queueName, message.getText());
								process(message.getText());
							}
						} else {
							log.error("从队列[{}]接收到消息[{}]不是TextMessage类型", queueName, arg0.toString());
						}
					} catch (JMSException jmse) {
						log.error("从队列[{}]接收到消息失败：", queueName, jmse);
					} catch (Exception e) {
						log.error("从队列[{}]接收到消息处理出错：", queueName, e);
					}
				}
			});
		} catch (Exception e) {
			log.error("从队列[{}]接收到消息失败：", queueName, e);
			try {
				if (null != session) {
					session.close();
				}
				if (null != connection) {
					connection.close();
				}
			} catch (Exception ce) {
			}
		}
	}

	public abstract void process(String message);
}
