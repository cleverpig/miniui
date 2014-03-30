package com.core.basic;

public abstract class IApplication {
	private static IApplication sIns;
	protected IApplication() {}
	
	public static IApplication getInstance() {
		if (sIns == null) {
			try {
				sIns = (IApplication) Class.forName("com.my.MyApplication").newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sIns;
	}
	
	public abstract String getScriptInfo();
	
	public abstract String getAppName();
	
	//where upload file save
	public abstract String getFileUploadDir();
}
