package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31005 implements AbstractTcpClient {
	
	private List<String> confIdList = new ArrayList<String>();
	

	public List<String> getConfIdList() {
		return confIdList;
	}

	public void setConfIdList(List<String> confIdList) {
		this.confIdList = confIdList;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_StopConfRecord).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<StopConfRcdReq>");
					for(String id : confIdList) {
						sb.append("<ConfID type=\"string\">").append(id).append("</ConfID>");
					}
				sb.append("</StopConfRcdReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
	return sb.toString();
	}

	@Override
	public void resolveXml(String repXml) {
		// TODO Auto-generated method stub
		
	}
}
