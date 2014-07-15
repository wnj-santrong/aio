package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31004 extends LocalTcpBase {
	
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

	private int resultCode;
	private String _confId;
	private int doRcd;
	private int doUni;
	private String _rcdName;
	
	public void setConfId(String confId) {
		this.confId = confId;
	}

	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}

	public void setIsRecord(int isRecord) {
		this.isRecord = isRecord;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public void setbScale(int bScale) {
		this.bScale = bScale;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setCourseAbs(String courseAbs) {
		this.courseAbs = courseAbs;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public void setRcdName(String rcdName) {
		this.rcdName = rcdName;
	}

	public void setRecStreamInfoList(List<RecStreamInfo> recStreamInfoList) {
		this.recStreamInfoList = recStreamInfoList;
	}

	
	
	public int getResultCode() {
		return resultCode;
	}

	public String get_confId() {
		return _confId;
	}

	public int getDoRcd() {
		return doRcd;
	}

	public int getDoUni() {
		return doUni;
	}

	public String get_rcdName() {
		return _rcdName;
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
								if(item.hasAud == 1) {
									sb.append("<AudSmpRate type=\"int\">").append(item.audSmpRate).append("</AudSmpRate>");
									sb.append("<AudCh type=\"int\">").append(item.audCh).append("</AudCh>");
									sb.append("<AudBitrate type=\"int\">").append(item.audBitrate).append("</AudBitrate>");
								}
							sb.append("</RcdStreamInfo>");
						}
					sb.append("</RcdStreamInfoArray>");
				sb.append("</StartConfRecordReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/StartConfRcdResp/ResultCode").getText());
		this._confId = xml.find("/MsgBody/StartConfRcdResp/ConfID").getText();
		this.doRcd = Integer.parseInt(xml.find("/MsgBody/StartConfRcdResp/DoRcd").getText());
		this.doUni = Integer.parseInt(xml.find("/MsgBody/StartConfRcdResp/DoUni").getText());
		this._rcdName = xml.find("/MsgBody/StartConfRcdResp/RcdName").getText();
		
	}
	
	public class RecStreamInfo{
		private String strmAddr;
		private int strmPort;
		private String StrmUser;
		private String StrmPw;
		private int strmType;// 类型
		private int strmBandwidth;// 码率
		private int strmFmt;// 格式（宽高） 720P这类的
		private int strmFRate;// 帧率
		//---------------------------以下是音频，如果没有请不要发送
		private int audSmpRate;// 声音采样率
		private int audCh;// 声道
		private int audBitrate;// 声音码率
		
		private int hasAud; // 是否有音频
		
		public void setStrmAddr(String strmAddr) {
			this.strmAddr = strmAddr;
		}
		public void setStrmPort(int strmPort) {
			this.strmPort = strmPort;
		}
		public void setStrmUser(String strmUser) {
			StrmUser = strmUser;
		}
		public void setStrmPw(String strmPw) {
			StrmPw = strmPw;
		}
		public void setStrmType(int strmType) {
			this.strmType = strmType;
		}
		public void setStrmBandwidth(int strmBandwidth) {
			this.strmBandwidth = strmBandwidth;
		}
		public void setStrmFmt(int strmFmt) {
			this.strmFmt = strmFmt;
		}
		public void setStrmFRate(int strmFRate) {
			this.strmFRate = strmFRate;
		}
		public void setAudSmpRate(int audSmpRate) {
			this.audSmpRate = audSmpRate;
		}
		public void setAudCh(int audCh) {
			this.audCh = audCh;
		}
		public void setAudBitrate(int audBitrate) {
			this.audBitrate = audBitrate;
		}
		public void setHasAud(int hasAud) {
			this.hasAud = hasAud;
		}
	}

}
