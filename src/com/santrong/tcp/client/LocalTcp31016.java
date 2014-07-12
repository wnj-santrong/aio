package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcp;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31016 extends AbstractTcp {
	private String srcAddr;
	
	// 返回值
	private int resultCode;
	private List<SrcState> srcStateList = new ArrayList<SrcState>();

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
	}
	
	public int getResultCode() {
		return resultCode;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_GetSourceState).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<GetSrcStateReq>");
					sb.append("<SrcArray>");
						sb.append("<SrcAddr type=\"string\">").append(this.srcAddr).append("</SrcAddr>");
					sb.append("</SrcArray>");
				sb.append("</GetSrcStateReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/GetSrcStateResp/ResultCode").getText());
		List<Element> elementList = xml.finds("/MsgBody/GetSrcStateResp/SrcStateArray/SrcAddr");
		for(Element el : elementList) {
			SrcState item = new SrcState();
			
			item.addr = el.getText();
			//TODO 属性如何拿state
			
			srcStateList.add(item);
		}
		
	}
	
	public class SrcState{
		private String addr;
		private int state;
		public String getAddr() {
			return addr;
		}
		public int getState() {
			return state;
		}
	}
}
