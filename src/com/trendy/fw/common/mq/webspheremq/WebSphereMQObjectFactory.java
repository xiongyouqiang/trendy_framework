package com.trendy.fw.common.mq.webspheremq;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.trendy.fw.common.mq.MQObjectFactory;

public class WebSphereMQObjectFactory extends MQObjectFactory<WebSphereMQObject> {
	
	public WebSphereMQObjectFactory(String brokerId) {
		this.brokerId = brokerId;
	}

	@Override
	public WebSphereMQObject create() throws Exception {
		return new WebSphereMQObject(brokerId);
	}

	@Override
	public PooledObject<WebSphereMQObject> wrap(WebSphereMQObject obj) {
		return new DefaultPooledObject<WebSphereMQObject>(obj);
	}

	@Override
	public void destroyObject(PooledObject<WebSphereMQObject> p) throws Exception {
		WebSphereMQObject mqObject = (WebSphereMQObject) p;
		mqObject.close();
	}

}
