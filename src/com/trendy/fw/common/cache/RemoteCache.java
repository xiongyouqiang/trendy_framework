package com.trendy.fw.common.cache;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.danga.MemCached.MemCachedClient;
import com.trendy.fw.common.cache.memcached.MemcachedPool;

public class RemoteCache {
	private static Logger log = LoggerFactory.getLogger(RemoteCache.class);

	private static RemoteCache instance = null;

	/**
	 * 实例化
	 * 
	 * @return
	 */
	public static RemoteCache getInstance() {
		if (instance == null) {
			synchronized (RemoteCache.class) {
				if (instance == null) {
					instance = new RemoteCache();
				}
			}
		}
		return instance;
	}

	private RemoteCache() {
	}

	/**
	 * 获取缓存内容
	 * 
	 * @param appId
	 * @param key
	 * @return
	 */
	public Object get(String appId, String key) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		if (client == null) {
			return null;
		}
		return client.get(key);
	}

	/**
	 * 设置缓存内容，默认为长期有效
	 * 
	 * @param appId
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String appId, String key, Object value) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.set(key, value);
	}

	/**
	 * 设置有期限的缓存内容
	 * 
	 * @param appId
	 * @param key
	 * @param value
	 * @param refreshPeriod
	 *            过期时间，该过期时间注意app与memcached服务器上保持一致
	 * @return
	 */
	public boolean set(String appId, String key, Object value, long refreshPeriod) {
		// 计算过期时间
		long expiryTime = System.currentTimeMillis() + (refreshPeriod * 1000);
		Date expiry = new Date(expiryTime);

		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.add(key, value, expiry);
	}

	/**
	 * 删除缓存
	 * 
	 * @param appId
	 * @param key
	 * @return
	 */
	public boolean delete(String appId, String key) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.delete(key);
	}

	/**
	 * 自增步长
	 * 
	 * @param appId
	 * @param key
	 * @return
	 */
	public long incrementCounter(String appId, String key) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.addOrIncr(key);
	}

	/**
	 * 自增指定步长
	 * 
	 * @param appId
	 * @param key
	 * @param step
	 * @return
	 */
	public long incrementCounter(String appId, String key, int step) {
		return incrementCounter(appId, key, (long) step);
	}

	/**
	 * 自增指定步长
	 * 
	 * @param appId
	 * @param key
	 * @param step
	 * @return
	 */
	public long incrementCounter(String appId, String key, long step) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.addOrIncr(key, step);
	}

	/**
	 * 自减步长，自减到0
	 * 
	 * @param appId
	 * @param key
	 * @return
	 */
	public long decrementCounter(String appId, String key) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.addOrDecr(key);
	}

	/**
	 * 自减指定步长，自减到0
	 * 
	 * @param appId
	 * @param key
	 * @param step
	 * @return
	 */
	public long decrementCounter(String appId, String key, int step) {
		return decrementCounter(appId, key, (long) step);
	}

	/**
	 * 自减指定步长，自减到0
	 * 
	 * @param appId
	 * @param key
	 * @param step
	 * @return
	 */
	public long decrementCounter(String appId, String key, long step) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		return client.addOrDecr(key, step);
	}

	/**
	 * 获取指定应用下的统计信息
	 * 
	 * @param appId
	 * @return
	 */
	public Map<String, Map<String, String>> stats(String appId) {
		String[] servers = MemcachedPool.getMemcacheConfig(appId).getServers();
		return stats(appId, servers);
	}

	/**
	 * 获取指定应用下特定服务器的统计信息
	 * 
	 * @param appId
	 * @param servers
	 * @return
	 */
	public Map<String, Map<String, String>> stats(String appId, java.lang.String[] servers) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		Map<String, Map<String, String>> result = client.stats();
		return result;
	}

	/**
	 * 获取指定应用的特定Slab上的条目详细信息
	 * 
	 * @param appId
	 * @param slabNumber
	 * @param limit
	 * @return
	 */
	public Map<String, Map<String, String>> statsCacheDump(String appId, int slabNumber, int limit) {
		String[] servers = MemcachedPool.getMemcacheConfig(appId).getServers();
		return statsCacheDump(appId, servers, slabNumber, limit);
	}

	/**
	 * 获取指定应用下服务器的特定Slab上的条目详细信息
	 * 
	 * @param appId
	 * @param servers
	 *            如：172.19.8.42:11211,172.19.8.42:11212，多台服务器使用半角逗号分隔
	 * @param slabNumber
	 * @param limit
	 * @return
	 */
	public Map<String, Map<String, String>> statsCacheDump(String appId, java.lang.String[] servers, int slabNumber,
			int limit) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		Map<String, Map<String, String>> result = client.statsCacheDump(servers, slabNumber, limit);
		return result;
	}

	/**
	 * 获取指定应用下缓存条目统计信息
	 * 
	 * @param appId
	 * @return
	 */
	public Map<String, Map<String, String>> statsItems(String appId) {
		String[] servers = MemcachedPool.getMemcacheConfig(appId).getServers();
		return statsItems(appId, servers);
	}

	/**
	 * 获取指定应用下服务器的缓存条目统计信息
	 * 
	 * @param appId
	 * @param servers
	 *            如：172.19.8.42:11211,172.19.8.42:11212，多台服务器使用半角逗号分隔
	 * @return
	 */
	public Map<String, Map<String, String>> statsItems(String appId, java.lang.String[] servers) {
		MemCachedClient client = MemcachedPool.getMemCachedClient(appId);
		Map<String, Map<String, String>> result = client.statsItems(servers);
		return result;
	}
}
