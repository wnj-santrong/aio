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
public class LocalTcp31014 extends LocalTcpBase {

	private String srcAddr;
	private int srcPort;
	private String srcUser;
	private String srcPw;
	private int srcType;
	
	// 返回值
	private int resultCode;

	public int getResultCode() {
		return resultCode;
	}

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
	}

	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}

	public void setSrcUser(String srcUser) {
		this.srcUser = srcUser;
	}

	public void setSrcPw(String srcPw) {
		this.srcPw = srcPw;
	}

	public void setSrcType(int srcType) {
		this.srcType = srcType;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_AddSource).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<AddSourceReq>");
					sb.append("<SrcAddr type=\"string\">").append(this.srcAddr).append("</SrcAddr>");
					sb.append("<SrcPort type=\"int\">").append(this.srcPort).append("</SrcPort>");
					sb.append("<SrcUser type=\"string\">").append(this.srcUser).append("</SrcUser>");
					sb.append("<SrcPw type=\"string\">").append(this.srcPw).append("</SrcPw>");
					sb.append("<SrcType type=\"int\">").append(this.srcType).append("</SrcType>");				
				sb.append("</AddSourceReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/AddSourceResp/ResultCode").getText());
		
	}
}
