package com.santrong.http.server;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.tcp.client.LocalTcp31017;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30008 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		LocalTcp31017 tcp = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			
			TcpClientService client = TcpClientService.getInstance();
			tcp = new LocalTcp31017();
			
			tcp.setConfId(xml.find("/MsgBody/UrlInfoReq/ConfID").getText());
			tcp.setbPushStream(Integer.parseInt(xml.find("/MsgBody/UrlInfoReq/bPushStream").getText()));
			tcp.setUrl(xml.find("/MsgBody/UrlInfoReq/Url").getText());
			client.request(tcp);
			
			// 失败
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				rt = 1;
			}
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			rt = 1;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(HttpDefine.Xml_Header);
		sb.append("<ResMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_SetUrlInfo).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<UrlInfoResp>");
				sb.append("</UrlInfoResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

}
