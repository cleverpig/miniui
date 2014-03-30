package com.core.basic;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.*;

import com.core.db.Db;

public class Login {
	
	public static void login(HttpServletRequest req, HttpServletResponse res) {
		//TODO:
		String name = req.getParameter("username");
		if (name == null)
			name = "";
		name = name.trim();
		String pwd = req.getParameter("password");
		if (pwd == null)
			pwd = "";
		boolean hasUser = false, pwdOk = false;
		
		try {
			java.sql.Connection con = Db.getConnection();
			PreparedStatement ps = con.prepareStatement("select id,username,pwd from usr where username = ?");
			ps.setString(1, name);
			java.sql.ResultSet rs = ps.executeQuery();
			
			User u = new User();
			if (rs.next()) {
				u.mId = Integer.parseInt(rs.getString(1));
				u.mUserName = toStr(rs.getString(2));
				u.mPwd = toStr(rs.getString(3));
				hasUser = true;
			}
			rs.close();
			ps.close();
			
			if (pwd.equals(u.mPwd)) {
				req.getSession().setAttribute("__User__", u);
				pwdOk = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (pwdOk)
				res.getWriter().write("{status:'ok', msg:'ok'}");
			else if (hasUser)
				res.getWriter().write("{status:'err', msg:'密码错误'}");
			else
				res.getWriter().write("{status:'err', msg:'用户名不存在'}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String toStr(String s) {
		if (s == null)
			return "";
		return s.trim();
	}
}
