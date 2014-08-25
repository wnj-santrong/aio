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
public class LocalTcp31005 extends LocalTcpBase {
	
	private String confId;
	
	private int resultCode;
	private String _confId;
	private int rcdResult;
	private String fileUrl;
	private String rcdTime;
	private long rSRCRcdSize;
	private long mVRcdSize;
	private long cMPSRcdSize;
	private int rcdType;
	
	public void setConfId(String confId) {
		this.confId = confId;
	}


	public int getResultCode() {
		return resultCode;
	}


	public String get_confId() {
		return _confId;
	}

	public long getrSRCRcdSize() {
		return rSRCRcdSize;
	}

	public long getmVRcdSize() {
		return mVRcdSize;
	}

	public long getcMPSRcdSize() {
		return cMPSRcdSize;
	}

	public int getRcdResult() {
		return rcdResult;
	}



	public String getFileUrl() {
		return fileUrl;
	}



	public String getRcdTime() {
		return rcdTime;
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
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_StopConfRecord).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<StopConfRcdReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
				sb.append("</StopConfRcdReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
	return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/ResultCode").getText());
		this.rcdResult = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/RcdResult").getText());
		this.rSRCRcdSize = Long.parseLong(xml.find("/MsgBody/StopConfRcdResp/RSRCRcdSize").getText());
		this.mVRcdSize = Long.parseLong(xml.find("/MsgBody/StopConfRcdResp/MVRcdSize").getText());
		this.cMPSRcdSize = Long.parseLong(xml.find("/MsgBody/StopConfRcdResp/CMPSRcdSize").getText());
		this.rcdType = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/RcdType").getText());
		this._confId = xml.find("/MsgBody/StopConfRcdResp/ConfID").getText();
		this.fileUrl = xml.find("/MsgBody/StopConfRcdResp/FileURL").getText();
		this.rcdTime = xml.find("/MsgBody/StopConfRcdResp/RcdTime").getText();
		
	}
}
