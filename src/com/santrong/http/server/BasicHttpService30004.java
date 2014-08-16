package com.santrong.http.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.mysql.jdbc.StringUtils;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.http.HttpDefine;
import com.santrong.http.server.base.AbstractHttpService;
import com.santrong.log.Log;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.DirDefine;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31004;
import com.santrong.tcp.client.LocalTcp31004.RecStreamInfo;
import com.santrong.tcp.client.LocalTcp31005;
import com.santrong.tcp.client.LocalTcp31006;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.CommonTools;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午8:35:02
 */
public class BasicHttpService30004 implements AbstractHttpService{
	
	TcpClientService client = TcpClientService.getInstance();

	@Override
	public String excute(XmlReader xml) {
		int rt = 0;
		String sessionId = "";
		
		try{
			sessionId = xml.find("/MsgHead/SessionId").getText();
			String confId = xml.find("/MsgBody/RecordCtlReq/ConfID").getText();
			int operType = Integer.parseInt(xml.find("/MsgBody/RecordCtlReq/OperType").getText());// <!-- 0：停止录制; 1：开始录制||继续录制; 2：暂停录制; -->
			int addOrUpdate = 0;//0不操作数据库，1数据库新增文件，2数据库更新文件
			
			// 状态校验
			RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
			if(roomStatus.getIsRecord() == 1 && operType == 1) {
				rt = 1;//已经有课件正在录制中,不能开启录制和继续录制
			}
			
			if(roomStatus.getIsRecord() == 0 && (operType == 0 || operType == 2)) {
				rt = 1;//如果没有课件在录制,不能开启停止和暂停
			}	
			
			// 状态改变之前判断数据库操作类型
			if(roomStatus.getIsRecord() == 0 && operType == 1) {// 当前没有在录制，发送指令1肯定会触发录制
				addOrUpdate = 1;
			}
			if(roomStatus.getIsRecord() == 1 && operType == 0) {// 当前正则录制，发送的指令是0停止
				addOrUpdate = 2;
			}
			
			if(rt != 1) {/*---------------------开会------------------------------*/
				// 从数据库获取会议配置
				String[] arr = confId.split("_");
				int channel = Integer.parseInt(arr[1]);
				MeetingDao meetingDao = new MeetingDao();
				MeetingItem meeting = meetingDao.selectByChannel(channel);
				
				// 先开会，如果有需要的话
				if(roomStatus.getIsLive() == 0 && operType == 1) {
					rt = openMeeting(meeting, confId);
				}
				
				if(rt != 1) {/*---------------------录制------------------------------*/
					// 前面已经校验状态，这里直接发送指令
					rt = doRecord(meeting, confId, operType, addOrUpdate);
				}
				
				if(rt != -1) {/*---------------------关会------------------------------*/
					// 如果是停止录制，当前正在开会，会议的开启是由面板触发的，则同时停止会议
					if(operType == 0 && roomStatus.getIsLive() == 1 && roomStatus.getLiveSource() == 1) {
						rt = closeMeeting(confId);
					}
				}
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
			rt = 1;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(HttpDefine.Xml_Header);
		sb.append("<ResMsg>");
			sb.append("<MsgHead>");
				sb.append("<MsgCode>").append(HttpDefine.Basic_Server_RecordCtl).append("</MsgCode>");
				sb.append("<ReturnCode>").append(rt).append("</ReturnCode>");
				sb.append("<SessionId>").append(sessionId).append("</SessionId>");
			sb.append("</MsgHead>");
			sb.append("<MsgBody>");
				sb.append("<RecordCtlResp>");
				sb.append("</RecordCtlResp>");
			sb.append("</MsgBody>");
		sb.append("</ResMsg>");
		return sb.toString();
	}
	
	/*
	 * 发送录制相关指令
	 */
	private int doRecord(MeetingItem meeting, String confId, int operType, int addOrUpdate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//时间作为标致，精确到秒
		String fileName = sdf.format(new Date());
		String rcdName = DirDefine.VedioDir + "/" + confId + "/" + fileName;//全路径		
		
		LocalTcp31006 tcp = new LocalTcp31006();
		tcp.setConfId(confId);
		tcp.setOperType(operType);
		if(StringUtils.isNullOrEmpty(meeting.getCourseName())) {
			tcp.setCourseName(fileName);
		}else{
			tcp.setCourseName(meeting.getCourseName());
		}
		tcp.setCourseAbs(meeting.getRemark());
		tcp.setTeacher(meeting.getTeacher());
		tcp.setRcdName(rcdName);
		client.request(tcp);
		
		if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
			return 1;
		}
		
		// 更新内存状态
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
		if(operType == 1) {
			roomStatus.setIsRecord(1);
		}else {
			roomStatus.setIsRecord(0);
		}
		StatusMgr.setRoomStatus(confId, roomStatus);
		
		/*---------------------数据库操作开始------------------------------*/
		
		if(addOrUpdate == 1) {
			// 新增文件
			FileDao fileDao = new FileDao();
			FileItem file = new FileItem();
			BeanUtils.copyProperties(meeting, file);
			file.setId(CommonTools.getGUID());
			file.setFileName(rcdName);
			file.setFileSize(0);
			file.setDuration("");
			file.setStatus(FileItem.File_Status_Recording);
			file.setLevel(0);
			file.setDownloadCount(0);
			file.setPlayCount(0);
			file.setCts(new Date());
			file.setUts(new Date());
			
			if(fileDao.insert(file) <= 0) {
				return 1;
			}
			
		}else if(addOrUpdate == 2) {
			// 更新课件状态
			FileDao fileDao = new FileDao();
			
			String[] arr = tcp.getFileUrl().split("/");
			FileItem file = fileDao.selectByFileName(arr[arr.length - 1]);
			file.setDuration(tcp.getRcdTime());
			file.setUts(new Date());
			file.setStatus(FileItem.File_Status_Recorded);
			if(tcp.getRcdType() == MeetingItem.Record_Type_CMPS) {
				file.setFileSize(tcp.getcMPSRcdSize() * 1024 * 1024);
			}else if(tcp.getRcdType() == MeetingItem.Record_Type_RSRC) {
				file.setFileSize(tcp.getrSRCRcdSize() * 1024 * 1024);
			}else if(tcp.getRcdType() == MeetingItem.Record_Type_MV) {
				file.setFileSize(tcp.getmVRcdSize() * 1024 * 1024);
			}
			
			if(fileDao.update(file) <= 0) {
				return 1;
			}
			
		}
		
		return 0;
	}	
	
	/* 
	 * 开会
	 */
	private int openMeeting(MeetingItem meeting, String confId) throws Exception{
		
		LocalTcp31004 tcp = new LocalTcp31004();
		tcp.setConfId(confId);
		tcp.setIsLive(1);//固定值
		tcp.setRecordType(MeetingItem.Record_Type_CMPS);
		tcp.setLayout(meeting.getRecordMode());
		tcp.setbScale(MeetingItem.Bscale_Extend);
		
		// ds获取
		DatasourceDao dsDao = new DatasourceDao();
		List<DatasourceItem> dsList = dsDao.selectByMeetingId(meeting.getId());
		if(dsList != null) {
			meeting.setDsList(dsList);
		}
		
		List<RecStreamInfo> datasourceList = new ArrayList<RecStreamInfo>();
		
		
		for(DatasourceItem item : meeting.getDsList()) {
			RecStreamInfo ds = tcp.new RecStreamInfo();
			ds.setHasAud(0);
			ds.setStrmAddr(item.getAddr());
			ds.setStrmPort(item.getPort());
			ds.setStrmUser(item.getUsername());
			ds.setStrmPw(item.getPassword());
			ds.setStrmType(item.getDsType());
			ds.setStrmBandwidth(meeting.getBitRate());
			ds.setStrmFmt(meeting.getResolution());
			ds.setStrmFRate(25);
			datasourceList.add(ds);
		}
		
		// 第一路附加声音属性
		datasourceList.get(0).setHasAud(1);// 标识为带声音的
		datasourceList.get(0).setAudSmpRate(48);// 声音采样率
		datasourceList.get(0).setAudCh(2);// 声道
		datasourceList.get(0).setAudBitrate(64);// 声音码率
			
		tcp.setRecStreamInfoList(datasourceList);
		client.request(tcp);
		
		if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
			return 1;
		}
		
		if(tcp.getDoUni() != 1) {//结果显示直播没成功
			return 1;
		}
		
		// 同步本地内存状态
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
		roomStatus.setIsLive(tcp.getDoUni());
		roomStatus.setLiveSource(1);// 标记下是从面板触发的开会
		StatusMgr.setRoomStatus(confId, roomStatus);
		
		return 0;
	}
	
	/* 
	 * 关会
	 */
	private int closeMeeting(String confId) throws Exception{

		LocalTcp31005 tcp = new LocalTcp31005();
		tcp.setConfId(confId);
		client.request(tcp);
		
		if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
			return 1;
		}
		
		// 同步本地内存
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
		roomStatus.setIsLive(0);
		StatusMgr.setRoomStatus(confId, roomStatus);
		
		return 0;
	}

}
