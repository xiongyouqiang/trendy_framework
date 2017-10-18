package com.trendy.fw.common.mq.activemq;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQListener implements ServletContextListener {
	private static Logger log = LoggerFactory.getLogger(ActiveMQListener.class);
	public static Thread thread = null;

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("ActiveMQListener启动");
		if (thread == null) {
			thread = new Thread(new Runnable() {
				public void run() {
					new ActiveMQInitializer().initReceiverListenerList();
				}
			});
			thread.start();
		}
		log.info("ActiveMQListener启动完成");
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("ActiveMQListener销毁");
		if (thread != null) {
			thread.interrupt();
		}
		log.info("ActiveMQListener销毁完成");
	}
}
