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
public class LocalTcp31015 extends LocalTcpBase {
	private String srcAddr;
	
	
	// 返回值
	private int resultCode;

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
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
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_DelSource).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<DelSourceReq>");
					sb.append("<SrcArray>");
						sb.append("<SrcAddr type=\"string\">").append(this.srcAddr).append("</SrcAddr>");
					sb.append("</SrcArray>");
				sb.append("</DelSourceReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/DelSourceResp/ResultCode").getText());
		
	}
}
