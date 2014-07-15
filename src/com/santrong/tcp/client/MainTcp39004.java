package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.MainTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class MainTcp39004 extends MainTcpBase{
	
	private int resultCode;
	private List<ModuleStatus> moduleList = new ArrayList<ModuleStatus>();
	
	public List<ModuleStatus> getModuleList() {
		return moduleList;
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
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_GetModInfo).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void resolveXml(XmlReader xml) {

		this.resultCode = Integer.parseInt(xml.find("/MsgBody/GetModInfoResp/ResultCode").getText());
		List<Element> version = xml.find("/MsgBody/GetModInfoResp/Version").getChildren();
		for(Element e : version) {
			ModuleStatus module = new ModuleStatus();
			module.name = e.getName();
			module.version = e.getText();
			
			Element e_state = xml.find("/MsgBody/GetModInfoResp/State/" + module.name);
			if(e_state != null) {
				module.state = Integer.parseInt(e_state.getText());
			}else {
				module.state = 0;
			}
			
			moduleList.add(module);
		}
	}
	
	public class ModuleStatus {
		private String name;
		private String version;
		private int state;
		public String getName() {
			return name;
		}
		public String getVersion() {
			return version;
		}
		public int getState() {
			return state;
		}
	}

}
