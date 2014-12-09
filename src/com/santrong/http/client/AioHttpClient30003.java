package com.santrong.http.client;

import com.santrong.http.HttpDefine;
import com.santrong.http.client.base.AioHttpBase;
import com.santrong.tcp.TcpDefine;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年12月4日
 * @time 上午10:24:49
 */
public class AioHttpClient30003 extends AioHttpBase {
	private String remoteId;
	private int status;
	
	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Aio_Client_UpdateFileStatus).append("</MsgCode>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<UpdateFileStatusReq>");
					sb.append("<RemoteId type=\"string\">").append(this.remoteId).append("</RemoteId>");
					sb.append("<Status type=\"int\">").append(this.status).append("</Status>");
				sb.append("</UpdateFileStatusReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
