package com.core.db;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;

import com.core.meta.MetaCache;
import com.core.meta.MetaCol;
import com.core.meta.MetaTable;

public class Db {
	private static ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	private static boolean bChecked;
	private static boolean needInitTable = true;
	
	public static Connection getConnection() {
		Connection obj = local.get();
		if (obj != null) {
			return obj;
		}
		obj = DbPool.getInstance().fetch();
		local.set(obj);
		try {
			check(obj);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	private static void check(Connection con) throws SQLException {
		if (bChecked)
			return;
		bChecked = true;
		
		MetaCache mc = MetaCache.getInstance();
		Set<String> names = mc.getRealTableNames();
		PreparedStatement ps = con.prepareStatement("show tables");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String t = rs.getString(1);
			if (names.contains(t)) {
				names.remove(t);
			}
			needInitTable = false;
		}
		rs.close();
		ps.close();
		
		for (String s : names) {
			createTable(con, s);
		}
		if (needInitTable) {
			initTable(con);
		}
	}
	
	private static void initTable(Connection con) {
		try {
			InitSql is = (InitSql) Class.forName("com.my.MyInitSql").newInstance();
			String[] sql = is.getInitSql();
			Statement stm = con.createStatement();
			for (String ss : sql)
				stm.addBatch(ss);
			stm.executeBatch();
			stm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createTable(Connection con, String name) throws SQLException {
		MetaTable mt = MetaCache.getInstance().fetch(name);
		StringBuilder sb = new StringBuilder();
		sb.append("create table ").append(mt.getTabName());
		sb.append(" (id integer primary key auto_increment");
		List<MetaCol> list = mt.getMetaCols();
		for (MetaCol mc : list) {
			if (mc.mName.toUpperCase().equals("ID") || !mc.mIsDbCol)
				continue;
			sb.append(",").append(mc.mName).append(" ").append(mc.mCreateSql);
		}
		sb.append(")");
		PreparedStatement ps = con.prepareStatement(sb.toString());
		ps.executeUpdate();
		ps.close();
	}

	public static void releaseConnection() {
		Connection obj = local.get();
		try {
			if (obj != null) {
				local.remove();
				DbPool.getInstance().release(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
