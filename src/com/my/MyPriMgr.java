package com.my;

import com.core.basic.IPriMgr;

public class MyPriMgr extends IPriMgr {

	@Override
	protected void init() {
		PriItem p1 = new PriItem("�û�����", null, "A");
		addPriItem(p1);
		addPriItem(new PriItem("��ѯ", p1, "A1"));
		addPriItem(new PriItem("�޸�", p1, "A2"));
		
		PriItem p2 = new PriItem("���Ź���", null, "B");
		addPriItem(p2);
		addPriItem(new PriItem("��ѯ", p2, "B1"));
		addPriItem(new PriItem("�޸�", p2, "B2"));
	}
	
}
