package com.santrong.tcp.client;

import com.santrong.system.Global;
import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.MainTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class MainTcp31001 extends MainTcpBase{
	
	private String addr;
	private int port;
	private int heartbeat;
	
	private int resultCode;
	private int isReboot;

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}

	public int getResultCode() {
		return resultCode;
	}

	public int getIsReboot() {
		return isReboot;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Main_Client_Login).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<LoginReq>");
					sb.append("<Addr type=\"string\">").append(this.addr).append("</Addr>");
					sb.append("<Port type=\"int\">").append(this.port).append("</Port>");
					sb.append("<Heartbeat type=\"int\">").append(this.heartbeat).append("</Heartbeat>");
				sb.append("</LoginReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {

		this.resultCode = Integer.parseInt(xml.find("/MsgBody/LoginResp/ResultCode").getText());
		this.isReboot = Integer.parseInt(xml.find("/MsgBody/LoginResp/IsReboot").getText());
	}

}
