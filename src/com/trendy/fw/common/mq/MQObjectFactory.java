package com.trendy.fw.common.mq;

import org.apache.commons.pool2.BasePooledObjectFactory;

public abstract class MQObjectFactory<MQObject> extends BasePooledObjectFactory<MQObject> {
	protected String brokerId;
}
