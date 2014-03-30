package com.core.json;

import java.util.*;

public class JsonMap extends HashMap<String, JsonObject> implements JsonObject {
	
	public void createJson(StringBuilder sb) {
		sb.append("{");
		Set<Map.Entry<String, JsonObject>> items = this.entrySet();
		int i = 0;
		Iterator<Map.Entry<String, JsonObject>> it = items.iterator();
		while (it.hasNext()) {
			if (i != 0)
				sb.append(",");
			Map.Entry<String, JsonObject> item = it.next();
			sb.append(item.getKey()).append(":");
			JsonObject jo = item.getValue();
			if (jo == null)
				sb.append("''");
			else
				jo.createJson(sb);
			++i;
		}
		sb.append("}");
	}
	
}
