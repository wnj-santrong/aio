package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.LocalTcp31007.ConfInfo.RcdStreamInfo;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31007 extends LocalTcpBase {
	
	// 返回值
	private int resultCode;
	private List<ConfInfo> confInfoList = new ArrayList<ConfInfo>();

	public int getResultCode() {
		return resultCode;
	}

	public List<ConfInfo> getConfInfoList() {
		return confInfoList;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_GetConfInfo).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/GetConfInfoResp/ResultCode").getText());
		List<Element> confInfoElmentList = xml.finds("/MsgBody/GetConfInfoResp/ConfInfoArray/ConfInfo");
		for(Element el : confInfoElmentList) {
			ConfInfo item = new ConfInfo();
			item.confId = xml.find("/ConfID", el).getText();
			item.rcdName = xml.find("/RcdName", el).getText();
			item.startTime = xml.find("/StartTime", el).getText();
			item.rcdState = Integer.parseInt(xml.find("/RcdState").getText());
			item.liveState = Integer.parseInt(xml.find("/LiveState").getText());
			item.recordType = Integer.parseInt(xml.find("/RecordType").getText());
			item.layout = Integer.parseInt(xml.find("/Layout").getText());
			item.bScale = Integer.parseInt(xml.find("/bScale").getText());
			item.courseName = xml.find("/CourseName", el).getText();
			item.courseAbs = xml.find("/CourseAbs", el).getText();
			item.teacher = xml.find("/Teacher", el).getText();
			
			List<Element> rcdStreamInfoElmentList = xml.finds("/RcdStreamInfoArray/RcdStreamInfo", el);
			for(Element ee : rcdStreamInfoElmentList) {
				RcdStreamInfo entry = item.new RcdStreamInfo();
				
				entry.strmAddr = xml.find("/StrmAddr", ee).getText();
				entry.strmPort = Integer.parseInt(xml.find("/StrmPort", ee).getText());
				entry.strmUser = xml.find("/StrmUser", ee).getText();
				entry.strmPw = xml.find("/StrmPW", ee).getText();
				entry.strmType = Integer.parseInt(xml.find("/StrmType", ee).getText());
				entry.strmBandwidth = Integer.parseInt(xml.find("/StrmBandwidth", ee).getText());
				entry.strmFmt = Integer.parseInt(xml.find("/StrmFmt", ee).getText());
				entry.strmFRate = Integer.parseInt(xml.find("/StrmFRate", ee).getText());
				entry.audSmpRate = Integer.parseInt(xml.find("/AudSmpRate", ee).getText());
				entry.audCh = Integer.parseInt(xml.find("/AudCh", ee).getText());
				entry.audBitrate = Integer.parseInt(xml.find("/AudBitrate", ee).getText());
				
				item.recStreamInfoList.add(entry);
			}
			
			confInfoList.add(item);
		}
	}
	
	public class ConfInfo {
		private String confId;
		private String rcdName;
		private String startTime;
		private int rcdState;
		private int liveState;
		private int recordType;
		private int layout;
		private int bScale;
		private String courseName;
		private String courseAbs;
		private String teacher;
		private List<RcdStreamInfo> recStreamInfoList = new ArrayList<RcdStreamInfo>();
		
		public String getConfId() {
			return confId;
		}

		public String getRcdName() {
			return rcdName;
		}

		public int getRecordType() {
			return recordType;
		}

		public String getStartTime() {
			return startTime;
		}

		public int getRcdState() {
			return rcdState;
		}

		public int getLiveState() {
			return liveState;
		}

		public int getLayout() {
			return layout;
		}

		public int getbScale() {
			return bScale;
		}

		public String getCourseName() {
			return courseName;
		}

		public String getCourseAbs() {
			return courseAbs;
		}

		public String getTeacher() {
			return teacher;
		}

		public List<RcdStreamInfo> getRecStreamInfoList() {
			return recStreamInfoList;
		}

		public class RcdStreamInfo{
			private String strmAddr;
			private int strmPort;
			private String strmUser;
			private String strmPw;
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
			public int getStrmPort() {
				return strmPort;
			}
			public String getStrmUser() {
				return strmUser;
			}
			public String getStrmPw() {
				return strmPw;
			}
			public int getStrmType() {
				return strmType;
			}
			public int getStrmBandwidth() {
				return strmBandwidth;
			}
			public int getStrmFmt() {
				return strmFmt;
			}
			public int getStrmFRate() {
				return strmFRate;
			}
			public int getAudSmpRate() {
				return audSmpRate;
			}
			public int getAudCh() {
				return audCh;
			}
			public int getAudBitrate() {
				return audBitrate;
			}
			
		}
	}
}
