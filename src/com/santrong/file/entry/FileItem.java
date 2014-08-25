package com.santrong.file.entry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.DirDefine;
import com.santrong.util.CommonTools;
import com.santrong.util.FileUtils;

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
	public static final int File_Status_ERROR = 10;// 异常课件
	
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
	private int recordType;
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
		return CommonTools.formatDiskSize(this.fileSize);
	}
	
	// 获取缩略图
	public List<String> getThumbnail() {
		StringBuilder sb = new StringBuilder();
		sb.append("/CLSRM_").append(this.channel).append("/").append(this.fileName).append("/");
		if(this.recordType == MeetingItem.Record_Type_RSRC) {
			sb.append(MeetingItem.Record_Type_RSRC_Dir);
		}else if(this.recordType == MeetingItem.Record_Type_MV) {
			sb.append(MeetingItem.Record_Type_MV_Dir);
		}else if(this.recordType == MeetingItem.Record_Type_CMPS) {
			sb.append(MeetingItem.Record_Type_CMPS_Dir);
		}
		sb.append("/resource/thumbnail");
		List<File> files = FileUtils.getAllFileAndDirectory(DirDefine.VedioDir + sb.toString());// 获取目录下的所有缩略图，默认名称排序
		List<String> fileList = new ArrayList<String>();
		for(File f:files) {
			fileList.add(f.getPath());
		}
		if(fileList.size() == 0) {
			fileList.add("/resource/photo/video01.jpg");
		}else {
			Collections.sort(fileList); // 自然顺序排序
		}
		
		return fileList;
	}
	
	// 获取分辨率
	public String getResolutionString() {
		switch(this.getResolution()) {
			case MeetingItem.Resolution_emCif :
				return "352*288";
			case MeetingItem.Resolution_em4Cif :
				return "704*576";
			case MeetingItem.Resolution_em720P :
				return "1280*720";
			case MeetingItem.Resolution_em1080P :
				return "1920*1080";
			case MeetingItem.Resolution_em12801024 :
				return "1280*1024";
			default :
				return "";
		}
	}
	
	public int getRecordType() {
		return recordType;
	}
	public void setRecordType(int recordType) {
		this.recordType = recordType;
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
