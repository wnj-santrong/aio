package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31008 implements AbstractTcpClient {
	
	private int freeSize;
	private int maxTime;
	
	
	public int getFreeSize() {
		return freeSize;
	}

	public void setFreeSize(int freeSize) {
		this.freeSize = freeSize;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_SetThreshold).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<Config>");
					sb.append("<FreeSize type=\"int\">").append(this.freeSize).append("</FreeSize>");
					sb.append("<MaxTime type=\"int\">").append(this.maxTime).append("</MaxTime>");
				sb.append("</Config>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(String repXml) {
		// TODO Auto-generated method stub
		
	}
}
