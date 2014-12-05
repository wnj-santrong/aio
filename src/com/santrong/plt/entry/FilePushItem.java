package com.santrong.plt.entry;

import java.util.Date;

/**
 * @author weinianjie
 * @date 2014年12月5日
 * @time 上午11:04:46
 */
public class FilePushItem {
	private String id;
	private String fileId;
	private String username;
	private int status;
	private Date cts;
	
	public static final int File_Push_Status_Wating = 0;
	public static final int File_Push_Status_Pushing = 1;
	public static final int File_Push_Status_Error = 2;
	public static final int File_Push_Status_Done = 3;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCts() {
		return cts;
	}
	public void setCts(Date cts) {
		this.cts = cts;
	}
}
