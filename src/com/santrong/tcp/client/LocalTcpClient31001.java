package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcpClient31001 extends AbstractTcpClient{
	
	public String getMajorXml() {
		return header
			+ "<ReqMsg>"
				+ "<MsgHead>"
					+ "<MsgCode>" + TcpDefine.Basic_Client_Login + "</MsgCode>"
					+ "<ModId>" + TcpDefine.ModuleSign + "</ModId>"
					+ "<SessionId>1</SessionId>"
				+ "</MsgHead>"
				+ "<MsgBody>"
					+ "<LoginReq>"
						+ "<Addr type=\"string\">#{Addr}</Addr>"
						+ "<Port type=\"int\">#{Port}</Port>"
						+ "<Heartbeat type=\"int\">#{Heartheat}</Heartbeat>"
					+ "</LoginReq>"
				+ "</MsgBody>"
			+ "</ReqMsg>";
	}
}
