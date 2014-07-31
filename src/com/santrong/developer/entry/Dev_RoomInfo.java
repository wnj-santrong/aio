package com.santrong.developer.entry;

/**
 * @author weinianjie
 * @date 2014年7月31日
 * @time 下午8:05:49
 */
public class Dev_RoomInfo {
	private String confId;
	private int isConnect;
	private int isLive;
	private int isRecord;
	private int liveSource;
	
	public String getConfId() {
		return confId;
	}
	public void setConfId(String confId) {
		this.confId = confId;
	}
	public int getIsConnect() {
		return isConnect;
	}
	public void setIsConnect(int isConnect) {
		this.isConnect = isConnect;
	}
	public int getIsLive() {
		return isLive;
	}
	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}
	public int getIsRecord() {
		return isRecord;
	}
	public void setIsRecord(int isRecord) {
		this.isRecord = isRecord;
	}
	public int getLiveSource() {
		return liveSource;
	}
	public void setLiveSource(int liveSource) {
		this.liveSource = liveSource;
	}
}
