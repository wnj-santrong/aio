package com.santrong.system.network;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 上午9:45:03
 */
public class NetworkInfo {
	
	public static final int Type_Lan = 0;
	public static final int Type_Wan = 1;
	
	private int index;
	private String ip;
	private String mask;
	private String gateway;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	
}
