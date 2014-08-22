package com.santrong.tcp.client;

import com.santrong.system.Global;
import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31009 extends LocalTcpBase {
	
	// 返回值
	private int resultCode;
	private int confMax;
	private int confInUse;
	private int uniVodMax;
	private int uniCur;
	private int vodCur;
	private int freePcent;
	private int freeSize;// 单位MB
	private String model;
	private String serialNo;
	
	public int getResultCode() {
		return resultCode;
	}

	public int getConfMax() {
		return confMax;
	}

	public int getConfInUse() {
		return confInUse;
	}

	public int getUniVodMax() {
		return uniVodMax;
	}

	public int getUniCur() {
		return uniCur;
	}

	public int getVodCur() {
		return vodCur;
	}

	public int getFreePcent() {
		return freePcent;
	}

	public int getFreeSize() {
		return freeSize;
	}
	public String getModel() {
		return model;
	}

	public String getSerialNo() {
		return serialNo;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_GetResource).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/GetResResp/ResultCode").getText());
		this.confMax = Integer.parseInt(xml.find("/MsgBody/GetResResp/ConfMax").getText());
		this.confInUse = Integer.parseInt(xml.find("/MsgBody/GetResResp/ConfInUse").getText());
		this.uniVodMax = Integer.parseInt(xml.find("/MsgBody/GetResResp/UniVodMax").getText());
		this.uniCur = Integer.parseInt(xml.find("/MsgBody/GetResResp/UniCur").getText());
		this.vodCur = Integer.parseInt(xml.find("/MsgBody/GetResResp/VodCur").getText());
		this.freePcent = Integer.parseInt(xml.find("/MsgBody/GetResResp/SpaceInfo/FreePcent").getText());
		this.freeSize = Integer.parseInt(xml.find("/MsgBody/GetResResp/SpaceInfo/FreeSize").getText());
		this.model = xml.find("/MsgBody/GetResResp/Model").getText();
		this.serialNo = xml.find("/MsgBody/GetResResp/SerialNo").getText();
	}
}
