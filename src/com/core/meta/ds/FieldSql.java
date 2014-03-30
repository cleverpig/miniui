package com.core.meta.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.core.db.Db;

public class FieldSql extends BasicSql {
	protected String mField;

	public FieldSql(String sql) {
		super(sql);
	}
	
	public void setField(String field) {
		mField = field;
	}
	
	@Override
	public ResultSet query() throws SQLException {
		if (mSql == null || mSql.trim().isEmpty())
			return null;
		String sql = mSql;
		if (mSql.contains("??")) {
			String s = mField == null || mField.trim().equals("") ? "0" : mField;
			sql = mSql.replace("??", s);
			return Db.getConnection().createStatement().executeQuery(sql);
		}
		java.sql.PreparedStatement ps = Db.getConnection().prepareStatement(mSql);
		if (mSql.contains("?")) {
			ps.setString(1, mField);
		}
		return ps.executeQuery();
	}

}
