package com.core.meta;

import java.util.*;

public abstract class MetaCache {
	protected Map<String, MetaTable> mMetaTables = new HashMap<String, MetaTable>();
	private static MetaCache ins; 

	public static MetaCache getInstance() {
		if (ins == null) {
			try {
				ins = (MetaCache) Class.forName("com.my.MyMetaCache").newInstance();
				ins.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ins;
	}
	
	public MetaTable fetch(String tabName) {
		try {
			return mMetaTables.get(tabName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public Set<String> getTableNames() {
		return new HashSet<String>(mMetaTables.keySet());
	}
	
	public Set<String> getRealTableNames() {
		Object[] tabs = getTableNames().toArray();
		Set<String> ret = new HashSet<String>();
		for (Object obj : tabs) {
			MetaTable mt = fetch((String)obj);
			if (mt.isReal())
				ret.add((String)obj);
		}
		return ret;
	}
	
	protected MetaCache() {
	}
	
	protected void add(MetaTable mcl) {
		mMetaTables.put(mcl.getTabName(), mcl);
	}
	
	protected abstract void init();
}
