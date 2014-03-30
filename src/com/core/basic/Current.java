package com.core.basic;

import javax.servlet.http.HttpServletRequest;

public class Current {
	private static ThreadLocal<HttpServletRequest> sReqTL = new ThreadLocal<HttpServletRequest>();
	
	public static User getUser() {
		return (User)sReqTL.get().getSession().getAttribute("__User__");
	}
	
	public static HttpServletRequest getRequest() {
		return sReqTL.get();
	}
	
	public static void save(HttpServletRequest req) {
		sReqTL.set(req);
	}
}
