package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcp;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31006 extends AbstractTcp {
	private String confId;
	private int operType;// <!-- 0：停止录制; 1：开始录制; 2：暂停录制; -->
	
	private int resultCode;

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}
	
	public int getResultCode() {
		return resultCode;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_RecordCtl).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<RcdCtlReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<OperType type=\"int\">").append(this.operType).append("</OperType>");
				sb.append("</RcdCtlReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/RcdCtlResp/ResultCode").getText());
		
	}
}
