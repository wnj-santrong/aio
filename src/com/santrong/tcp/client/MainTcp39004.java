package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class MainTcp39004 extends LocalTcpBase{
	
	private int resultCode;
	private String maintainVersion;
	private String controlVersion;
	private String uniserverVersion;
	private String vodserverVersion;
	private String webserviceVerion;
	private int maintainStatus;
	private int controlStatus;
	private int uniserverStatus;
	private int vodserverStatus;
	
	public int getResultCode() {
		return resultCode;
	}

	public String getMaintainVersion() {
		return maintainVersion;
	}

	public String getControlVersion() {
		return controlVersion;
	}

	public String getUniserverVersion() {
		return uniserverVersion;
	}

	public String getVodserverVersion() {
		return vodserverVersion;
	}

	public String getWebserviceVerion() {
		return webserviceVerion;
	}

	public int getMaintainStatus() {
		return maintainStatus;
	}

	public int getControlStatus() {
		return controlStatus;
	}

	public int getUniserverStatus() {
		return uniserverStatus;
	}

	public int getVodserverStatus() {
		return vodserverStatus;
	}
	
	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_GetModInfo).append("</MsgCode>");
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

		this.resultCode = Integer.parseInt(xml.find("/MsgBody/GetModInfoResp/ResultCode").getText());
		this.maintainVersion = xml.find("/MsgBody/GetModInfoResp/Version/maintain").getText();
		this.controlVersion = xml.find("/MsgBody/GetModInfoResp/Version/control").getText();
		this.uniserverVersion = xml.find("/MsgBody/GetModInfoResp/Version/uniserver").getText();
		this.vodserverVersion = xml.find("/MsgBody/GetModInfoResp/Version/vodserver").getText();
		this.webserviceVerion = xml.find("/MsgBody/GetModInfoResp/Version/webservice").getText();
		this.maintainStatus = Integer.parseInt(xml.find("/MsgBody/GetModInfoResp/State/maintain").getText());
		this.controlStatus = Integer.parseInt(xml.find("/MsgBody/GetModInfoResp/State/control").getText());
		this.uniserverStatus = Integer.parseInt(xml.find("/MsgBody/GetModInfoResp/State/uniserver").getText());
		this.vodserverStatus = Integer.parseInt(xml.find("/MsgBody/GetModInfoResp/State/vodserver").getText());
	}

}
