package com.santrong.meeting.entry;

import java.util.Date;

import com.mysql.jdbc.StringUtils;
import com.santrong.log.Log;

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
	
	// 数据源类型strmType
	public static final int Datasoruce_Type_VGA			= 1; // VGA
	public static final int Datasoruce_Type_Camera		= 0; // 摄像头
	
	// 系统低层教室名称
	public static final String ConfIdPreview = "CLSRM_";// 教室ID：CLSRM_*，*代表1——N，N为服务器所支持的最大教室数量
	
	private int id;
	private String showName;
	private String courseName;
	private String teacher;
	private String remark;
	private int bitRate;
	private String resolution;
	private int maxTime;
	private String datasource1;
	private String datasource2;
	private String datasource3;
	private int useRecord;
	private int recordMode;
	private int channel;
	private Date cts;
	private Date uts;
	
	//内存状态
	private int isLive;
	private int isRecord;
	private int isConnected;
	
	public int getWidth() {
		if(!StringUtils.isNullOrEmpty(this.resolution)) {
			String[] arr = resolution.split("\\*");
			try{
				return Integer.parseInt(arr[0]);
			}catch(Exception e) {
				Log.printStackTrace(e);
				return 0;
			}
		}
		return 0;
	}
	
	public int getHeight() {
		if(!StringUtils.isNullOrEmpty(this.resolution)) {
			String[] arr = resolution.split("\\*");
			try{
				return Integer.parseInt(arr[1]);
			}catch(Exception e) {
				Log.printStackTrace(e);
				return 0;
			}
		}
		return 0;
	}	
	
	
	public int getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(int isConnected) {
		this.isConnected = isConnected;
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

	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public int getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	public String getDatasource1() {
		return datasource1;
	}
	public void setDatasource1(String datasource1) {
		this.datasource1 = datasource1;
	}
	public String getDatasource2() {
		return datasource2;
	}

	public void setDatasource2(String datasource2) {
		this.datasource2 = datasource2;
	}

	public String getDatasource3() {
		return datasource3;
	}

	public void setDatasource3(String datasource3) {
		this.datasource3 = datasource3;
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
