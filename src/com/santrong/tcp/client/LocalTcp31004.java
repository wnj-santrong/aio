package com.santrong.tcp.client;

import java.util.ArrayList;
import java.util.List;

import com.santrong.system.Global;
import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcp31004 extends LocalTcpBase {
	
	private String confId;
	private int isLive;
	private int recordType;// 1资源，2电影，4合成
	private int layout;
	private int bScale;// 1拉伸，0不拉伸

	private List<RecStreamInfo> recStreamInfoList = new ArrayList<RecStreamInfo>();

	// 返回值
	private int resultCode;
	private String _confId;
	private int doRcd;
	private int doUni;
	private String _rcdName;
	
	public void setConfId(String confId) {
		this.confId = confId;
	}

	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}
	
	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public void setbScale(int bScale) {
		this.bScale = bScale;
	}

	public void setRecStreamInfoList(List<RecStreamInfo> recStreamInfoList) {
		this.recStreamInfoList = recStreamInfoList;
	}

	
	
	public int getResultCode() {
		return resultCode;
	}

	public String get_confId() {
		return _confId;
	}

	public int getDoRcd() {
		return doRcd;
	}

	public int getDoUni() {
		return doUni;
	}

	public String get_rcdName() {
		return _rcdName;
	}

	@Override
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		sb.append(TcpDefine.Xml_Header);
		sb.append("<ReqMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(TcpDefine.Basic_Client_StartConfRecord).append("</MsgCode>");
				sb.append("<ModId>").append(Global.Module_Sign).append("</ModId>");
				sb.append("<SessionId>1</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<StartConfRecordReq>");
					sb.append("<ConfID type=\"string\">").append(this.confId).append("</ConfID>");
					sb.append("<IsLive type=\"int\">").append(this.isLive).append("</IsLive>");
					sb.append("<RecordType type=\"int\">").append(this.recordType).append("</RecordType>");
					sb.append("<Layout type=\"int\">").append(this.layout).append("</Layout>");
					sb.append("<bScale type=\"int\">").append(this.bScale).append("</bScale>");
					sb.append("<RcdStreamInfoArray>");
						for(RecStreamInfo item : recStreamInfoList) {
							sb.append("<RcdStreamInfo>");
								sb.append("<StrmAddr type=\"string\">").append(item.strmAddr).append("</StrmAddr>");
								sb.append("<StrmPort type=\"int\">").append(item.strmPort).append("</StrmPort>");
								sb.append("<StrmUser type=\"string\">").append(item.StrmUser).append("</StrmUser>");
								sb.append("<StrmPW type=\"string\">").append(item.StrmPw).append("</StrmPW>");
								sb.append("<StrmType type=\"int\">").append(item.strmType).append("</StrmType>");
								sb.append("<StrmBandwidth type=\"int\">").append(item.strmBandwidth).append("</StrmBandwidth>");
								sb.append("<StrmFmt type=\"int\">").append(item.strmFmt).append("</StrmFmt>");
								sb.append("<StrmFRate type=\"int\">").append(item.strmFRate).append("</StrmFRate>");
								if(item.hasAud == 1) {
									sb.append("<AudSmpRate type=\"int\">").append(item.audSmpRate).append("</AudSmpRate>");
									sb.append("<AudCh type=\"int\">").append(item.audCh).append("</AudCh>");
									sb.append("<AudBitrate type=\"int\">").append(item.audBitrate).append("</AudBitrate>");
								}
							sb.append("</RcdStreamInfo>");
						}
					sb.append("</RcdStreamInfoArray>");
				sb.append("</StartConfRecordReq>");
			sb.append("</MsgBody>");
		sb.append("</ReqMsg>");
		return sb.toString();
	}

	@Override
	public void resolveXml(XmlReader xml) {
		this.resultCode = Integer.parseInt(xml.find("/MsgBody/StartConfRcdResp/ResultCode").getText());
		this._confId = xml.find("/MsgBody/StartConfRcdResp/ConfID").getText();
		this.doRcd = Integer.parseInt(xml.find("/MsgBody/StartConfRcdResp/DoRcd").getText());
		this.doUni = Integer.parseInt(xml.find("/MsgBody/StartConfRcdResp/DoUni").getText());
		this._rcdName = xml.find("/MsgBody/StartConfRcdResp/RcdName").getText();
		
	}
	
	public class RecStreamInfo{
		private String strmAddr;
		private int strmPort;
		private String StrmUser;
		private String StrmPw;
		private int strmType;// 类型	1VGA和0摄像头
		private int strmBandwidth;// 码率
		private int strmFmt;// 格式（宽高） 720P这类的
		private int strmFRate;// 帧率		一般填25
		//---------------------------以下是音频，如果没有请不要发送
		private int audSmpRate;// 声音采样率
		private int audCh;// 声道
		private int audBitrate;// 声音码率 单位k，一般传递64即可
		
		private int hasAud; // 是否有音频
		
		public void setStrmAddr(String strmAddr) {
			this.strmAddr = strmAddr;
		}
		public void setStrmPort(int strmPort) {
			this.strmPort = strmPort;
		}
		public void setStrmUser(String strmUser) {
			StrmUser = strmUser;
		}
		public void setStrmPw(String strmPw) {
			StrmPw = strmPw;
		}
		public void setStrmType(int strmType) {
			this.strmType = strmType;
		}
		public void setStrmBandwidth(int strmBandwidth) {
			this.strmBandwidth = strmBandwidth;
		}
		public void setStrmFmt(int strmFmt) {
			this.strmFmt = strmFmt;
		}
		public void setStrmFRate(int strmFRate) {
			this.strmFRate = strmFRate;
		}
		public void setAudSmpRate(int audSmpRate) {
			this.audSmpRate = audSmpRate;
		}
		public void setAudCh(int audCh) {
			this.audCh = audCh;
		}
		public void setAudBitrate(int audBitrate) {
			this.audBitrate = audBitrate;
		}
		public void setHasAud(int hasAud) {
			this.hasAud = hasAud;
		}
	}

}
