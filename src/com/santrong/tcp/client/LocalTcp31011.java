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
public class LocalTcp31011 extends LocalTcpBase {
	// 参数
	private int logLevel;
	
	// 返回值
	private int resultCode;

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
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
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_SetLogLevel).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<SetLogLevelReq>");
					sb.append("<LogLevel type=\"int\">").append(this.logLevel).append("</LogLevel>");
				sb.append("</SetLogLevelReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/SetLogLevelResp/ResultCode").getText());
		
	}
}
