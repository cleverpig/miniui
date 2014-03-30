package com.core.meta.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.core.db.Db;

public class BasicSql implements ISql {
	protected String mSql;

	public BasicSql(String sql) {
		mSql = sql;
	}

	@Override
	public ResultSet query() throws SQLException {
		if (mSql == null || mSql.trim().isEmpty())
			return null;
		java.sql.PreparedStatement ps = Db.getConnection().prepareStatement(mSql);
		return ps.executeQuery();
	}

}
