package com.santrong.http.server;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.tcp.client.MainTcp39004;
import com.santrong.tcp.client.MainTcp39004.ModuleStatus;
import com.santrong.tcp.client.TcpService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30002 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		MainTcp39004 tcp = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			TcpService client = TcpService.getInstance();
			tcp = new MainTcp39004();
			client.request(tcp);
			
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
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_GetModInfo).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<GetModInfoResp>");
					sb.append("<Version>");
					for(ModuleStatus module : tcp.getModuleList()) {
						sb.append("<").append(module.getName()).append(" type=\"string\">").append(module.getVersion()).append("</").append(module.getName()).append(">");
					}
					sb.append("</Version>");
					sb.append("<State>");
					for(ModuleStatus module : tcp.getModuleList()) {
						sb.append("<").append(module.getName()).append(" type=\"int\">").append(module.getState()).append("</").append(module.getName()).append(">");
					}					
					sb.append("</State>");
				sb.append("</GetModInfoResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

}
