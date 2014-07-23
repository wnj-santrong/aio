package com.santrong.meeting.entry;

import java.util.Date;

/**
 * @author weinianjie
 * @date 2014年7月23日
 * @time 上午11:29:47
 */
public class DatasourceItem {
	
	// 数据源类型strmType
	public static final int Datasoruce_Type_VGA			= 1; // VGA
	public static final int Datasoruce_Type_Camera		= 0; // 摄像头
	
	private String id;
	private String addr;
	private int port;
	private String username;
	private String password;
	private String meetingId;
	private int dsType;
	private int priority;
	private Date cts;
	private Date uts;
	
	private int isConnected;
	
	public int getIsConnected() {
		return isConnected;
	}
	public void setIsConnected(int isConnected) {
		this.isConnected = isConnected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMeetingId() {
		return meetingId;
	}
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getDsType() {
		return dsType;
	}
	public void setDsType(int dsType) {
		this.dsType = dsType;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getCts() {
		return cts;
	}
	public void setCts(Date cts) {
		this.cts = cts;
	}
	public Date getUts() {
		return uts;
	}
	public void setUts(Date uts) {
		this.uts = uts;
	}
}
