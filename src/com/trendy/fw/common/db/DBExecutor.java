package com.trendy.fw.common.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trendy.fw.common.page.PageBean;
import com.trendy.fw.common.page.PageResultBean;
import com.trendy.fw.common.util.BeanKit;

public abstract class DBExecutor {
	protected static final Logger log = LoggerFactory.getLogger(DBExecutor.class);

	public int getTotalRecordResult(String sql, List<String> condList, List<String> paramList, DBObject db)
			throws SQLException {
		sql = sql + SqlKit.parseCondList2String(condList, true);
		return getTotalRecordResult(sql, paramList, db);
	}

	public int getTotalRecordResult(String sql, List<String> paramList, DBObject db) throws SQLException {
		int result = 0;
		String sqlCount = SqlKit.parseCountSql(sql);
		log.info("SQL Count = {}", sqlCount);
		PreparedStatement ps = db.getPreparedStatement(sqlCount);

		for (int i = 0; i < paramList.size(); i++) {
			ps.setObject(i + 1, paramList.get(i));
		}

		ResultSet rs = db.executePstmtQuery();
		if (rs.next()) {
			result = rs.getInt(1);
		}
		rs.close();
		return result;
	}

	public <E> List<E> getRecordListResult(String sql, String orderQuery, List<String> condList,
			List<String> paramList, Class<E> clazz, PageBean pageBean, DBObject db) throws SQLException, Exception {
		sql = sql + SqlKit.parseCondList2String(condList, true);
		sql = sql + " " + orderQuery;
		return getRecordListResult(sql, paramList, clazz, pageBean, db);
	}

	public abstract <E> List<E> getRecordListResult(String sql, List<String> paramList, Class<E> clazz,
			PageBean pageBean, DBObject db) throws SQLException, Exception;

	public <E> PageResultBean<E> getPageResult(String sql, String orderQuery, List<String> condList,
			List<String> paramList, Class<E> clazz, PageBean pageBean, DBObject db) throws SQLException, Exception {
		sql = sql + SqlKit.parseCondList2String(condList, true);
		log.info("SQL = {}", sql);

		return getPageResult(sql, orderQuery, paramList, clazz, pageBean, db);
	}

	public <E> PageResultBean<E> getPageResult(String sql, String orderQuery, List<String> paramList, Class<E> clazz,
			PageBean pageBean, DBObject db) throws SQLException, Exception {
		PageResultBean<E> resultBean = new PageResultBean<E>();

		int total = getTotalRecordResult(sql, paramList, db);
		resultBean.setTotalRecordNumber(total);

		List<E> list = new ArrayList<E>();
		if (total > 0) {
			sql = sql + " " + orderQuery;
			list = getRecordListResult(sql, paramList, clazz, pageBean, db);
		}
		resultBean.setRecordList(list);

		return resultBean;
	}

	public abstract String parsePageSql(String sql, int startNum, int endNum);

	public <E> List<E> getRecordListResult(String sql, String orderQuery, List<String> condList,
			List<String> paramList, Class<E> clazz, DBObject db) throws SQLException, Exception {
		sql = sql + SqlKit.parseCondList2String(condList, true);
		sql = sql + " " + orderQuery;
		return getRecordListResult(sql, paramList, clazz, db);
	}

	public <E> List<E> getRecordListResult(String sql, List<String> paramList, Class<E> clazz, DBObject db)
			throws SQLException, Exception {
		log.info("SQL = {}", sql);
		List<E> list = new ArrayList<E>();
		PreparedStatement ps = db.getPreparedStatement(sql);

		int index = 0;
		for (index = 0; index < paramList.size(); index++) {
			ps.setObject(index + 1, paramList.get(index));
		}

		ResultSet rs = db.executePstmtQuery();
		while (rs.next()) {
			E bean = clazz.newInstance();
			bean = BeanKit.resultSet2Bean(rs, clazz);
			list.add(bean);
		}
		rs.close();
		return list;
	}
}
