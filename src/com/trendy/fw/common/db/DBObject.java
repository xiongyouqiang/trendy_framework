package com.trendy.fw.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DBObject {
	private static Logger log = LoggerFactory.getLogger(DBObject.class);

	protected Connection conn = null;
	protected Statement stmt = null;
	protected PreparedStatement pstmt = null;

	public Connection getConnection() throws SQLException {
		return conn;
	}

	public Statement getStatement() throws SQLException {
		return conn.createStatement();
	}

	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		pstmt = conn.prepareStatement(sql);
		return pstmt;
	}

	public void beginTransition() throws SQLException {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("Begin transition error: ", sqle);
			throw sqle;
		}
	}

	public void commit() throws SQLException {
		try {
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("Commit error: ", sqle);
			throw sqle;
		}
	}

	public void rollback() throws SQLException {
		try {
			conn.rollback();
			conn.setAutoCommit(true);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			log.error("Rollback error: ", sqle);
			throw sqle;
		}
	}

	public void close() throws SQLException {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException sqle) {
			log.error("Close error: ", sqle);
			throw sqle;
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException sqle) {
			log.error("ExecuteQuery error: ", sqle);
			throw sqle;
		}
	}

	public int executeUpdate(String sql) throws SQLException {
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException sqle) {
			log.error("ExecuteUpdate error: ", sqle);
			throw sqle;
		}
	}

	public abstract int executeInsert(String sql) throws SQLException;

	public ResultSet executePstmtQuery() throws SQLException {
		try {
			return pstmt.executeQuery();
		} catch (SQLException sqle) {
			log.error("ExecutePreparedUpdate error: ", sqle);
			throw sqle;
		}
	}

	public int executePstmtUpdate() throws SQLException {
		try {
			return pstmt.executeUpdate();
		} catch (SQLException sqle) {
			log.error("ExecutePreparedUpdate error: ", sqle);
			throw sqle;
		}
	}

	public abstract int executePstmtInsert() throws SQLException;

	@SuppressWarnings("deprecation")
	public abstract String parsePageSql(String sql, int startNum, int endNum);
}
