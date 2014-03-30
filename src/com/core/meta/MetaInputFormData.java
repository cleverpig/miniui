package com.core.meta;

import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.http.*;

import com.core.db.Db;
import com.core.meta.ds.FieldSql;

public class MetaInputFormData extends MetaData {
	
	public MetaInputFormData(HttpServletRequest req, Writer res, MetaTable table) {
		super(req, res, table);
	}
	
	public static  MetaInputFormData createInstance(HttpServletRequest req, Writer res, String tableName) {
		MetaTable md = MetaCache.getInstance().fetch(tableName);
		MetaInputFormData d = new MetaInputFormData(req, res, md);
		return d;
	}
	
	protected String createJson(Object param, int flags) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			ResultSet rs = (ResultSet) param;
			ResultSetMetaData md = rs.getMetaData();
			boolean hasData = rs.next();
			
			String id = mReq.getParameter("id");
			if (id == null) id = "";
			String cmd = (hasData || !id.equals("")) ? "update" : "add";
			sb.append("{type:'hidden', name:'__cmd', val:'" + cmd + "'}");
			sb.append(",{type:'hidden', name:'__table', val:'" + mTable.getTabName() + "'}");
			if (id.trim().length() != 0)
				sb.append(",{type:'hidden', name:'id', val:'").append(id.trim()).append("'}");
			
			for (int i = 0; i < md.getColumnCount(); ++i) {
				MetaCol mc = mTable.findMetaCol(md.getColumnLabel(i+1));
				if (mc == null)
					continue;
				if (!mc.mInputable) {
					continue;
				}
				sb.append(",");
				String v = hasData ? rs.getString(i + 1) : "";
				sb.append("{title:").append(escape(mc.mTitle));
				sb.append(",name:").append(escape(mc.mName));
				sb.append(",type:").append(MetaCol.typeToString(mc.mType));
				
				sb.append(",src:");
				if (mc.mSrc == null) sb.append("''");
				else mc.mSrc.generateJson().createJson(sb);
				
				sb.append(",val:").append(escape(v));
				
				sb.append(",valDesc:");
				if (mc.mValDesc == null) sb.append("''");
				else {
					if ( mc.mValDesc.getISql() instanceof FieldSql) {
						FieldSql fs = (FieldSql) mc.mValDesc.getISql();
						fs.setField(v);
					}
					mc.mValDesc.generateJson().createJson(sb);
				}
				
				sb.append(",rule:").append(escape(mc.mRule));
				sb.append(",opt:").append(escape(mc.mOpt));
				sb.append("}");
			}
			sb.append("]");
			return sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public void exec() {
		try {
			Connection con = Db.getConnection();
			boolean b = buildSql();
			//System.out.print("Input sql:" + mSql);
			PreparedStatement ps = con.prepareStatement(mSql);
			String id = mReq.getParameter("id");
			ps.setString(1, id == null ? "0" : id);
			ResultSet rs = ps.executeQuery();
			String json = createJson(rs, b ? 1 : 0);
			rs.close();
			ps.close();
			mOut.write("var _data=");
			mOut.write(json);
			mOut.write(";\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean buildSql() {
		if (mSql != null && !mSql.equals("")) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		int i = 0;
		for (MetaCol mc : mTable.getMetaCols()) {
			if (!mc.mInputable)
				continue;
			if (i != 0)
				sb.append(",");
			sb.append(mc.mName);
			++i;
		}
		sb.append(" from ").append(mTable.getTabName());
		sb.append(" where id = ?");
		mSql = sb.toString();
		return true;
	}
}
