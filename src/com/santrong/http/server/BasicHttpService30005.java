package com.santrong.http.server;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.tcp.client.LocalTcp31012;
import com.santrong.tcp.client.TcpService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30005 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		LocalTcp31012 tcp = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			
			TcpService client = TcpService.getInstance();
			tcp = new LocalTcp31012();
			tcp.setConfId(xml.find("/MsgBody/TiltCtrlReq/ConfID").getText());
			tcp.setStrmId(Integer.parseInt(xml.find("/MsgBody/TiltCtrlReq/StrmId").getText()));
			tcp.setTiltType(Integer.parseInt(xml.find("/MsgBody/TiltCtrlReq/TiltType").getText()));
			tcp.setSpeed(Integer.parseInt(xml.find("/MsgBody/TiltCtrlReq/Speed").getText()));
			tcp.setCamAddr(Integer.parseInt(xml.find("/MsgBody/TiltCtrlReq/CamAddr").getText()));
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
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_TiltCtrl).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<TiltCtrlResp>");
				sb.append("</TiltCtrlResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

}
