package com.santrong.meeting.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author weinianjie
 * @date 2014年7月21日
 * @time 上午10:44:57
 */
public class MeetingItem {
	// 布局layout
	public static final int Record_Mode_One_1 			= 110; //视频布局，总视频一路，第一种方案
	public static final int Record_Mode_TWO_1 			= 210; 
	public static final int Record_Mode_TWO_2 			= 220; 
	public static final int Record_Mode_TWO_3 			= 230; 
	public static final int Record_Mode_Three_1 		= 310; 
	public static final int Record_Mode_Three_2 		= 320; 
	public static final int Record_Mode_Three_3 		= 330; 
	public static final int Record_Mode_Three_4 		= 340; 
	public static final int Record_Mode_Three_5 		= 350; 
	public static final int Record_Mode_FOUR_1			= 410; 
	public static final int Record_Mode_FOUR_2 			= 420; 
	public static final int Record_Mode_FOUR_3 			= 430; 
	public static final int Record_Mode_FOUR_4 			= 440; 
	public static final int Record_Mode_FOUR_5 			= 450; 
	
	// 是否拉伸bScale
	public static final int Bscale_Extend				= 1; // 拉伸
	public static final int Bscale_Not_Extend			= 0; // 不拉伸
	
	// 录制模式recordType
	public static final int Record_Type_RSRC			= 1; // 资源模式
	public static final int Record_Type_MV				= 2; // 电影模式
	public static final int Record_Type_CMPS			= 4; // 合成模式
	
	public static final String Record_Type_RSRC_Dir			= "rsrc"; // 资源模式
	public static final String Record_Type_MV_Dir			= "mv"; // 电影模式
	public static final String Record_Type_CMPS_Dir			= "cmps"; // 合成模式
	
	
	// 分辨率
	public static final int Resolution_emCif			= 3; // 352*288
	public static final int Resolution_em4Cif			= 4; // 704*576
	public static final int Resolution_em720P			= 22; // 1280*720
	public static final int Resolution_em1080P			= 25; // 1920*1080
	public static final int Resolution_em12801024		= 35; // 1280*1024
	
	// 系统低层教室名称
	public static final String ConfIdPreview = "CLSRM_";// 教室ID：CLSRM_*，*代表1——N，N为服务器所支持的最大教室数量
	
	private String id;
	private String showName;
	private String courseName;
	private String teacher;
	private String remark;
	private int bitRate;
	private int resolution;
	private int maxTime;
	private int useRecord;
	private int recordMode;
	private int recordType;
	private int channel;
	private Date cts;
	private Date uts;
	
	//内存状态
	private int isLive;
	private int isRecord;
	private int isConnect;
	
	private List<DatasourceItem> dsList = new ArrayList<DatasourceItem>();

	public int getIsConnect() {
		return isConnect;
	}

	public void setIsConnect(int isConnect) {
		this.isConnect = isConnect;
	}

	public int getUseRecord() {
		return useRecord;
	}

	public void setUseRecord(int useRecord) {
		this.useRecord = useRecord;
	}

	public int getIsLive() {
		return isLive;
	}

	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}
	public List<DatasourceItem> getDsList() {
		return dsList;
	}

	public void setDsList(List<DatasourceItem> dsList) {
		this.dsList = dsList;
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

	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
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
	public int getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public int getIsRecord() {
		return isRecord;
	}
	public void setIsRecord(int isRecord) {
		this.isRecord = isRecord;
	}
	public int getRecordMode() {
		return recordMode;
	}
	public void setRecordMode(int recordMode) {
		this.recordMode = recordMode;
	}
	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
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
