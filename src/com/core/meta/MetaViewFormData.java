package com.core.meta;

import java.io.Writer;
import java.sql.*;

import javax.servlet.http.*;
import com.core.db.Db;

public class MetaViewFormData extends MetaData {

	/**
	 * @param req
	 * @param res
	 * @param sql If is null, then create it auto.
	 */
	public MetaViewFormData(HttpServletRequest req, Writer res, MetaTable table, String sql) {
		super(req, res, table);
		mSql = sql;
	}
	
	protected String createJson(Object param, int flags) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			ResultSet rs = (ResultSet) param;
			ResultSetMetaData md = rs.getMetaData();
			
			for (int i = 0, j = 0; i < md.getColumnCount(); ++i) {
				MetaCol mc = mTable.findMetaCol(md.getColumnLabel(i+1));
				if (mc == null)
					continue;
				if (!mc.mViewable) {
					continue;
				}
				if (j != 0)
					sb.append(",");
				String v = rs.getString(i + 1);
				sb.append("{title:").append(escape(mc.mTitle));
				sb.append(",visible:true,cnt:").append(escape(v)).append("}");
				++j;
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
		for (MetaCol mc : mTable.getMetaCols()) {
			if (!mc.mViewable)
				continue;
			sb.append(mc.mName);
			if (sb.length() != 0)
				sb.append(",");
		}
		sb.append(" from ").append(mTable.getTabName());
		sb.append(" where id = ?");
		mSql = sb.toString();
		return true;
	}
	
}
