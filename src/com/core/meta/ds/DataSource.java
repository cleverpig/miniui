package com.core.meta.ds;

import com.core.json.JsonObject;

public abstract class DataSource {
	protected ISql mSql;
	protected String mStaticSrc;
	
	public DataSource(ISql sql) {
		mSql = sql;
	}
	
	public DataSource(String staticSrc) {
		mStaticSrc = staticSrc;
	}
	
	public ISql getISql() {
		return mSql;
	}
	
	public JsonObject generateJson() {
		if (mSql != null)
			return generateDynamic();
		return generateStatic();
	}
	
	protected abstract JsonObject generateDynamic();
	
	protected abstract JsonObject generateStatic();
}
