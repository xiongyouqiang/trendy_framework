package com.trendy.fw.common.mq.webspheremq;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.mq.MQListenerConfigBean;
import com.trendy.fw.common.mq.MQObject;
import com.trendy.fw.common.mq.MQObjectFactory;
import com.trendy.fw.common.mq.MQObjectPoolConfigBean;
import com.trendy.fw.common.mq.MQReceiverConfigBean;
import com.trendy.fw.common.mq.MQSenderConfigBean;
import com.trendy.fw.common.mq.activemq.ActiveMQConfigBean;
import com.trendy.fw.common.mq.activemq.ActiveMQInitializer;
import com.trendy.fw.common.mq.activemq.ActiveMQObjectFactory;
import com.trendy.fw.common.util.JsonKit;

public class WebSphereMQObjectPool {
	private static Map<String, GenericObjectPool<MQObject>> mqObjectPool = new ConcurrentHashMap<String, GenericObjectPool<MQObject>>(
			100);

	static {
		initMQObjectPool();
	}

	private static void initMQObjectPool() {
		WebSphereMQInitializer initializer = new WebSphereMQInitializer();
		List<WebSphereMQConfigBean> configList = initializer.getWebSphereMQConfigList();
		for (WebSphereMQConfigBean bean : configList) {
			MQObjectFactory factory = new WebSphereMQObjectFactory(bean.getBrokerId());

			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			MQObjectPoolConfigBean poolConfigBean = bean.getObjectPoolConfig();
			config.setMaxTotal(poolConfigBean.getMaxTotal());
			config.setBlockWhenExhausted(poolConfigBean.getBlockWhenExhausted());
			config.setMaxWaitMillis(poolConfigBean.getMaxWaitMillis());
			config.setNumTestsPerEvictionRun(poolConfigBean.getNumTestsPerEvictionRun());
			config.setTestOnBorrow(poolConfigBean.getTestOnBorrow());
			config.setTestOnReturn(poolConfigBean.getTestOnReturn());
			config.setTestWhileIdle(poolConfigBean.getTestWhileIdle());
			config.setTimeBetweenEvictionRunsMillis(poolConfigBean.getTimeBetweenEvictionRunsMillis());
			config.setMinEvictableIdleTimeMillis(poolConfigBean.getMinEvictableIdleTimeMillis());

			GenericObjectPool<MQObject> pool = new GenericObjectPool<MQObject>(factory, config);

			mqObjectPool.put(bean.getBrokerId(), pool);
		}
	}

	public GenericObjectPool<MQObject> getMQObjectPool(String brokerId) {
		return mqObjectPool.get(brokerId);
	}
}
