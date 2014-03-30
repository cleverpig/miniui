package com.core.cmd;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import com.core.basic.Login;
import com.core.db.Db;
import com.core.json.JsonString;
import com.core.meta.*;

public class DbCmd {
	protected HttpServletRequest mReq;
	protected HttpServletResponse mRes;
	protected String mQueryBaseSql;
	protected String mQueryWhereSql;
	
	public DbCmd(HttpServletRequest req, HttpServletResponse res) {
		mReq = req;
		mRes = res;
	}
	
	/**
	 * @param sql "select ... from xx"
	 * @param sqlWhere "where 1=1"
	 */
	public void setQuerySql(String baseSql, String sqlWhere) {
		mQueryBaseSql = baseSql;
		mQueryWhereSql = sqlWhere;
	}
	
	public void exec() {
		String cmd = mReq.getParameter("__cmd");
		
		if ("login".equals(cmd)) {
			Login.login(mReq, mRes);
			return;
		}

		String table = mReq.getParameter("__table");
		if (table == null || "".equals(table))
			return;
		
		if ("query".equals(cmd)) {
			query(table);
		} else if ("add".equals(cmd)) {
			add(table);
		} else if ("update".equals(cmd)) {
			update(table);
		} else if ("delete".equals(cmd)) {
			delete(table);
		}
	}

	protected ArrayList<MetaCol> getExitsMetaCols(String name) {
		MetaTable tab = MetaCache.getInstance().fetch(name);
		ArrayList<MetaCol> list = new ArrayList<MetaCol>();
		Enumeration<String> keys = mReq.getParameterNames();
		
		while (keys.hasMoreElements()) {
			String k = keys.nextElement();
			String v = mReq.getParameter(k);
			if (v == null || v.trim().length() == 0)
				continue;
			MetaCol mc  = tab.findMetaCol(k);
			if (mc != null)
				list.add(mc);
		}
		return list;
	}
	
	protected ArrayList<MetaCol> getReqMetaCols(String name) {
		MetaTable tab = MetaCache.getInstance().fetch(name);
		ArrayList<MetaCol> list = new ArrayList<MetaCol>();
		Enumeration<String> keys = mReq.getParameterNames();
		
		while (keys.hasMoreElements()) {
			String k = keys.nextElement();
			String v = mReq.getParameter(k);
			MetaCol mc  = tab.findMetaCol(k);
			if (mc == null)
				continue;
			if (mc.mDbType == MetaCol.DbType.NUMBER) {
				if (v != null && v.trim().length() != 0)
					list.add(mc);
			} else {
				list.add(mc);
			}
		}
		return list;
	}
	
	protected String getSqlColumns(ArrayList<MetaCol> list) {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (MetaCol mc : list) {
			//if (!mc.mIsDbCol)
			//	continue;
			if (i != 0)
				sb.append(",");
			sb.append(mc.mName);
			++i;
		}
		return sb.toString();
	}
	
	protected ArrayList<MetaCol> getQueryableMetaCols(String name) {
		MetaTable tab = MetaCache.getInstance().fetch(name);
		ArrayList<MetaCol> list = new ArrayList<MetaCol>();
		
		for (MetaCol mc : tab.getMetaCols()) {
			if (mc.mResultable) {
				list.add(mc);
			}
		}
		return list;
	}
	
	protected void query(String name) {
		ArrayList<MetaCol> list = getExitsMetaCols(name);
		ArrayList<String> strs = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbwhere = new StringBuilder();
		
		if (mQueryBaseSql != null && mQueryBaseSql.trim().length() != 0) {
			sb.append(mQueryBaseSql);
		} else {
			sb.append("select ").append(getSqlColumns(getQueryableMetaCols(name))).append(" from ");
			sb.append(name);
		}
		if (mQueryWhereSql == null || mQueryWhereSql.trim().length() == 0)
			sbwhere.append(" where 1=1 ");
		else
			sbwhere.append(" ").append(mQueryWhereSql).append(" ");
		
		for (MetaCol mc : list)	{
			String v = mReq.getParameter(mc.getQueryName());
			if (v != null && v.trim().length() != 0) {
				sbwhere.append(" and ").append(mc.mName);
				if (mc.mType == MetaCol.TYPE.COMBOBOX)
					sbwhere.append(" = ?");
				else
					sbwhere.append(" like CONCAT('%', ?, '%')");
				strs.add(v.trim());
			}
		}
		
		try {
			String sqlCount = "select count(*) from " + name + sbwhere;
			PreparedStatement pc = Db.getConnection().prepareStatement(sqlCount);
			for (int i = 0; i < strs.size(); ++i) {
				pc.setString(i+1, strs.get(i));
			}
			ResultSet rc = pc.executeQuery();
			rc.next();
			int count = rc.getInt(1);
			rc.close();
			pc.close();
			String _s = mReq.getParameter("__cur");
			int curPage = (_s == null || _s.trim().length() == 0) ? 0 : Integer.parseInt(_s);
			_s = mReq.getParameter("__per");
			int perPage = (_s == null || _s.trim().length() == 0) ? 10 : Integer.parseInt(_s);
			int totalPage = (count + perPage - 1) / perPage;
			
			sb.append(sbwhere);
			sb.append(" limit ").append(curPage * perPage).append(",").append(perPage);
			PreparedStatement ps = Db.getConnection().prepareStatement(sb.toString());
			//System.out.print("Query sql:" + sb + "\n");
			for (int i = 0; i < strs.size(); ++i) {
				ps.setString(i+1, strs.get(i));
			}
			ResultSet rs = ps.executeQuery();
			String json = buildResultJson(name, rs, curPage, totalPage, perPage);
			rs.close();
			ps.close();
			mRes.getWriter().write(json);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected String buildResultJson(String name, ResultSet rs, int curPage, int maxPage, int perPage) {
		StringBuilder sb = new StringBuilder();
		MetaTable tab = MetaCache.getInstance().fetch(name);
		sb.append("{title:[");
		try {
			ResultSetMetaData md = rs.getMetaData();
			for (int i = 0; i < md.getColumnCount(); ++i) {
				MetaCol mc = tab.findMetaCol(md.getColumnLabel(i+1));
				if (i != 0) sb.append(",");
				sb.append(JsonString.escape(mc.mTitle));
			}
			sb.append("],maxPage:").append(maxPage).append(",curPage:");
			sb.append(curPage).append(",perPage:").append(perPage).append(",content:[");
			int cc = 0;
			while (rs.next()) {
				if (cc != 0) sb.append(",");
				sb.append("[");
				for (int i = 0; i < md.getColumnCount(); ++i) {
					if (i != 0) sb.append(",");
					sb.append(JsonString.escape(rs.getString(i+1)));
				}
				sb.append("]");
				++cc;
			}
			sb.append("]}");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}
	
	protected void add(String name) {
		int count = 0;
		try {
			ArrayList<MetaCol> list = getExitsMetaCols(name);
			StringBuilder sb = new StringBuilder();
			ArrayList<String> strs = new ArrayList<String>();
			
			sb.append("insert into ").append(name);
			sb.append(" (").append(getSqlColumns(list)).append(") ");
			sb.append("values (");
			
			int i = 0;
			for (MetaCol mc : list)	{
				String v = mReq.getParameter(mc.getQueryName());
				if (i != 0) sb.append(",");
				sb.append("?");
				strs.add(v.trim());
				++i;
			}
			sb.append(")");
			java.sql.PreparedStatement ps = Db.getConnection().prepareStatement(sb.toString());
			for (i = 0; i < strs.size(); ++i) {
				ps.setString(i+1, strs.get(i));
			}
			count = ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			if (count == 1)
				mRes.getWriter().write("{status:'ok', msg:'保存成功!'}");
			else
				mRes.getWriter().write("{status:'error', msg:'保存失败!'}");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected void update(String name) {
		String id = mReq.getParameter("id");
		if (id == null || id.trim().length() == 0)
			return;
		int count = 0;
		try {
			ArrayList<MetaCol> list = getReqMetaCols(name);
			StringBuilder sb = new StringBuilder();
			ArrayList<String> strs = new ArrayList<String>();
			
			sb.append("update ").append(name);
			sb.append(" set ");
			
			int i = 0;
			for (MetaCol mc : list)	{
				String v = mReq.getParameter(mc.getQueryName());
				if (i != 0) sb.append(",");
				sb.append(mc.mName).append(" = ? ");
				strs.add(v == null ? "" : v.trim());
				++i;
			}
			sb.append(" where id=?");
			strs.add(id);
			
			java.sql.PreparedStatement ps = Db.getConnection().prepareStatement(sb.toString());
			for (i = 0; i < strs.size(); ++i) {
				ps.setString(i+1, strs.get(i));
			}
			count = ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			if (count == 1)
				mRes.getWriter().write("{status:'ok', msg:'保存成功!'}");
			else
				mRes.getWriter().write("{status:'error', msg:'保存失败!'}");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected void delete(String name) {
		int count = 0;
		try {
			String id = mReq.getParameter("id");
			StringBuilder sb = new StringBuilder();
			sb.append("delete from ").append(name).append(" where id in (?)");
			
			java.sql.PreparedStatement ps = Db.getConnection().prepareStatement(sb.toString());
			ps.setString(1, id);
			count = ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			if (count > 0)
				mRes.getWriter().write("{status:'ok', msg:'删除成功!'}");
			else
				mRes.getWriter().write("{status:'error', msg:'删除失败!'}");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
