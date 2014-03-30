package com.core.db;

import java.sql.*;
import java.util.*;

public class DbPool {
	private static DbPool sIns = new DbPool();
	private static DbConfig config;
	private List<Connection> mUsingPool = new LinkedList<Connection>();
	private List<Connection> mIdlePool = new LinkedList<Connection>();
	private Object mLock = new Object();
	
	static {
		try {
			config = (DbConfig) Class.forName("com.my.MyDbConfig").newInstance();
			Class.forName(config.getDriver()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DbPool getInstance() {
		return sIns;
	}
	
	private Connection newConnection() {
		try {
			String url = config.getUrl();
			String user = config.getUserName();
			String password = config.getPassword();
			Connection con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Connection fetch() {
		synchronized(mLock) {
			if (mIdlePool.isEmpty() && mUsingPool.size() < config.getMaxConnectCount()) {
				Connection con = newConnection();
				if (con != null)
					mIdlePool.add(con);
			}
			if (mIdlePool.isEmpty()) {
				try {
					mLock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Connection con = mIdlePool.remove(0);
			if (con != null)
				mUsingPool.add(con);
			return con;
		}
	}
	
	public void release(Connection con) {
		if (con == null)
			return;
		synchronized(mLock) {
			mUsingPool.remove(con);
			if (!mIdlePool.contains(con))
				mIdlePool.add(con);
		}
		mLock.notify();
	}
}
