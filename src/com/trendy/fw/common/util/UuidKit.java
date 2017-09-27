package com.trendy.fw.common.util;

import java.util.UUID;

public class UuidKit {
	/**
	 * 获取随机生成的uuid
	 * 
	 * @return
	 */
	public static String getUuid() {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		return id.replaceAll("-", "");
	}
}
