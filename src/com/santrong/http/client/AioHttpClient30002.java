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
public class AioHttpClient30002 extends AioHttpBase {
	private String username;
	private String title;
	private long fileSize;
	private String duration;
	private String remoteId;
	
	@Override
	public String toXml() {
		
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Aio_Client_AddFile).append("</MsgCode>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<AddFileReq>");
					sb.append("<Username type=\"string\">").append(this.username).append("</Username>");
					sb.append("<Title type=\"string\">").append(this.title).append("</Title>");
					sb.append("<FileSize type=\"long\">").append(this.fileSize).append("</FileSize>");
					sb.append("<Duration type=\"string\">").append(this.duration).append("</Duration>");
					sb.append("<RemoteId type=\"string\">").append(this.remoteId).append("</RemoteId>");
				sb.append("</AddFileReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setRemoteId(String remoteId) {
		this.remoteId = remoteId;
	}
}
