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
public class AioHttpClient30001 extends AioHttpBase {

	private String username;
	private String password;
	
	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Aio_Client_Login).append("</MsgCode>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<LoginReq>");
					sb.append("<Username type=\"string\">").append(this.username).append("</Username>");
					sb.append("<Pwdmd5 type=\"string\">").append(this.password).append("</Pwdmd5>");
				sb.append("</LoginReq>");
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

	public void setPassword(String password) {
		this.password = password;
	}
}
