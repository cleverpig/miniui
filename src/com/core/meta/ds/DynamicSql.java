package com.core.meta.ds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.core.basic.Current;
import com.core.basic.User;
import com.core.db.Db;

public class DynamicSql implements ISql {
	protected String mSql;
	private ArrayList<String> mParams = new ArrayList<String>();
	
	/**
	 * @param dynSql
	 * 		Õ¼Î»·û:
	 *   	$(uid)   user id
	 *   	$(p?)    privilige idx, ? is a number from 0.... Example: $(p13)
	 *   	$(P)	 privilige string
	 */
	public DynamicSql(String dynSql) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dynSql.length(); ++i) {
			if (dynSql.charAt(i) != '$') {
				sb.append(dynSql.charAt(i));
				continue;
			}
			if (dynSql.charAt(i+1) != '(')	{
				sb.append(dynSql.charAt(i));
				continue;
			}
			i += 2;
			int idx = dynSql.indexOf(')', i);
			sb.append('?');
			mParams.add(dynSql.substring(i, idx));
			i = idx;
		}
		mSql = sb.toString();
	}
	
	protected String parse(String str, User u) {
		if (str.equals("uid")) {
			return u == null ? "0" : String.valueOf(u.mId);
		}
		if (str.charAt(0) == 'p') {
			int pri = Integer.parseInt(str.substring(1));
			if (u == null || u.mPrivilige == null || u.mPrivilige.length() <= pri)
				return "0";
			return String.valueOf(u.mPrivilige.charAt(pri));
		}
		if (str.equals("P")) {
			if (u == null || u.mPrivilige == null)
				return "";
			return u.mPrivilige;
		}
		
		return "";
	}
	

	@Override
	public ResultSet query() throws SQLException {
		User u = Current.getUser();
		PreparedStatement ps = Db.getConnection().prepareStatement(mSql);
		int i = 0;
		for (String s : mParams) {
			++i;
			String v = parse(s, u);
			ps.setString(i, v);
		}
		return ps.executeQuery();
	}

}
