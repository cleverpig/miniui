package com.my;

import com.core.meta.*;
import com.core.meta.ds.*;

public class MyMetaCache extends MetaCache {

	@Override
	protected void init() {
		//TODO:
		//------usr----------------------------------------
		MetaTable mcl = new MetaTable("usr", true);
		MetaCol mc = new MetaCol("id", "ID", false, true, false, false, MetaCol.TYPE.TEXT, null, null, MetaCol.DbType.NUMBER);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("username", "�û���", true, true, true, true, MetaCol.TYPE.TEXT, "must|len(5,20)", "varchar(20)", MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("nickname", "����", true, true, true, true, MetaCol.TYPE.TEXT, "must|len(5,20)", "varchar(20)", MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("pwd", "����", false, false, false, false, MetaCol.TYPE.TEXT, "", "varchar(20)", MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("sex", "�Ա�", true, true, true, true, MetaCol.TYPE.COMBOBOX, "", "char(4)", MetaCol.DbType.STRING);
		mc.mSrc = new ComboDataSource("��:��;Ů:Ů", true);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("old", "����", false, true, true, true, MetaCol.TYPE.TEXT, "", "integer", MetaCol.DbType.NUMBER);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("birthday", "����", true, true, true, true, MetaCol.TYPE.DATE, "must", "char(12)", MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("idcard", "���֤", true, true, true, true, MetaCol.TYPE.TEXT, "must|len(17)", "char(17)", MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("pic", "��Ƭ", false, false, true, true, MetaCol.TYPE.FILE_UPLOAD, "", "varchar(30)", MetaCol.DbType.STRING);
		mc.mSrc = new StringDataSource("$(root)/core/file_upload_action.jsp");
		mc.mValDesc = new ResultSetDataSource(new FieldSql("select id serverId, name from file_upload where id in (??)"));
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("mark", "��ע", false, true, true, true, MetaCol.TYPE.TEXT_AREA, "", "varchar(255)", MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("pst", "����", false, true, true, true, MetaCol.TYPE.URL_DIALOG, "", "varchar(255)", MetaCol.DbType.STRING);
		mc.mSrc = new StringDataSource("$(root)/my/OL.jsp");
		mc.mOpt = "{width:500, height:300}";
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("pri", "Ȩ��", false, false, false, true, MetaCol.TYPE.URL_DIALOG, "", "varchar(255)", MetaCol.DbType.STRING);
		mc.mSrc = new StringDataSource("$(root)/core/pri_mgr.jsp?uid=$(uid)&indialog=true");
		mc.mOpt = "{width:500, height:400}";
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("id __op", "__op", false, true, false, false, MetaCol.TYPE.TEXT, "", null, MetaCol.DbType.STRING);
		mc.mIsDbCol = false;
		mc.mQueryName = "__op";
		mcl.addMetaCol(mc);
		add(mcl);
		
		//------file upload----------------------------------------
		mcl = new MetaTable("file_upload", true);
		mc = new MetaCol("id", "ID", false, true, false, false, MetaCol.TYPE.TEXT, null, null, MetaCol.DbType.NUMBER);
		mcl.addMetaCol(mc);
		
		mc = new MetaCol("name", "�ļ���", false, true, false, false, MetaCol.TYPE.TEXT, null, null, MetaCol.DbType.STRING);
		mcl.addMetaCol(mc);
		add(mcl);
	}
	
}
