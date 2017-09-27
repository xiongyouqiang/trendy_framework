package com.trendy.fw.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLiteDBObject extends DBObject {
	private static Logger log = LoggerFactory.getLogger(SQLiteDBObject.class);

	public SQLiteDBObject(String dbLink) throws SQLException {
		conn = getInitConnection(dbLink);
		stmt = conn.createStatement();
	}

	private Connection getInitConnection(String dbLink) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbLink);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("获取SQLite连接时出错：", sqle);
		}
		return conn;
	}

	public int executeInsert(String sql) throws SQLException {
		int result = 0;
		try {
			stmt.executeQuery(sql);

			String sqlId = "select last_insert_rowid() ";
			ResultSet rs = conn.createStatement().executeQuery(sqlId);
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("ExecuteQuery error: ", sqle);
			throw sqle;
		}
		return result;
	}

	public int executePstmtInsert() throws SQLException {
		int result = 0;
		try {
			pstmt.execute();

			String sqlId = "select last_insert_rowid() ";
			ResultSet rs = conn.createStatement().executeQuery(sqlId);
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("ExecuteQuery error: ", sqle);
			throw sqle;
		}
		return result;
	}

	public String parsePageSql(String sql, int startNum, int endNum) {
		return sql + " limit " + startNum + "," + (endNum - startNum);
	}
}
