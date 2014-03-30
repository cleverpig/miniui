package com.core.meta;

import java.io.Writer;
import java.util.*;

import javax.servlet.http.*;

public abstract class MetaData {
	protected MetaTable mTable;
	protected String mSql;
	protected HttpServletRequest mReq;
	protected Writer mOut;
	
	public MetaData(HttpServletRequest req, Writer res, MetaTable table) {
		mReq = req;
		mOut = res;
		mTable = table;
	}
	
	protected abstract String createJson(Object param, int flags);
	
	public abstract void exec();
	protected abstract boolean buildSql();
	
	public void setSql(String sql) {
		mSql = sql;
	}
	
	public String escape(String val) {
		if (val == null)
			return "''";
		return "'" + val.replace("'", "\'") + "'";
	}
	
	public String escape(int val) {
		return String.valueOf(val);
	}
}
