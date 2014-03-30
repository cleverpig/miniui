package com.my;

import com.core.basic.Current;
import com.core.basic.IApplication;

public class MyApplication extends IApplication {
	private String mScriptInfo;
	
	@Override
	public String getScriptInfo() {
		if (mScriptInfo == null) {
			String jsPath = Current.getRequest().getContextPath() + "/jscss/";
			mScriptInfo = "<script type='text/javascript' src='" + jsPath + "jquery.js'></script>\n";
			String all = "all.jsp?" + Math.random();
			mScriptInfo += "<script type='text/javascript' src='" + jsPath + all + "'></script>\n";
			String css = "all.css?" + Math.random();
			mScriptInfo += "<link  rel='stylesheet' type='text/css' href='" + jsPath + css + "' ></link>\n";
		}
		return mScriptInfo;
	}

	@Override
	public String getAppName() {
		return "Demo";
	}

	@Override
	public String getFileUploadDir() {
		return "D:\\J2EE\\Workspace";
	}

}
