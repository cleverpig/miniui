package com.core.json;

import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonObject> implements JsonObject {

	public void createJson(StringBuilder sb) {
		sb.append("[");
		for (int i = 0; i < this.size(); ++i) {
			if (i != 0)
				sb.append(",");
			JsonObject jo = this.get(i);
			jo.createJson(sb);
		}
		sb.append("]");
	}
	
	public void add(JsonObject[] arr) {
		if (arr == null)
			return;
		for (JsonObject jo : arr)
			add(jo);
	}
	
}
