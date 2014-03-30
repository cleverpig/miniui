package com.core.basic;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

import javax.servlet.http.*;
import com.core.db.Db;

public class User {
	public int mId;
	public String mUserName;
	public String mPwd;
	public String mPrivilige;	// "0100111001..."
	
	
	public boolean hasPrivilige(int priIdx) {
		if (mPrivilige == null)
			return false;
		if (mPrivilige.length() <= priIdx)
			return false;
		return mPrivilige.charAt(priIdx) == '1';
	}
	
	public static User queryUser(String uid) {
		return null;
	}
	
	public static void updatePri(HttpServletRequest req, HttpServletResponse res) {
		try {
			String pri = req.getParameter("pri");
			pri = pri == null ? "" : pri.trim();
			String uid = req.getParameter("uid");	//user id
			uid = uid == null ? "0" : uid.trim();
			
			String[] pris = pri.split(",");
			int[] ipris = new int[pris.length];
			for (int i = 0; i < ipris.length; ++i) {
				ipris[i] = Integer.parseInt(pris[i]);
			}
			Arrays.sort(ipris);
			
			if (ipris.length > 0) {
				char[] p = new char[ipris[ipris.length - 1] + 1];
				java.util.Arrays.fill(p, '0');
				for (int i = 0; i < ipris.length; ++i) {
					if (ipris[i] >= 0)
						p[ipris[i]] = '1';
				}
				pri = new String(p);
			} else {
				pri = "";
			}
			
			PreparedStatement ps = Db.getConnection().prepareStatement("update usr set pri=? where id=?");
			ps.setString(1, pri);
			ps.setString(2, uid);
			ps.executeUpdate();
			res.getWriter().write("{status:'ok', msg:'保存成功!'}");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				res.getWriter().write("{status:'err', msg:'保存失败!'}");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
