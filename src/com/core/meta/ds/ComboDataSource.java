package com.core.meta.ds;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.core.json.JsonArray;
import com.core.json.JsonObject;
import com.core.json.JsonString;

public class ComboDataSource extends DataSource {
	private JsonArray mStaticData;
	protected boolean mFirstEmpty;
	
	/**
	 * @param sql  select id,name from xxx
	 */
	public ComboDataSource(ISql sql, boolean firstEmpty) {
		super(sql);
		mFirstEmpty = firstEmpty;
	}
	
	/**
	 * @param staticSrc  id:name;id:name;...
	 * @param firstEmpty
	 */
	public ComboDataSource(String staticSrc, boolean firstEmpty) {
		super(staticSrc);
		mFirstEmpty = firstEmpty;
	}

	@Override
	protected JsonObject generateDynamic() {
		JsonArray arr = new JsonArray();
		
		if (mFirstEmpty) {
			arr.add(new JsonString(""));
			arr.add(new JsonString(""));
		}
		
		try {
			ResultSet rs = mSql.query();
			int c = rs.getMetaData().getColumnCount();
			if (c == 2) {
				while (rs.next()) {
					arr.add(new JsonString(rs.getString(1)));
					arr.add(new JsonString(rs.getString(2)));
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	@Override
	protected JsonObject generateStatic() {
		if (mStaticData != null) {
			return mStaticData;
		}
		mStaticData = new JsonArray();
		if (mFirstEmpty) {
			mStaticData.add(new JsonString(""));
			mStaticData.add(new JsonString(""));
		}
		String[] items = mStaticSrc.split(";");
		for (String s: items) {
			if (s.trim().isEmpty())
				continue;
			String[] kv = s.split(":");
			if (kv == null || kv.length != 2)
				continue;
			for (String k : kv)
				mStaticData.add(new JsonString(k.trim()));
		}
		return mStaticData;
	}

}
