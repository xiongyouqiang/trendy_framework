package com.trendy.fw.common.mq;

import javax.jms.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MQObject {
	private static Logger log = LoggerFactory.getLogger(MQObject.class);

	protected Connection connection = null;
	protected String brokerId = "";
	protected MQConfigBean configBean = null;
	protected String sign = "";

	/**
	 * 获取链接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		log.debug("sign = {}", sign);
		if (connection == null) {
			log.debug("createConnection sign [{}]", sign);
			createConnection();
		}
		return connection;
	}

	/**
	 * 创建链接
	 * 
	 * @return
	 */
	public abstract Connection createConnection();

	/**
	 * 关闭链接
	 */
	public void close() {
		try {
			if (null != connection) {
				connection.close();
			}
		} catch (Exception e) {
			log.error("MQ connection close error:", e);
		}
	}
}
