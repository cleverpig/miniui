package com.my;

import com.core.db.DbConfig;

public class MyDbConfig implements DbConfig {

	@Override
	public String getDriver() {
		return "org.gjt.mm.mysql.Driver";
	}

	@Override
	public String getUrl() {
		return "jdbc:mysql://localhost/test";
	}

	@Override
	public String getUserName() {
		return "root";
	}

	@Override
	public String getPassword() {
		return "root";
	}

	@Override
	public int getMaxConnectCount() {
		return 30;
	}

}
