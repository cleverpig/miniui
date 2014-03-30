package com.core.meta.ds;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.core.json.JsonArray;
import com.core.json.JsonMap;
import com.core.json.JsonObject;
import com.core.json.JsonString;

public class ResultSetDataSource extends DataSource {

	public ResultSetDataSource(ISql sql) {
		super(sql);
	}

	@Override
	protected JsonObject generateDynamic() {
		JsonArray arr = new JsonArray();
		try {
			ResultSet rs = mSql.query();
			int c = rs.getMetaData().getColumnCount();
			ResultSetMetaData md = rs.getMetaData();
			while (rs.next()) {
				JsonMap row = new JsonMap();
				arr.add(row);
				for (int i = 0; i < c; ++i)
					row.put(md.getColumnLabel(i+1), new JsonString(rs.getString(i+1)));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	@Override
	protected JsonObject generateStatic() {
		return null;
	}

}
