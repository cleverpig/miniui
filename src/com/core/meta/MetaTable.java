package com.core.meta;

import java.util.*;

public class MetaTable implements Cloneable {
	private ArrayList<MetaCol> mList = new ArrayList<MetaCol>();
	private HashMap<String, MetaCol> mQueryMap = new HashMap<String, MetaCol>();
	private String mTabName;
	private boolean mReal;
	
	public MetaTable(String tabName, boolean real) {
		mTabName = tabName;
		mReal = real;
	}
	
	public void addMetaCol(MetaCol mc) {
		mList.add(mc);
		mQueryMap.put(mc.getQueryName(), mc);
	}
	
	public MetaCol findMetaCol(String name) {
		if (mQueryMap.containsKey(name))
			return mQueryMap.get(name);
		for (MetaCol mc : mList) {
			if (mc.getQueryName().equals(name))
				return mc;
		}
		return null;
	}
	
	public List<MetaCol> getMetaCols() {
		return mList;
	}
	
	public String getTabName() {
		return mTabName;
	}
	
	public boolean isReal() {
		return mReal;
	}
	
	public MetaTable clone() {
		try {
			MetaTable v = (MetaTable) super.clone();
			v.mList = (ArrayList<MetaCol>) mList.clone();
			return v;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
