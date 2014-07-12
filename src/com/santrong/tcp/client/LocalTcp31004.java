package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31004 implements AbstractTcpClient {
	
	private String confId;
	private int isLive;
	private int isRecord;
	private int recordType;
	private int layout;
	private int bScale;
	private String courseName;
	private String courseAbs;
	private String teacher;
	private String rcdName;
	private List<RecStreamInfo> recStreamInfoList = new ArrayList<RecStreamInfo>();
	
	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
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

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public int getbScale() {
		return bScale;
	}

	public void setbScale(int bScale) {
		this.bScale = bScale;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseAbs() {
		return courseAbs;
	}

	public void setCourseAbs(String courseAbs) {
		this.courseAbs = courseAbs;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getRcdName() {
		return rcdName;
	}

	public void setRcdName(String rcdName) {
		this.rcdName = rcdName;
	}

	public List<RecStreamInfo> getRecStreamInfoList() {
		return recStreamInfoList;
	}

	public void setRecStreamInfoList(List<RecStreamInfo> recStreamInfoList) {
		this.recStreamInfoList = recStreamInfoList;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_StartConfRecord).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<StartConfRecordReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<IsLive type=\"int\">").append(this.isLive).append("</IsLive>");
					sb.append("<IsRecord type=\"int\">").append(this.isRecord).append("</IsRecord>");
					sb.append("<RecordType type=\"int\">").append(this.recordType).append("</RecordType>");
					sb.append("<Layout type=\"int\">").append(this.layout).append("</Layout>");
					sb.append("<bScale type=\"int\">").append(this.bScale).append("</bScale>");
					sb.append("<CourseName type=\"string\">").append(this.courseName).append("</CourseName>");
					sb.append("<CourseAbs type=\"string\">").append(this.courseAbs).append("</CourseAbs>");
					sb.append("<Teacher type=\"string\">").append(this.teacher).append("</Teacher>");
					sb.append("<RcdName type=\"string\">").append(this.rcdName).append("</RcdName>");
					sb.append("<RcdStreamInfoArray>");
						for(RecStreamInfo item : recStreamInfoList){
							sb.append("<RcdStreamInfo>");
								sb.append("<StrmAddr type=\"string\">").append(item.strmAddr).append("</StrmAddr>");
								sb.append("<StrmPort type=\"int\">").append(item.strmPort).append("</StrmPort>");
								sb.append("<StrmUser type=\"string\">").append(item.StrmUser).append("</StrmUser>");
								sb.append("<StrmPW type=\"string\">").append(item.StrmPw).append("</StrmPW>");
								sb.append("<StrmType type=\"int\">").append(item.strmType).append("</StrmType>");
								sb.append("<StrmBandwidth type=\"int\">").append(item.strmBandwidth).append("</StrmBandwidth>");
								sb.append("<StrmFmt type=\"int\">").append(item.strmFmt).append("</StrmFmt>");
								sb.append("<StrmFRate type=\"int\">").append(item.strmFRate).append("</StrmFRate>");
								sb.append("<AudSmpRate type=\"int\">").append(item.audSmpRate).append("</AudSmpRate>");
								sb.append("<AudCh type=\"int\">").append(item.audCh).append("</AudCh>");
								sb.append("<AudBitrate type=\"int\">").append(item.audBitrate).append("</AudBitrate>");
							sb.append("</RcdStreamInfo>");
						}
					sb.append("</RcdStreamInfoArray>");
				sb.append("</StartConfRecordReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(String repXml) {
		// TODO Auto-generated method stub
		
	}
	
	public class RecStreamInfo{
		private String strmAddr;
		private int strmPort;
		private String StrmUser;
		private String StrmPw;
		private int strmType;
		private int strmBandwidth;
		private int strmFmt;
		private int strmFRate;
		private int audSmpRate;
		private int audCh;
		private int audBitrate;
		public String getStrmAddr() {
			return strmAddr;
		}
		public void setStrmAddr(String strmAddr) {
			this.strmAddr = strmAddr;
		}
		public int getStrmPort() {
			return strmPort;
		}
		public void setStrmPort(int strmPort) {
			this.strmPort = strmPort;
		}
		public String getStrmUser() {
			return StrmUser;
		}
		public void setStrmUser(String strmUser) {
			StrmUser = strmUser;
		}
		public String getStrmPw() {
			return StrmPw;
		}
		public void setStrmPw(String strmPw) {
			StrmPw = strmPw;
		}
		public int getStrmType() {
			return strmType;
		}
		public void setStrmType(int strmType) {
			this.strmType = strmType;
		}
		public int getStrmBandwidth() {
			return strmBandwidth;
		}
		public void setStrmBandwidth(int strmBandwidth) {
			this.strmBandwidth = strmBandwidth;
		}
		public int getStrmFmt() {
			return strmFmt;
		}
		public void setStrmFmt(int strmFmt) {
			this.strmFmt = strmFmt;
		}
		public int getStrmFRate() {
			return strmFRate;
		}
		public void setStrmFRate(int strmFRate) {
			this.strmFRate = strmFRate;
		}
		public int getAudSmpRate() {
			return audSmpRate;
		}
		public void setAudSmpRate(int audSmpRate) {
			this.audSmpRate = audSmpRate;
		}
		public int getAudCh() {
			return audCh;
		}
		public void setAudCh(int audCh) {
			this.audCh = audCh;
		}
		public int getAudBitrate() {
			return audBitrate;
		}
		public void setAudBitrate(int audBitrate) {
			this.audBitrate = audBitrate;
		}
		
	}

}
