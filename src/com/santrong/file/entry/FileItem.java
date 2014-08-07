package com.santrong.file.entry;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author weinianjie
 * @date 2014年7月16日
 * @time 下午4:23:29
 */
public class FileItem {
	public static final int File_Status_Recording = 0;// 正在录制中
	public static final int File_Status_Recorded = 1;// 录制已完成
	public static final int File_Status_Uploading = 2;// ftp上传中
	public static final int File_Status_Uploaded = 3;// ftp上传完成
	
	public static final int File_Level_Open = 0;// 公开的课件
	public static final int File_Level_Close = 1;// 未公开的课件
	
	private String id;
	private String fileName;
	private String courseName;
	private String teacher;
	private String remark;
	private long fileSize;
	private long tarSize;
	private String duration;
	private int status;
	private int level;
	private int channel;
	private int bitRate;
	private int resolution;
	private int playCount;
	private int downloadCount;
	private Date cts;
	private Date uts;
	
	public String getFileSizeString() {
		long tmp = this.fileSize;
		if(tmp >= 1024) {
			tmp = tmp / 1024;
			if(tmp >= 1024) {
				tmp = tmp /1024;
				if(tmp >= 1024) {
					tmp = tmp / 1024;
					double s = ((double)(tmp / 1024));	
					DecimalFormat df = new DecimalFormat("#.00");
					return df.format(s) + "G";
				}else {
					return tmp + "M";
				}
			}else {
				return tmp + "K";
			}
		}else {
			return tmp + "B";
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public int getBitRate() {
		return bitRate;
	}
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}
	public int getResolution() {
		return resolution;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
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
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public long getTarSize() {
		return tarSize;
	}
	public void setTarSize(long tarSize) {
		this.tarSize = tarSize;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public int getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
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
