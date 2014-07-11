package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcpClient31014 extends AbstractTcpClient{
	
	public String getMajorXml() {
		return header
			+ "<ReqMsg>"
				+ "<MsgHead>"
					+ "<MsgCode>" + TcpDefine.Basic_Client_AddSource + "</MsgCode>"
					+ "<ModId>" + TcpDefine.ModuleSign + "</ModId>"
					+ "<SessionId>1</SessionId>"
				+ "</MsgHead>"
				+ "<MsgBody>"
					+ "<AddSourceReq>"
						+ "<SrcAddr type=\"string\">#{SrcAddr}</SrcAddr>"
						+ "<SrcPort type=\"int\">#{SrcPort}</SrcPort>"
						+ "<SrcUser type=\"string\">#{SrcUser}</SrcUser>"
						+ "<SrcPw type=\"string\">#{SrcPw}</SrcPw>"
						+ "<SrcType type=\"int\">#{SrcType}</SrcType>"				
					+ "</AddSourceReq>"
				+ "</MsgBody>"
			+ "</ReqMsg>";	
	}
}
