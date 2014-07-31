package com.santrong.system.status;

/**
 * @author weinianjie
 * @date 2014年7月21日
 * @time 下午5:20:35
 */
public class RoomStatusEntry {
	private int isConnect;
	private int isRecord;
	private int isLive;
	private int liveSource;//开会是从哪里发起的，0网页，1面板，默认网页，即使是面板开启的，如果程序重启了，依然当成网页开启的来处理
	
	public int getIsConnect() {
		return isConnect;
	}
	public void setIsConnect(int isConnect) {
		this.isConnect = isConnect;
	}
	public int getIsRecord() {
		return isRecord;
	}
	public void setIsRecord(int isRecord) {
		this.isRecord = isRecord;
	}
	public int getIsLive() {
		return isLive;
	}
	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}
	public int getLiveSource() {
		return liveSource;
	}
	public void setLiveSource(int liveSource) {
		this.liveSource = liveSource;
	}
}
