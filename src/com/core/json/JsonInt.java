package com.core.json;

public class JsonInt implements JsonObject {
	private int mInt;
	
	public JsonInt(int val) {
		mInt = val;
	}
	
	public void createJson(StringBuilder sb) {
		sb.append(mInt);
	}
}
