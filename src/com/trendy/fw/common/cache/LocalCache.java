package com.trendy.fw.common.cache;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.oscache.base.EntryRefreshPolicy;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.opensymphony.oscache.web.filter.ExpiresRefreshPolicy;

public class LocalCache {
	private static Logger log = LoggerFactory.getLogger(LocalCache.class);

	private static GeneralCacheAdministrator admin = null;
	private static LocalCache instance = null;

	public static LocalCache getInstance() {
		if (instance == null) {
			synchronized (LocalCache.class) {
				if (instance == null) {
					instance = new LocalCache();
				}
			}
		}
		return instance;
	}

	private LocalCache() {
		admin = new GeneralCacheAdministrator();
	}

	public GeneralCacheAdministrator getGeneralCacheAdministrator() {
		return admin;
	}

	/**
	 * 本地缓存获取数据,如果过期,使用数据源刷洗本地缓存
	 * 
	 * @param <T>
	 *            获取的类型
	 * @param key
	 *            数据对应的key
	 * @param refreshSrc
	 *            拥有刷新数据的数据源
	 * @return 数据对象
	 */
	public static <T> T getCache(String key, int refreshPeriod, Callable refreshSrc) {
		return (T) getCache(key, null, refreshPeriod, refreshSrc);
	}

	/**
	 * 本地缓存获取数据,如果过期,使用数据源刷洗本地缓存
	 * 
	 * @param <T>
	 *            获取的类型
	 * @param key
	 *            数据对应的key
	 * @param cacheGroup
	 *            数据保存在那个组
	 * @param refreshSrc
	 *            拥有刷新数据的数据源
	 * @return 数据对象
	 */
	public static <T> T getCache(String key, String cacheGroup, int refreshPeriod, Callable refreshSrc) {
		boolean updated = false;
		Object object = null;
		try {
			object = instance.get(key, refreshPeriod);
		} catch (NeedsRefreshException nre) {

			String[] cacheGroups = null;
			if (null != cacheGroup) {
				cacheGroups = new String[] { cacheGroup };
			}

			try {
				object = refreshSrc.call();
				if (null != object) { // 保存到本地缓存
					instance.set(key, object, cacheGroups);
					updated = true;
				}
			} catch (Exception e) {
				log.error("LocalCache从根结点查找应用对应缓存组异常：", e);
			} finally {
				if (!updated) { // 缓存更新失败
					object = nre.getCacheContent(); // 取得一个老的版本
					if (null != object) {
						instance.set(key, object, cacheGroups); // 重新设置本地缓存延长过期时间
					} else {
						instance.cancelUpdate(key); // 取消更新
					}
				}
			}
		}
		if (null == object) {
			String errorMessage = "无法通过本地缓存获取数据. key:" + key + ", cacheGroup:" + cacheGroup;
			log.warn(errorMessage);
			throw new NullPointerException(errorMessage);
		}
		return (T) object;
	}

	/**
	 * 取消更新 只用于在处理捕获的NeedsRefreshException异常并尝试生成新缓存内容失效的时候
	 * 
	 * @param key
	 */
	public void cancelUpdate(String key) {
		admin.cancelUpdate(key);
	}

	/**
	 * 从缓存中移除一个key标识的对象
	 * 
	 * @param key
	 */
	public void delete(String key) {
		admin.removeEntry(key);
	}

	/**
	 * 更新一个key标识的对象
	 * 
	 * @param key
	 */
	public void flush(String key) {
		admin.flushEntry(key);
	}

	/**
	 * 更新所有缓存
	 */
	public void flushAll() {
		admin.flushAll();
	}

	/**
	 * 更新一组属于属于某个指定group的cache
	 * 
	 * @param group
	 */
	public void flushGroup(String group) {
		admin.flushGroup(group);
	}

	/**
	 * 从缓存中获取一个key标识的对象
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) throws NeedsRefreshException {
		return admin.getFromCache(key);
	}

	/**
	 * 从缓存中获取一个key标识的对象. refreshPeriod刷新周期,标识此对象在缓存中保存的时间(单位:秒)
	 * 
	 * @param key
	 * @param refreshPeriod
	 *            过期时间,单位为秒
	 * @return
	 * @throws NeedsRefreshException
	 */
	public Object get(String key, int refreshPeriod) throws NeedsRefreshException {
		return admin.getFromCache(key, refreshPeriod);
	}

	/**
	 * 存储一个由Key标识的缓存对象
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		admin.putInCache(key, value);
	}

	public void set(String key, Object value, int refreshPeriod) {
		EntryRefreshPolicy policy = new ExpiresRefreshPolicy(refreshPeriod);
		admin.putInCache(key, value, policy);
	}

	/**
	 * 存储一个由Key标识的缓存对象到指定的多个组中
	 * 
	 * @param key
	 *            标识
	 * @param value
	 *            缓存对象
	 * @param groups
	 *            指定组
	 */
	public void set(String key, Object value, String[] groups) {
		admin.putInCache(key, value, groups);
	}

	/**
	 * 存储一个由Key标识的缓存对象到指定的单个组中
	 * 
	 * @param key
	 *            标识
	 * @param value
	 *            缓存对象
	 * @param group
	 *            指定组
	 */
	public void set(String key, Object value, String group) {
		admin.putInCache(key, value, new String[] { group });
	}
}
