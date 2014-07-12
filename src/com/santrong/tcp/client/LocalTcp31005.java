package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcp;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31005 extends AbstractTcp {
	
	private List<String> confIdList = new ArrayList<String>();
	
	private int resultCode;
	private String _confId;
	private int rcdResult;
	private String fileUrl;
	private String rcdTime;
	private int rSRCRcdSize;
	private int mVRcdSize;
	private int cMPSRcdSize;
	private int rcdType;
	
	public void setConfIdList(List<String> confIdList) {
		this.confIdList = confIdList;
	}
	
	public int getResultCode() {
		return resultCode;
	}


	public String get_confId() {
		return _confId;
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
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_StopConfRecord).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<StopConfRcdReq>");
					for(String id : confIdList) {
						sb.append("<ConfID type=\"string\">").append(id).append("</ConfID>");
					}
				sb.append("</StopConfRcdReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
	return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/ResultCode").getText());
		this.rcdResult = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/RcdResult").getText());
		this.rSRCRcdSize = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/RSRCRcdSize").getText());
		this.mVRcdSize = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/MVRcdSize").getText());
		this.cMPSRcdSize = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/CMPSRcdSize").getText());
		this.rcdType = Integer.parseInt(xml.find("/MsgBody/StopConfRcdResp/RcdType").getText());
		this._confId = xml.find("/MsgBody/StopConfRcdResp/ConfID").getText();
		this.fileUrl = xml.find("/MsgBody/StopConfRcdResp/FileURL").getText();
		this.rcdTime = xml.find("/MsgBody/StopConfRcdResp/RcdTime").getText();
		
	}
}
