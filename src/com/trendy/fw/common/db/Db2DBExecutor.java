package com.trendy.fw.common.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.trendy.fw.common.page.PageBean;
import com.trendy.fw.common.util.BeanKit;

public class Db2DBExecutor extends DBExecutor {

	public <E> List<E> getRecordListResult(String sql, List<String> paramList, Class<E> clazz, PageBean pageBean,
			DBObject db) throws SQLException, Exception {
		List<E> list = new ArrayList<E>();
		String sqlSearch = "select * from (" + sql + ") as PAGE where rownum > ? and rownum <= ?";
		log.info("Db2DBExecutor SQL = {}", sqlSearch);
		PreparedStatement ps = db.getPreparedStatement(sqlSearch);

		int index = 0;
		for (index = 0; index < paramList.size(); index++) {
			ps.setObject(index + 1, paramList.get(index));
		}
		ps.setInt(index + 1, pageBean.getStartRecordNum());
		ps.setInt(index + 2, pageBean.getEndRecordNum());

		ResultSet rs = db.executePstmtQuery();
		while (rs.next()) {
			E bean = clazz.newInstance();
			bean = BeanKit.resultSet2Bean(rs, clazz);
			list.add(bean);
		}
		rs.close();
		return list;
	}

	public String parsePageSql(String sql, int startNum, int endNum) {
		return "select * from (" + sql + ") as PAGE where rownum > " + startNum + " and rownum <= " + endNum;
	}
}
