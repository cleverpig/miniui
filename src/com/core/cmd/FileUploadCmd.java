package com.core.cmd;

import java.sql.SQLException;

import javax.servlet.http.*;

import com.core.db.Db;

public class FileUploadCmd {
	
	private static class FileInfo {
		public FileInfo(String locId, String name) {
			mLocalId = locId;
			mFileName = name;
		}
		public String mFileName;
		public String mLocalId;
		
		public int mComplete;
		public long mServerId;
	}
	
	public static void exec(HttpServletRequest req, HttpServletResponse res) {
		String cmd = req.getParameter("__cmd");
		
		if ("upload".equals(cmd)) {
			upload(req, res);
			return;
		} else if ("fetch_complete".equals(cmd)) {
			fetch(req, res);
			return;
		}
	}

	protected static void upload(HttpServletRequest req, HttpServletResponse res) {
		String localId = req.getParameter("localId");
		String fileName = req.getParameter("fileName");
		FileInfo info = new FileInfo(localId, fileName);
		req.getSession().setAttribute(localId, info);
		
		try {
			java.sql.PreparedStatement ps = Db.getConnection().prepareStatement("insert into file_upload(name) values (?)");
			ps.setString(1, fileName);
			ps.execute();
			java.sql.ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				info.mServerId = rs.getInt(1);
			}
			rs.close();
			saveToDisk(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveToDisk(FileInfo fi) {
		//TODO:
	}
	
	protected static void fetch(HttpServletRequest req, HttpServletResponse res) {
		try {
			String localId = req.getParameter("localId");
			FileInfo fi = (FileInfo) req.getSession().getAttribute(localId);
			res.getWriter().write("{localId:" + localId + ", complete:" + 
					fi.mComplete + ",serverId:" + fi.mServerId+ "}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
