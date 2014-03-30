package com.core.basic;

import java.util.*;

public abstract class IPriMgr {
	private static IPriMgr sIns;
	protected List<PriItem> mPriItemList = new ArrayList<PriItem>();
	protected String mJson;
	
	protected IPriMgr() {
		init();
	}
	
	public static class PriItem {
		private static int sId;
		public int mId;
		public int mIdx = -1;
		public String mName;
		public PriItem mParent;
		public boolean mIsLeaf = true;
		public String mTag;
		
		public PriItem(String name, PriItem parent, String tag) {
			mId = ++sId;
			mParent = parent;
			mName = name;
			if (parent != null)
				parent.mIsLeaf = false;
			mTag = tag;
		}
	}
	
	public static IPriMgr getInstance() {
		if (sIns == null) {
			try {
				sIns = (IPriMgr) Class.forName("com.my.MyPriMgr").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sIns;
	}
	
	protected void addPriItem(PriItem pi) {
		mPriItemList.add(pi);
	}
	
	public int getPriviligeVal(String tag) {
		if (tag == null)
			return -1;
		for (PriItem pi : mPriItemList) {
			if (tag.equals(pi.mTag))
				return pi.mIdx;
		}
		return -1;
	}
	
	public boolean hasPrivilige(String tag, User u) {
		if (tag == null || u == null)
			return false;
		return hasPrivilige(tag, u.mPrivilige);
	}
	
	public boolean hasPrivilige(String tag, String pri) {
		int i = getPriviligeVal(tag);
		if (i < 0 || pri == null)
			return false;
		if (pri.length() <= i)
			return false;
		return pri.charAt(i) == '1';
	}
	
	// "01010001" to "1,4,8"
	public String priToVal(String pri) {
		if (pri == null)
			return "";
		pri = pri.trim();
		String val = "";
		for (int i = 0; i < pri.length(); ++i) {
			if (pri.charAt(i) == '1') {
				if (!val.isEmpty()) val += ",";
				val += i;
			}
		}
		return val;
	}
	
	protected abstract void init();
	
	public String createJson() {
		if (mJson == null) {
			StringBuilder json = new StringBuilder();
			json.append("[");
			int i = 0;
			initIdx();
			for (PriItem pi : mPriItemList) {
				if (i != 0) json.append(",");
				json.append("{");
				json.append("id:").append(pi.mId);
				json.append(",pId:").append(pi.mParent == null ? -1: pi.mParent.mId);
				json.append(",name:\"").append(pi.mName).append("\"");
				json.append(",open:true");
				json.append(",idx:").append(pi.mIdx);
				if (!pi.mIsLeaf) {
					json.append(",nocheck:true");
				}
				json.append("}");
				++i;
			}
			json.append("]");
			mJson = json.toString();
		}
		return mJson;
	}
	
	private void initIdx() {
		int i = 0;
		for (PriItem pi : mPriItemList) {
			if (pi.mIsLeaf) {
				pi.mIdx = i++;
			}
		}
	}
}
