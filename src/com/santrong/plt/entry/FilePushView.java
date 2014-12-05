package com.santrong.plt.entry;

import java.util.Date;

import com.santrong.util.SantrongUtils;

/**
 * @author weinianjie
 * @date 2014年12月5日
 * @time 上午11:35:57
 */
public class FilePushView {
	private String id;						// 推送Id
	private String fileId;				// 文件Id
	private String username;			// 平台用户名
	private String status;				// 推送状态
	private String fileName;			// 文件名
	private String courseName;		// 课程名
	private String duration;			// 录制时长
	private String teacher;				// 授课老师
	private long fileSize;					// 课件大小
	private Date cts;						// 课程录制时间
	
	public String getFileSizeString() {
		return SantrongUtils.formatDiskSize(this.fileSize);
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Date getCts() {
		return cts;
	}
	public void setCts(Date cts) {
		this.cts = cts;
	}
}
