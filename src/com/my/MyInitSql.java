package com.my;

import com.core.db.InitSql;

public class MyInitSql implements InitSql {

	@Override
	public String[] getInitSql() {
		return new String[] {
				"insert into usr(username,pwd,pri) values('admin', 'admin', '1111111111')"
		};
	}

}
