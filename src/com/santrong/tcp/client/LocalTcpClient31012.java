package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcpClient31012 extends AbstractTcpClient{
	
	public String getMajorXml() {
		return header
			+ "<ReqMsg>"
				+ "<MsgHead>"
					+ "<MsgCode>" + TcpDefine.Basic_Client_TiltCtrl + "</MsgCode>"
					+ "<ModId>" + TcpDefine.ModuleSign + "</ModId>"
					+ "<SessionId>1</SessionId>"
				+ "</MsgHead>"
				+ "<MsgBody>"
					+ "<TiltCtrlReq>"
						+ "<ConfID type=\"string\">#{ConfID}</ConfID>"
						+ "<StrmId type=\"int\">#{StrmId}</StrmId>"
						+ "<TiltType type=\"int\">#{TiltType}</TiltType>"
						+ "<Speed type=\"int\">#{Speed}</Speed>"
						+ "<Preset type=\"int\">#{Preset}</Preset>"
						+ "<CamAddr type=\"int\">#{CamAddr}</CamAddr>"
					+ "</TiltCtrlReq>"
				+ "</MsgBody>"
			+ "</ReqMsg>";	
	}
}
