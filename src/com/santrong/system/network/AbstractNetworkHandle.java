package com.santrong.system.network;


/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 上午9:44:36
 */
public abstract class AbstractNetworkHandle {
	
	/*
	 * 设置网络
	 */
	public abstract boolean setNetworkInfo(NetworkInfo vo);
	
	
	/*
	 * 获取网络
	 */
	public abstract NetworkInfo getNetworkInfo(int index);
}
