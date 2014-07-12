package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31012 implements AbstractTcpClient {
	private String confId;
	private int strmId;
	private int tiltTpe;
	private int speed;
	private int preset;
	private int camAddr;

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public int getStrmId() {
		return strmId;
	}

	public void setStrmId(int strmId) {
		this.strmId = strmId;
	}

	public int getTiltTpe() {
		return tiltTpe;
	}

	public void setTiltTpe(int tiltTpe) {
		this.tiltTpe = tiltTpe;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPreset() {
		return preset;
	}

	public void setPreset(int preset) {
		this.preset = preset;
	}

	public int getCamAddr() {
		return camAddr;
	}

	public void setCamAddr(int camAddr) {
		this.camAddr = camAddr;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_TiltCtrl).append("</MsgCode>");
				sb.append("<ModId>").append(TcpDefine.ModuleSign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<TiltCtrlReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<StrmId type=\"int\">").append(this.strmId).append("</StrmId>");
					sb.append("<TiltType type=\"int\">").append(this.tiltTpe).append("</TiltType>");
					sb.append("<Speed type=\"int\">").append(this.speed).append("</Speed>");
					sb.append("<Preset type=\"int\">").append(this.preset).append("</Preset>");
					sb.append("<CamAddr type=\"int\">").append(this.camAddr).append("</CamAddr>");
				sb.append("</TiltCtrlReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(String repXml) {
		// TODO Auto-generated method stub
		
	}
}
