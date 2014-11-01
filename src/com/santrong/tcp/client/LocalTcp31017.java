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
public class LocalTcp31017 extends LocalTcpBase {
	
	private String confId;
	private int bPushStream;
	private String url;	
	
	// 返回值
	private int resultCode;

	public int getResultCode() {
		return resultCode;
	}
	
	public void setConfId(String confId) {
		this.confId = confId;
	}

	public void setbPushStream(int bPushStream) {
		this.bPushStream = bPushStream;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_PushStream).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<PushStrmReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<PushStrmReq type=\"int\">").append(this.bPushStream).append("</bPushStream>");
					sb.append("<Url type=\"string\">").append(this.url).append("</Url>");
				sb.append("</PushStrmReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/PushStrmResp/ResultCode").getText());
	}

}
