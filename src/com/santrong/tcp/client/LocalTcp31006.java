package com.santrong.tcp.client;

import com.santrong.system.Global;
import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31006 extends LocalTcpBase {
	private String confId;
	private int operType;// <!-- 0：停止录制; 1：开始录制||继续录制; 2：暂停录制; -->
	private String courseName;
	private String courseAbs;
	private String teacher;
	private String rcdName;
	
	private int resultCode;
	private String fileUrl;
	private String rcdTime;
	private int rSRCRcdSize;// 单位MB
	private int mVRcdSize;// 单位MB
	private int cMPSRcdSize;// 单位MB
	private int rcdType;

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public void setOperType(int operType) {
		this.operType = operType;
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

	public int getResultCode() {
		return resultCode;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public String getRcdTime() {
		return rcdTime;
	}

	public int getrSRCRcdSize() {
		return rSRCRcdSize;
	}

	public int getmVRcdSize() {
		return mVRcdSize;
	}

	public int getcMPSRcdSize() {
		return cMPSRcdSize;
	}

	public int getRcdType() {
		return rcdType;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_RecordCtl).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<RcdCtlReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<OperType type=\"int\">").append(this.operType).append("</OperType>");
					sb.append("<CourseName type=\"string\">").append(this.courseName).append("</CourseName>");
					sb.append("<CourseAbs type=\"string\">").append(this.courseAbs).append("</CourseAbs>");
					sb.append("<Teacher type=\"string\">").append(this.teacher).append("</Teacher>");
					sb.append("<RcdName type=\"string\">").append(this.rcdName).append("</RcdName>");
				sb.append("</RcdCtlReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/RcdCtlResp/ResultCode").getText());
		this.rSRCRcdSize = Integer.parseInt(xml.find("/MsgBody/RcdCtlResp/RSRCRcdSize").getText());
		this.mVRcdSize = Integer.parseInt(xml.find("/MsgBody/RcdCtlResp/MVRcdSize").getText());
		this.cMPSRcdSize = Integer.parseInt(xml.find("/MsgBody/RcdCtlResp/CMPSRcdSize").getText());
		this.rcdType = Integer.parseInt(xml.find("/MsgBody/RcdCtlResp/RcdType").getText());
		this.fileUrl = xml.find("/MsgBody/RcdCtlResp/FileURL").getText();
		this.rcdTime = xml.find("/MsgBody/RcdCtlResp/RcdTime").getText();
	}
}
