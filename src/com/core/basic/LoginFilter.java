package com.core.basic;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		
		HttpServletRequest r = (HttpServletRequest) req;
		HttpServletResponse rs = (HttpServletResponse)res;
		
		User u = (User) r.getSession().getAttribute("__User__");
		if (u == null) {
			rs.getWriter().write("<script>top.location='" + r.getContextPath() + "/login.jsp';</script>");
			return;
		}
		fc.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
