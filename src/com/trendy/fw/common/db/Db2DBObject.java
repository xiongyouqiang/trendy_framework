package com.trendy.fw.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Db2DBObject extends DBObject {
	private static Logger log = LoggerFactory.getLogger(Db2DBObject.class);

	public Db2DBObject(String dbLink) throws SQLException {
		conn = getInitConnection(dbLink);
		stmt = conn.createStatement();
	}

	private Connection getInitConnection(String dbLink) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbLink);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("获取DB2链接时出错：", sqle);
		}
		return conn;
	}

	@Override
	public int executeInsert(String sql) throws SQLException {
		int result = 0;
		try {
			result = stmt.executeUpdate(sql);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("ExecuteQuery error: ", sqle);
			throw sqle;
		}
		return result;
	}

	@Override
	public int executePstmtInsert() throws SQLException {
		int result = 0;
		try {
			result = pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("ExecuteQuery error: ", sqle);
			throw sqle;
		}
		return result;
	}

	@Override
	public String parsePageSql(String sql, int startNum, int endNum) {
		return "select * from (" + sql + ") as PAGE where rownum > " + startNum + " and rownum <= " + endNum;
	}
}
