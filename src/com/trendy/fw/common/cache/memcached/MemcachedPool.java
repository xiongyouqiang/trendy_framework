package com.trendy.fw.common.cache.memcached;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcachedPool {
	private static Logger log = LoggerFactory.getLogger(MemcachedPool.class);

	// 配置文件路径
	private static String REMOTE_CACHE_CONFIG_PATH = "/config/remote_cache_config.xml";
	private static String CONFIG_REGEX = ",";

	// 配置信息
	private static List<MemcacheConfigBean> configList = new ArrayList<MemcacheConfigBean>(100);
	private static Map<String, MemcacheConfigBean> configMap = new HashMap<String, MemcacheConfigBean>(100);
	// 客户端链接池
	private static Map<String, MemCachedClient> memcachedPool = new ConcurrentHashMap<String, MemCachedClient>(100);

	static {
		init();
	}

	private static void init() {
		initConfigList();
		initMemcachedPool();
	}

	/**
	 * 读取配置文件内容到configList
	 */
	private static void initConfigList() {
		try {
			InputStreamReader in = new InputStreamReader(
					MemcachedPool.class.getResourceAsStream(REMOTE_CACHE_CONFIG_PATH));
			SAXReader reader = new SAXReader();
			Document document = reader.read(in);
			Element root = document.getRootElement();
			Iterator<Element> iter = root.elementIterator("remote_cache");

			while (iter.hasNext()) {
				MemcacheConfigBean bean = new MemcacheConfigBean();
				Element config = iter.next();

				bean.setAppId(config.elementText("app_id"));

				// 服务器IP及端口
				String servers = config.elementText("servers");
				String[] serversArray = servers.split(CONFIG_REGEX);
				bean.setServers(serversArray);

				// 服务器权重
				String weights = config.elementText("weights");
				String[] weightsStrArray = weights.split(CONFIG_REGEX);
				Integer[] weightsArray = new Integer[weightsStrArray.length];
				for (int i = 0; i < weightsStrArray.length; i++) {
					weightsArray[i] = Integer.parseInt(weightsStrArray[i]);
				}
				bean.setWeights(weightsArray);

				bean.setInitConn(Integer.parseInt(config.elementText("init_conn")));
				bean.setMinConn(Integer.parseInt(config.elementText("min_conn")));
				bean.setMaxConn(Integer.parseInt(config.elementText("max_conn")));
				bean.setMaxIdle(Long.parseLong(config.elementText("max_idle")));
				bean.setMaxBusyTime(Long.parseLong(config.elementText("max_busy_time")));
				bean.setMaintSleep(Integer.parseInt(config.elementText("maint_sleep")));
				bean.setSocketTimeOut(Integer.parseInt(config.elementText("socket_time_out")));
				bean.setSocketConnectTimeOut(Integer.parseInt(config.elementText("socket_connect_time_out")));
				bean.setNagle(Boolean.parseBoolean(config.elementText("nagle")));
				bean.setFailover(Boolean.parseBoolean(config.elementText("failover")));
				bean.setFailback(Boolean.parseBoolean(config.elementText("failback")));
				bean.setAliveCheck(Boolean.parseBoolean(config.elementText("alive_check")));

				configList.add(bean);
				configMap.put(bean.getAppId(), bean);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[Memcached配置]加载Memcached配置文件异常：", e);
		}
	}

	/**
	 * 初始化链接池
	 */
	private static void initMemcachedPool() {
		for (MemcacheConfigBean config : configList) {
			SockIOPool pool = SockIOPool.getInstance(config.getAppId());
			pool.setServers(config.getServers());
			pool.setWeights(config.getWeights());
			pool.setInitConn(config.getInitConn());
			pool.setMinConn(config.getMinConn());
			pool.setMaxConn(config.getMaxConn());
			pool.setMaxIdle(config.getMaxIdle());
			pool.setMaxBusyTime(config.getMaxBusyTime());
			pool.setMaintSleep(config.getMaintSleep());
			pool.setSocketTO(config.getSocketTimeOut());
			pool.setSocketConnectTO(config.getSocketConnectTimeOut());
			pool.setNagle(config.getNagle());
			pool.setFailover(config.getFailover());
			pool.setFailback(config.getFailback());
			pool.setAliveCheck(config.getAliveCheck());
			pool.initialize();

			MemCachedClient client = new MemCachedClient(config.getAppId());
			client.setPrimitiveAsString(true);

			memcachedPool.put(config.getAppId(), client);
		}
	}

	/**
	 * 从连接池中获取链接
	 * 
	 * @param appId
	 * @return
	 */
	public static MemCachedClient getMemCachedClient(String appId) {
		return memcachedPool.get(appId);
	}

	/**
	 * 从配置列表中获取某个配置内容
	 * 
	 * @param appId
	 * @return
	 */
	public static MemcacheConfigBean getMemcacheConfig(String appId) {
		return configMap.get(appId);
	}
}
