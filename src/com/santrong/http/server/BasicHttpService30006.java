package com.santrong.http.server;

import java.util.List;

import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30006 implements AbstractHttpService{

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		MeetingItem meeting = null;
		RoomStatusEntry roomStatus = null;
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			String confId = xml.find("/MsgBody/GetMtInfoReq/ConfID").getText();
			
			roomStatus = StatusMgr.getRoomStatus(confId);
			
			// 从数据库获取会议配置
			String[] arr = confId.split("_");
			int channel = Integer.parseInt(arr[1]);
			MeetingDao meetingDao = new MeetingDao();
			meeting = meetingDao.selectByChannel(channel);
			
			// ds获取
			DatasourceDao dsDao = new DatasourceDao();
			List<DatasourceItem> dsList = dsDao.selectByMeetingId(meeting.getId());
			if(dsList != null) {
				meeting.setDsList(dsList);
			}
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			rt = 1;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(HttpDefine.Xml_Header);
		sb.append("<ResMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_GetMtInfo).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<GetMtInfoResp>");
				if(meeting != null) {
					sb.append("<IsLive type=\"int\">").append(roomStatus.getIsLive()).append("</IsLive>");
					sb.append("<IsRecord type=\"int\">").append(roomStatus.getIsRecord()).append("</IsRecord>");
					sb.append("<RecordType type=\"int\">").append(MeetingItem.Record_Type_CMPS).append("</RecordType>");
					sb.append("<Layout type=\"int\">").append(meeting.getRecordMode()).append("</Layout>");
					sb.append("<bScale type=\"int\">").append(MeetingItem.Bscale_Extend).append("</bScale>");
					sb.append("<CourseName type=\"string\">").append(meeting.getCourseName()).append("</CourseName>");
					sb.append("<CourseAbs type=\"string\">").append(meeting.getRemark()).append("</CourseAbs>");
					sb.append("<Teacher type=\"string\">").append(meeting.getTeacher()).append("</Teacher>");
					sb.append("<RcdStreamInfoArray>");
					for(int i=0;i<meeting.getDsList().size();i++) {
						DatasourceItem item  = meeting.getDsList().get(i);
						sb.append("<RcdStreamInfo>");
							sb.append("<StrmAddr type=\"string\">").append(item.getAddr()).append("</StrmAddr>");
							sb.append("<StrmPort type=\"int\">").append(item.getPort()).append("</StrmPort>");
							sb.append("<StrmUser type=\"string\">").append(item.getUsername()).append("</StrmUser>");
							sb.append("<StrmPW type=\"string\">").append(item.getPassword()).append("</StrmPW>");
							sb.append("<StrmType type=\"int\">").append(item.getDsType()).append("</StrmType>");
							sb.append("<StrmBandwidth type=\"int\">").append(meeting.getBitRate()).append("</StrmBandwidth>");
							sb.append("<StrmFmt type=\"int\">").append(meeting.getResolution()).append("</StrmFmt>");
							sb.append("<StrmFRate type=\"int\">").append(25).append("</StrmFRate>");
							if(i == 0) {// 第一路添加音频
								sb.append("<AudSmpRate type=\"int\">").append(48).append("</AudSmpRate>");
								sb.append("<AudCh type=\"int\">").append(2).append("</AudCh>");
								sb.append("<AudBitrate type=\"int\">").append(64).append("</AudBitrate>");
							}
						sb.append("</RcdStreamInfo>");
					}
					sb.append("</RcdStreamInfoArray>");
				}
				sb.append("</GetMtInfoResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}

}
