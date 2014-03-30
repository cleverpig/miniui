package com.core.meta.ds;

import com.core.basic.Current;
import com.core.basic.User;
import com.core.json.JsonObject;
import com.core.json.JsonString;

public class StringDataSource extends DataSource {
	/**
	 * @param str 可以包含占位符
	 * 		 $(uid)   user id
	 * 		 $(uname)  user name
	 *		 $(root)	current project root path  Eg: /Web
	 */
	public StringDataSource(String str) {
		super(str);
	}
	
	@Override
	protected JsonObject generateDynamic() {
		return null;
	}

	@Override
	protected JsonObject generateStatic() {
		return new JsonString(parse());
	}
	
	protected String parse() {
		if (mStaticSrc == null)
			return null;
		User u = Current.getUser();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < mStaticSrc.length(); ++i) {
			if (mStaticSrc.charAt(i) != '$') {
				sb.append(mStaticSrc.charAt(i));
				continue;
			}
			if (mStaticSrc.charAt(i+1) != '(')	{
				sb.append(mStaticSrc.charAt(i));
				continue;
			}
			i += 2;
			int idx = mStaticSrc.indexOf(')', i);
			String s = mStaticSrc.substring(i, idx);
			i = idx;
			replace(u, sb, s);
		}
		return sb.toString();
	}
	
	protected void replace(User u, StringBuilder sb, String s) {
		if (s.equals("uid"))
			sb.append(u.mId);
		else if (s.equals("uname"))
			sb.append(u.mUserName);
		else if (s.equals("root"))
			sb.append(Current.getRequest().getContextPath());
	}
}
