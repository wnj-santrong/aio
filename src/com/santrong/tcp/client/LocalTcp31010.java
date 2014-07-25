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
public class LocalTcp31010 extends LocalTcpBase {
	private String courseName;
	private String confId;
	
	// 返回值
	private int resultCode;


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public int getResultCode() {
		return resultCode;
	}


	public void setConfId(String confId) {
		this.confId = confId;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_DeleteCourse).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<DelCourseReq>");
					sb.append("<CourseName type=\"string\">").append(this.courseName).append("</CourseName>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
				sb.append("</DelCourseReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/DelCourseResp/ResultCode").getText());
		
	}
	
}
