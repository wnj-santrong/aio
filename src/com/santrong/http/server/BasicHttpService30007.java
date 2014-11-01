package com.santrong.http.server;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31013;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30007 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		LocalTcp31013 tcp = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			
			TcpClientService client = TcpClientService.getInstance();
			tcp = new LocalTcp31013();
			tcp.setConfId(xml.find("/MsgBody/DirectCtrlReq/ConfID").getText());
			tcp.setLayout(Integer.parseInt(xml.find("/MsgBody/DirectCtrlReq/Layout").getText()));
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				// 修改内存状态
				RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(tcp.get_confId());
				if(roomStatus != null) {
					roomStatus.setLayout(tcp.get_layout());
				}
				
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
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_SetDirect).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<DirectCtrlResp>");
					sb.append("<ConfID type=\"string\">").append(tcp.get_confId()).append("</ConfID>");
					sb.append("<ResultCode type=\"int\">").append(tcp.getResultCode()).append("</ResultCode>");
					sb.append("<Layout type=\"int\">").append(tcp.get_layout()).append("</Layout>");
				sb.append("</DirectCtrlResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

}
