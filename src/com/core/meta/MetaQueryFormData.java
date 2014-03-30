package com.core.meta;

import java.io.*;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MetaQueryFormData extends MetaData {
	
	public MetaQueryFormData(HttpServletRequest req, Writer res, MetaTable table) {
		super(req, res, table);
	}
	
	public static  MetaQueryFormData createInstance(HttpServletRequest req, Writer res, String tableName) {
		MetaTable md = MetaCache.getInstance().fetch(tableName);
		MetaQueryFormData d = new MetaQueryFormData(req, res, md);
		return d;
	}

	@Override
	protected String createJson(Object param, int flags) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		List<MetaCol> list = mTable.getMetaCols();

		sb.append("{type:'hidden', name:'__cmd', val:'query'}");
		sb.append(",{type:'hidden', name:'__table', val:'" + mTable.getTabName() + "'}");
		for (MetaCol mc : list) {
			if (!mc.mQueryable)
				continue;
			sb.append(",");
			sb.append("{title:").append(escape(mc.mTitle));
			sb.append(",name:").append(escape(mc.mName));
			sb.append(",type:").append(MetaCol.typeToString(mc.mType));
			
			sb.append(",src:");
			if (mc.mSrc == null) sb.append("''");
			else mc.mSrc.generateJson().createJson(sb);
			
			sb.append(",valDesc:");
			if (mc.mValDesc == null) sb.append("''");
			else mc.mValDesc.generateJson().createJson(sb);
			sb.append(",opt:").append(escape(mc.mOpt));
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void exec() {
		try {
			String json = createJson(null, 1);
			mOut.write("var _data=");
			mOut.write(json);
			mOut.write(";\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean buildSql() {
		return false;
	}
}
