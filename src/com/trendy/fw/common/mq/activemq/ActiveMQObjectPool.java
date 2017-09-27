package com.trendy.fw.common.mq.activemq;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.trendy.fw.common.mq.MQObject;
import com.trendy.fw.common.mq.MQObjectFactory;
import com.trendy.fw.common.mq.MQObjectPoolConfigBean;

public class ActiveMQObjectPool {
	private static Map<String, GenericObjectPool<MQObject>> mqObjectPool = new ConcurrentHashMap<String, GenericObjectPool<MQObject>>(
			100);

	static {
		initMQObjectPool();
	}

	private static void initMQObjectPool() {
		ActiveMQInitializer initializer = new ActiveMQInitializer();
		List<ActiveMQConfigBean> configList = initializer.getActiveMQConfigList();
		for (ActiveMQConfigBean bean : configList) {
			MQObjectFactory factory = new ActiveMQObjectFactory(bean.getBrokerId());

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
