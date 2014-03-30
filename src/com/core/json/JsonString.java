package com.core.json;

public class JsonString implements JsonObject {
	private String mStr;
	
	public JsonString(String str) {
		mStr = str;
	}

	public void createJson(StringBuilder sb) {
		String s = mStr;
		if (s == null)
			s = "";
		s = s.replace("'", "\'");
		sb.append("'").append(s).append("'");
	}
	
	public static String escape(String str) {
		if (str == null)
			return "''";
		str = str.replace("'", "\'");
		return "'" + str + "'";
	}

}
