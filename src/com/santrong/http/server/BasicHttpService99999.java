package com.santrong.http.server;

import java.util.Date;

import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.SystemUpdateService;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.system.tip.TipItem;
import com.santrong.system.tip.TipService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService99999 implements AbstractHttpService{
//	public static final int  EVENT_RCDOVER				=	66001; // 单路录制结束，发生异常才会接收到这个消息
//	public static final int  EVENT_SPACEINFO 			=	66002; // 磁盘空间
//	public static final int  EVENT_RESINFO 				=	66003; // 资源使用情况
//	public static final int  EVENT_VODUSR 				=	66004; // 点播用户数
//	public static final int  EVENT_UNIUSR 				=	66005; // 直播用户数
//	public static final int  EVENT_SRCSTATE 			=	66006; // 连接状态
//	public static final int  EVENT_RCDFINISH 			=	66007; // 正常录制结束，会接收到这个消息
//	public static final int  EVENT_UPDATESTATE 			=	66008; // 系统升级状态上报
	
	//接收参数
	private Event66001 event66001;
	private Event66002 event66002;
	private Event66003 event66003;
	private Event66004 event66004;
	private Event66005 event66005;
	private Event66006 event66006;
	private Event66007 event66007;
	private Event66008 event66008;

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		RoomStatusEntry roomStatus = null;
		FileDao fileDao = null;
		FileItem file = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			int eventId = Integer.parseInt(xml.find("/MsgBody/ConfEvent/EventId").getText());
			
			switch(eventId) {
			case 66001 :
				// 目前一期只有一路，肯定收不到66001，指挥收到66007
				this.event66001 = new Event66001();
				this.event66001.confId = xml.find("/MsgBody/ConfEvent/RcdFinish/ConfID").getText();
				this.event66001.reasonId = xml.find("/MsgBody/ConfEvent/RcdFinish/ReasonID").getText();
				this.event66001.fileUrl = xml.find("/MsgBody/ConfEvent/RcdFinish/FileURL").getText();
				this.event66001.rcdTime = xml.find("/MsgBody/ConfEvent/RcdFinish/RcdTime").getText();
				this.event66001.rcdSize = Long.parseLong(xml.find("/MsgBody/ConfEvent/RcdFinish/RcdSize").getText());
				this.event66001.rcdType = Integer.parseInt(xml.find("/MsgBody/ConfEvent/RcdFinish/RcdType").getText());
				break;
				
			case 66002 :
				this.event66002 = new Event66002();
				this.event66002.freePercent = Integer.parseInt(xml.find("/MsgBody/ConfEvent/SpaceInfo/FreePercent").getText());
				this.event66002.freeSize = Long.parseLong(xml.find("/MsgBody/ConfEvent/SpaceInfo/FreeSize").getText());// 单位MB
				TipItem tip = new TipItem();
				tip.setTitle("tip_disk_lack_title");
				tip.setContent("tip_disk_lack_content");
				tip.setOther(1);//0是少，1是不足
				TipService tipService = new TipService();
				tipService.setTip(TipService.Disk_Lack, tip);
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
				
			case 66007 : 
				this.event66007 = new Event66007();
				this.event66007.confId = xml.find("/MsgBody/ConfEvent/ConfRcdFinish/ConfID").getText();
				this.event66007.reasonId = xml.find("/MsgBody/ConfEvent/ConfRcdFinish/ReasonID").getText();
				this.event66007.fileUrl = xml.find("/MsgBody/ConfEvent/ConfRcdFinish/FileURL").getText();
				this.event66007.rcdTime = xml.find("/MsgBody/ConfEvent/ConfRcdFinish/RcdTime").getText();
				this.event66007.RSRCRcdSize = Long.parseLong(xml.find("/MsgBody/ConfEvent/ConfRcdFinish/RSRCRcdSize").getText());
				this.event66007.MVRcdSize = Long.parseLong(xml.find("/MsgBody/ConfEvent/ConfRcdFinish/MVRcdSize").getText());
				this.event66007.CMPSRcdSize = Long.parseLong(xml.find("/MsgBody/ConfEvent/ConfRcdFinish/CMPSRcdSize").getText());
				this.event66007.rcdType = Integer.parseInt(xml.find("/MsgBody/ConfEvent/ConfRcdFinish/RcdType").getText());
				
				// 更新内存状态
				roomStatus = StatusMgr.getRoomStatus(this.event66007.confId);
				roomStatus.setIsRecord(0);
				StatusMgr.setRoomStatus(this.event66007.confId, roomStatus);
				
				// 更新课件状态
				fileDao = new FileDao();
				
				String[] arr2 = this.event66007.fileUrl.split("/");
				file = fileDao.selectByFileName(arr2[arr2.length - 1]);
				file.setDuration(this.event66007.rcdTime);
				file.setUts(new Date());
				file.setStatus(FileItem.File_Status_Recorded);
				if(this.event66007.rcdType == MeetingItem.Record_Type_CMPS) {
					file.setFileSize(this.event66007.CMPSRcdSize * 1024 * 1024);
				}else if(this.event66007.rcdType == MeetingItem.Record_Type_RSRC) {
					file.setFileSize(this.event66007.RSRCRcdSize * 1024 * 1024);
				}else if(this.event66007.rcdType == MeetingItem.Record_Type_MV) {
					file.setFileSize(this.event66007.MVRcdSize * 1024 * 1024);
				}
				
				if(fileDao.update(file) <= 0) {
					Log.mark("--update file status to finish fail, fileName:" + file.getFileName());
					rt = 1;
				}
				
				Log.logOpt("meeting-record", "stop", "event99999", "127.0.0.1");	
			
				break;
				
			case 66008 :
				this.event66008 = new Event66008();
				this.event66008.percent = Integer.parseInt(xml.find("/MsgBody/ConfEvent/UpdateState/Percent").getText());
				SystemUpdateService.updatePercent = this.event66008.percent;
				if(SystemUpdateService.updatePercent == 100) {
					SystemUpdateService.updating = false;
					SystemUpdateService.updateResult = "success";
				}
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
		private long rcdSize;// 单位MB
		private int rcdType;
	}
	
	@SuppressWarnings("unused")
	private class Event66002 {
		private int freePercent;
		private long freeSize;// 单位MB
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
	
	@SuppressWarnings("unused")
	private class Event66007 {
		private String confId;
		private String reasonId;
		private String fileUrl;
		private String rcdTime;
		private long RSRCRcdSize;// 单位MB
		private long MVRcdSize;// 单位MB
		private long CMPSRcdSize;// 单位MB
		private int rcdType;
	}
	
	private class Event66008 {
		private int percent;// 升级进度百分比
	}

}
