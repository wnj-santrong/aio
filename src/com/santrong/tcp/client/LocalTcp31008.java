package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31008 extends LocalTcpBase {
	
	private int freeSize;
	private int maxTime;
	
	private int resultCode;

	public void setFreeSize(int freeSize) {
		this.freeSize = freeSize;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
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
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_SetThreshold).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<Config>");
					sb.append("<FreeSize type=\"int\">").append(this.freeSize).append("</FreeSize>");
					sb.append("<MaxTime type=\"int\">").append(this.maxTime).append("</MaxTime>");
				sb.append("</Config>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/SetThresholdRes/ResultCode").getText());
		
	}
}
