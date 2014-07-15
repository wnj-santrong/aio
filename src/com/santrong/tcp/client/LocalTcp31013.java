package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31013 extends LocalTcpBase {
	private String confId;
	private int strmId;
	
	
	// 返回值
	private int resultCode;
	private String _confId;
	private int _strmId;

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public void setStrmId(int strmId) {
		this.strmId = strmId;
	}
	
	public int getResultCode() {
		return resultCode;
	}

	public String get_confId() {
		return _confId;
	}

	public int get_strmId() {
		return _strmId;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_DirectCtrl).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<DirectCtrlReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<StrmId type=\"int\">").append(this.strmId).append("</StrmId>");
				sb.append("</DirectCtrlReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/DirectCtrlResp/ResultCode").getText());
		this._strmId = Integer.parseInt(xml.find("/MsgBody/DirectCtrlResp/StrmId").getText());
		this._confId = xml.find("/MsgBody/DirectCtrlResp/ConfID").getText();
		
	}
}
