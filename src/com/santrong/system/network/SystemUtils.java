package com.santrong.system.network;

import java.util.Properties;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 上午9:34:11
 */
public class SystemUtils {
	public static final int WINDOWS = 0;
	public static final int LINUX = 100;
	public static final int UNIX = 200;
	public static final int OTHER = 300;
	
	public static int getOsType() {
		Properties pro = System.getProperties();
		String osStr = pro.getProperty("os.name");
		osStr = osStr.toLowerCase();
		if(osStr.indexOf("windows") != -1) {
			return WINDOWS;
		}
		if(osStr.indexOf("linux") != -1) {
			return LINUX;
		}
		if(osStr.indexOf("unix") != -1) {
			return UNIX;
		}
		return OTHER;
	}
	
	public static NetworkInfo getNetwork(int index) {
		int osType = getOsType();
		if(osType == WINDOWS) {
			return WindowsNetworkHandle.getInstance().getNetworkInfo(index);
		}
		if(osType == LINUX) {
			return UnixNetworkHandle.getInstance().getNetworkInfo(index);
		}
		if(osType == UNIX) {
			return UnixNetworkHandle.getInstance().getNetworkInfo(index);
		}
		if(osType == OTHER) {
			return UnixNetworkHandle.getInstance().getNetworkInfo(index);
		}
		return null;
	}
	
	public static boolean setNetwork(NetworkInfo info) {
		int osType = getOsType();
		if(osType == WINDOWS) {
			return WindowsNetworkHandle.getInstance().setNetworkInfo(info);
		}
		if(osType == LINUX) {
			return UnixNetworkHandle.getInstance().setNetworkInfo(info);
		}
		if(osType == UNIX) {
			return UnixNetworkHandle.getInstance().setNetworkInfo(info);
		}
		if(osType == OTHER) {
			return UnixNetworkHandle.getInstance().setNetworkInfo(info);
		}
		return false;
	}
}
