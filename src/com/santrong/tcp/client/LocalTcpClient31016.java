package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcpClient31016 extends AbstractTcpClient{
	
	public String getMajorXml() { 
		return header
			+ "<ReqMsg>"
				+ "<MsgHead>"
					+ "<MsgCode>" + TcpDefine.Basic_Client_GetSourceState + "</MsgCode>"
					+ "<ModId>" + TcpDefine.ModuleSign + "</ModId>"
					+ "<SessionId>1</SessionId>"
				+ "</MsgHead>"
				+ "<MsgBody>"
					+ "<GetSrcStateReq>"
						+ "<SrcArray>"
							+ "#{subXml1}"
						+ "</SrcArray>"
					+ "</GetSrcStateReq>"
				+ "</MsgBody>"
			+ "</ReqMsg>";
	}
	
	public String getSubXml1(){
		return  "<SrcAddr type=\"string\">#{SrcAddr}</SrcAddr>";
	}
}
