package com.core.meta;

import com.core.json.JsonObject;
import com.core.meta.ds.DataSource;

public class MetaCol implements Cloneable {

	public static enum TYPE {
		HIDDEN,
		PASSWORD,
		TEXT,
		TEXT_AREA,
		DATE,
		FILE_UPLOAD,
		COMBOBOX,
		GROUP,
		URL_DIALOG
	}
	
	public static enum DbType {
		STRING,
		NUMBER
	}
	
	public String mName;
	public String mTitle;
	public boolean mQueryable;
	public boolean mResultable;
	public boolean mViewable;
	public boolean mInputable;
	public TYPE mType;
	public String mRule;
	public DbType mDbType;
	
	public String mQueryName;
	
	public DataSource mSrc;
	public DataSource mValDesc;
	public String mCreateSql;
	public String mOpt;
	
	public boolean mIsDbCol = true;
	
	public MetaCol(String name, String title, boolean queryable, boolean resultable,
			boolean viewable, boolean inputable, TYPE t, String rule, String sql, DbType dbType) {
		mName = name;
		mTitle = title;
		mQueryable = queryable;
		mResultable = resultable;
		mViewable = viewable;
		mInputable = inputable;
		mType = t;
		mRule = rule;
		mCreateSql = sql;
		mDbType = dbType;
	}
	
	public String getQueryName() {
		if (mQueryName != null)
			return mQueryName;
		return mName;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		MetaCol mc = (MetaCol) obj;
		return mc.getQueryName().equals(this.getQueryName());
	}
	
	public MetaCol clone() {
		try {
			return (MetaCol) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String typeToString(MetaCol.TYPE t) {
		switch (t) {
		case HIDDEN:
			return "'hidden'";
		case PASSWORD:
			return "'password'";
		case TEXT:
			return "'text'";
		case TEXT_AREA:
			return "'textarea'";
		case DATE:
			return "'optional-date'";
		case FILE_UPLOAD:
			return "'optional-file'";
		case COMBOBOX:
			return "'combobox'";
		case GROUP:
			return "'optional-group'";
		case URL_DIALOG:
			return "'optional-url'";
		}
		return "--";
	}
}
