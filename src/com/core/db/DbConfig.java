package com.core.db;

public interface DbConfig {
	String getDriver();
	String getUrl();
	String getUserName();
	String getPassword();
	int getMaxConnectCount();
}
