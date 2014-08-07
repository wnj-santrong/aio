package com.santrong.http.server;

import java.util.Date;

import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService99999 implements AbstractHttpService{
//	public static final int  EVENT_RCDFINISH			=	66001; // 录制完成
//	public static final int  EVENT_SPACEINFO 			=	66002; // 磁盘空间
//	public static final int  EVENT_RESINFO 				=	66003; // 资源使用情况
//	public static final int  EVENT_VODUSR 				=	66004; // 点播用户数
//	public static final int  EVENT_UNIUSR 				=	66005; // 直播用户数
//	public static final int  EVENT_SRCSTATE 			=	66006; // 连接状态
	
	//接收参数
	private Event66001 event66001;
	private Event66002 event66002;
	private Event66003 event66003;
	private Event66004 event66004;
	private Event66005 event66005;
	private Event66006 event66006;

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			int eventId = Integer.parseInt(xml.find("/MsgBody/ConfEvent/EventId").getText());
			
			switch(eventId) {
			case 66001 :
				this.event66001 = new Event66001();
				this.event66001.confId = xml.find("/MsgBody/ConfEvent/RcdFinish/ConfId").getText();
				this.event66001.reasonId = xml.find("/MsgBody/ConfEvent/RcdFinish/ReasonId").getText();
				this.event66001.fileUrl = xml.find("/MsgBody/ConfEvent/RcdFinish/FileUrl").getText();
				this.event66001.rcdTime = xml.find("/MsgBody/ConfEvent/RcdFinish/RcdTime").getText();
				this.event66001.rcdSize = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdFinish/RcdSize").getText());
				this.event66001.rcdType = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdFinish/RcdType").getText());
				
				// 更新内存状态
				RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(this.event66001.confId);
				roomStatus.setIsRecord(0);
				StatusMgr.setRoomStatus(this.event66001.confId, roomStatus);
				
				// 更新课件状态
				FileDao fileDao = new FileDao();
				
				String[] arr = this.event66001.fileUrl.split("/");
				FileItem file = fileDao.selectByFileName(arr[arr.length - 1]);
				file.setDuration(this.event66001.rcdTime);
				file.setUts(new Date());
				file.setStatus(FileItem.File_Status_Recorded);
				if(this.event66001.rcdType == MeetingItem.Record_Type_CMPS) {
					file.setFileSize(this.event66001.rcdSize * 1024 * 1024);
				}else if(this.event66001.rcdType == MeetingItem.Record_Type_RSRC) {
					file.setFileSize(this.event66001.rcdSize * 1024 * 1024);
				}else if(this.event66001.rcdType == MeetingItem.Record_Type_MV) {
					file.setFileSize(this.event66001.rcdSize * 1024 * 1024);
				}
				
				if(fileDao.update(file) <= 0) {
					Log.warn("");
					rt = 1;
				}
				
				Log.logOpt("meeting-record", "stop", "event99999", "127.0.0.1");	
				
				break;
				
			case 66002 :
				this.event66002 = new Event66002();
				this.event66002.freePercent = Integer.parseInt(xml.find("/MsgBody/ConfEvent/SpaceInfo/FreePercent").getText());
				this.event66002.freeSize = Integer.parseInt(xml.find("/MsgBody/ConfEvent/SpaceInfo/FreeSize").getText());
				break;
				
			case 66003 :
				this.event66003 = new Event66003();
				this.event66003.confMax = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdResInfo/ConfMax").getText());
				this.event66003.confInUse = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdResInfo/ConfInUse").getText());
				this.event66003.uniVodMax = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdResInfo/UniVodMax").getText());
				this.event66003.uniCur = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdResInfo/UniCur").getText());
				this.event66003.vodCur = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdResInfo/VodCur").getText());			
				break;
				
			case 66004 :
				this.event66004 = new Event66004();
				this.event66004.usrCount = Integer.parseInt(xml.find("/MsgBody/ConfEvent/VodUsrCount/UsrCount").getText());
				StatusMgr.VodUsrCount = this.event66004.usrCount;
				break;
				
			case 66005 :
				this.event66005 = new Event66005();
				this.event66005.usrCount = Integer.parseInt(xml.find("/MsgBody/ConfEvent/UniUsrCount/UsrCount").getText());
				StatusMgr.UniUsrCount = this.event66005.usrCount;
				break;
				
			case 66006 :
				this.event66006 = new Event66006();
				this.event66006.connState = xml.find("/MsgBody/ConfEvent/SrcConnState/ConnState").getText();
				break;
			}
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			rt = 1;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(HttpDefine.Xml_Header);
		sb.append("<ResMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_ConfEvent).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<ConfEventResp>");
				sb.append("</ConfEventResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private class Event66001 {
		private String confId;
		private String reasonId;
		private String fileUrl;
		private String rcdTime;
		private int rcdSize;// 单位MB
		private int rcdType;
	}
	
	@SuppressWarnings("unused")
	private class Event66002 {
		private int freePercent;
		private int freeSize;// 单位MB
	}
	
	@SuppressWarnings("unused")
	private class Event66003 {
		private int confMax;
		private int confInUse;
		private int uniVodMax;
		private int uniCur;
		private int vodCur;
	}
	
	private class Event66004 {
		private int usrCount;
	}
	
	private class Event66005 {
		private int usrCount;
	}
	
	@SuppressWarnings("unused")
	private class Event66006 {
		private String connState;// 1连接，0断开
	}

}
