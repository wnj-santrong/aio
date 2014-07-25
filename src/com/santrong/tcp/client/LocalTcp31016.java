package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.santrong.system.Global;
import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31016 extends LocalTcpBase {
	private List<String> srcAddrList = new ArrayList<String>();
	
	// 返回值
	private int resultCode;
	private List<SrcState> srcStateList = new ArrayList<SrcState>();

	
	public void setSrcAddrList(List<String> srcAddrList) {
		this.srcAddrList = srcAddrList;
	}

	public int getResultCode() {
		return resultCode;
	}
	
	public List<SrcState> getSrcStateList() {
		return srcStateList;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_GetSourceState).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<GetSrcStateReq>");
					sb.append("<SrcArray>");
						for(String s : srcAddrList) {
						sb.append("<SrcAddr type=\"string\">").append(s).append("</SrcAddr>");
						}
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
			item.state = Integer.parseInt(el.getAttribute("state").getValue());
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
