package com.my;

import com.core.basic.IPriMgr;

public class MyPriMgr extends IPriMgr {

	@Override
	protected void init() {
		PriItem p1 = new PriItem("用户管理", null, "A");
		addPriItem(p1);
		addPriItem(new PriItem("查询", p1, "A1"));
		addPriItem(new PriItem("修改", p1, "A2"));
		
		PriItem p2 = new PriItem("部门管理", null, "B");
		addPriItem(p2);
		addPriItem(new PriItem("查询", p2, "B1"));
		addPriItem(new PriItem("修改", p2, "B2"));
	}
	
}
