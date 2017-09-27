package com.trendy.fw.common.util;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsKit {
	private static Logger log = LoggerFactory.getLogger(OsKit.class);

	public static String getHostName() {
		String hostName = "";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			log.error("getHostName error:", e);
		}
		return hostName;
	}
}
