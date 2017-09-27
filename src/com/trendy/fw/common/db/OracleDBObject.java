package com.trendy.fw.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleDBObject extends DBObject {
	private static Logger log = LoggerFactory.getLogger(DBObject.class);

	public OracleDBObject(String dbLink) throws SQLException {
		conn = getInitConnection(dbLink);
		stmt = conn.createStatement();
	}

	private Connection getInitConnection(String dbLink) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbLink);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("获取oracle连接时出错：", sqle);
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
		return "select * from (select PAGE.*, rownum RN from (" + sql + ") PAGE where RN < " + endNum + ") where RN > "
				+ startNum;
	}

}
