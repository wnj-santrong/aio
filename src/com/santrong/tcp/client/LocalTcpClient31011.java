package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcpClient31011 {
	
	public String getMajorXml() {
		return TcpDefine.Xml_Header
			+ "<ReqMsg>"
				+ "<MsgHead>"
					+ "<MsgCode>" + TcpDefine.Basic_Client_SetLogLevel + "</MsgCode>"
					+ "<ModId>" + TcpDefine.ModuleSign + "</ModId>"
					+ "<SessionId>1</SessionId>"
				+ "</MsgHead>"
				+ "<MsgBody>"
					+ "<SetLogLevelReq>"
						+ "<LogLevel type=\"int\">#{LogLevel}</LogLevel>"
					+ "</SetLogLevelReq>"
				+ "</MsgBody>"
			+ "</ReqMsg>";
	}
}
