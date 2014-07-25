package com.santrong.http.server;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.system.network.NetworkInfo;
import com.santrong.system.network.SystemUtils;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30003 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		NetworkInfo lan = null;
		NetworkInfo wan = null;
		int rt = 0;
		String sessionId = "";
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			lan = SystemUtils.getNetwork(NetworkInfo.Type_Lan);
			wan = SystemUtils.getNetwork(NetworkInfo.Type_Wan);
		}catch(Exception e) {
			Log.printStackTrace(e);
			rt = 1;
		}
		
		if(lan == null) {
			lan = new NetworkInfo();
		}
		
		if(wan == null) {
			wan = new NetworkInfo();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(HttpDefine.Xml_Header);
		sb.append("<ResMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_GetNetwork).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<GetNetworkResp>");
					sb.append("<Lan>");
						sb.append("<Ip type=\"string\">").append(lan.getIp()).append("</Ip>");
						sb.append("<Mask type=\"string\">").append(lan.getMask()).append("</Mask>");
						sb.append("<Gateway type=\"string\">").append(lan.getGateway()).append("</Gateway>");
					sb.append("</Lan>");
					sb.append("<Wan>");
						sb.append("<Ip type=\"string\">").append(wan.getIp()).append("</Ip>");
						sb.append("<Mask type=\"string\">").append(wan.getMask()).append("</Mask>");
						sb.append("<Gateway type=\"string\">").append(wan.getGateway()).append("</Gateway>");
					sb.append("</Wan>");					
				sb.append("</GetNetworkResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

}
