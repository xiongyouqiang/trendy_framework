package com.trendy.fw.common.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trendy.fw.common.util.ListKit;

public abstract class DBFactory {
	// 获取写数据库链接
	public abstract DBObject getDBObjectW() throws SQLException;

	// 获取读数据库链接
	public abstract DBObject getDBObjectR() throws SQLException;

	// 获取数据执行工具类
	public abstract DBExecutor getDBExecutor();

	// 初始化
	protected abstract void init();

	/**
	 * 通过数据库名列表字符串生成列表，字符串使用分号分割
	 * 
	 * @param connStr
	 * @return
	 */
	protected List<String> parseConnString2List(String connStr) {
		List<String> list = ListKit.string2List(connStr, ";");
		return list;
	}

	/**
	 * 通过数据库名列表字符串生成Map，字符串使用分号和等号分割，如：a=3;b=5;c=2
	 * 
	 * @param connStr
	 * @return
	 */
	protected Map<Integer, String> parseConnString2Map(String connStr) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<String[]> list = ListKit.string2ListOfArray(connStr, ";", "=");

		int count = 0;
		for (String[] dbArray : list) {
			int weight = Integer.parseInt(dbArray[1]);
			for (int i = 0; i < weight; i++) {
				map.put(count, dbArray[0]);
				count++;
			}
		}
		return map;
	}
}