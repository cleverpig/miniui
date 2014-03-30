package com.core.meta.ds;

import java.sql.ResultSet;

public interface ISql {
	
	ResultSet query() throws java.sql.SQLException;
}
