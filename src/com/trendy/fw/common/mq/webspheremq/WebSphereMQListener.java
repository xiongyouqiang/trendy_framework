package com.trendy.fw.common.mq.webspheremq;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSphereMQListener implements ServletContextListener {
	private static Logger log = LoggerFactory.getLogger(WebSphereMQListener.class);
	public static Thread thread = null;

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("WebSphereMQListener启动");
		if (thread == null) {
			thread = new Thread(new Runnable() {
				public void run() {
					new WebSphereMQInitializer().initReceiverListenerList();
				}
			});
			thread.start();
		}
		log.info("WebSphereMQListener启动完成");
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("WebSphereMQListener销毁");
		if (thread != null) {
			thread.interrupt();
		}
		log.info("WebSphereMQListener销毁完成");
	}
}
