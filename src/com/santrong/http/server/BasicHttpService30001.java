package com.santrong.http.server;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.tcp.client.LocalTcp31009;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30001 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		LocalTcp31009 tcp31009 = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			TcpClientService client = TcpClientService.getInstance();
			tcp31009 = new LocalTcp31009();
			client.request(tcp31009);
			
			if(tcp31009.getRespHeader().getReturnCode() == 1 || tcp31009.getResultCode() == 1) {
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
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_GetResource).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<GetResourceResp>");
					sb.append("<ConfMax type=\"int\">").append(tcp31009.getConfMax()).append("</ConfMax>");
					sb.append("<ConfInUse type=\"int\">").append(tcp31009.getConfInUse()).append("</ConfInUse>");
					sb.append("<UniVodMax type=\"int\">").append(tcp31009.getUniVodMax()).append("</UniVodMax>");
					sb.append("<UniCur type=\"int\">").append(tcp31009.getUniCur()).append("</UniCur>");
					sb.append("<VodCur type=\"int\">").append(tcp31009.getVodCur()).append("</VodCur>");
					sb.append("<SpaceInfo>");
						sb.append("<FreePcent type=\"int\">").append(tcp31009.getFreePcent()).append("</FreePcent>");
						sb.append("<FreeSize type=\"int\">").append(tcp31009.getFreeSize()).append("</FreeSize>");
					sb.append("</SpaceInfo>");
					sb.append("<Model type=\"string\">").append(tcp31009.getModel()).append("</Model>");
					sb.append("<SerialNo type=\"string\">").append(tcp31009.getSerialNo()).append("</SerialNo>");
				sb.append("</GetResourceResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();	
	}

}
