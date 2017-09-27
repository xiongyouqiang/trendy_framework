package com.trendy.fw.common.mq.activemq;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.trendy.fw.common.mq.MQObjectFactory;

public class ActiveMQObjectFactory extends MQObjectFactory<ActiveMQObject> {
	
	public ActiveMQObjectFactory(String brokerId) {
		this.brokerId = brokerId;
	}

	@Override
	public ActiveMQObject create() throws Exception {
		return new ActiveMQObject(brokerId);
	}

	@Override
	public PooledObject<ActiveMQObject> wrap(ActiveMQObject obj) {
		return new DefaultPooledObject<ActiveMQObject>(obj);
	}

	@Override
	public void destroyObject(PooledObject<ActiveMQObject> p) throws Exception {
		ActiveMQObject activeMQObject = (ActiveMQObject) p;
		activeMQObject.close();
	}

}
