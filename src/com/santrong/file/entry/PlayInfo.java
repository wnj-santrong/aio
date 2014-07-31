package com.santrong.file.entry;

/**
 * @author weinianjie
 * @date 2014年7月31日
 * @time 下午4:28:02
 */
public class PlayInfo {
		
//	strType:表示播放类型，1表示带有管理权限的直播，2表示普通直播，3点播 
//	strAddr:服务器IP地址 
//	strConfID:会议ID 
//	strFilePath:点播文件在服务器上的路径。仅当strType为3时有效 
//	strLiveType:直播类型，1表示资源模式，2表示电影模式，4表示合成模式。当strType为1、2时有效。
	
	public static final int Type_Live_Manager = 1;
	public static final int Type_Live = 2;
	public static final int Type_Vod = 3;
	
	public static final int LiveType_MD_RSRC = 1;
	public static final int LiveType_MD_MV = 2;
	public static final int LiveType_MD_CMPS = 4;
	
	private String id;
	private int type;
	private String addr;
	private String confId;
	private String filePath;
	private int liveType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getConfId() {
		return confId;
	}
	public void setConfId(String confId) {
		this.confId = confId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getLiveType() {
		return liveType;
	}
	public void setLiveType(int liveType) {
		this.liveType = liveType;
	}
}
