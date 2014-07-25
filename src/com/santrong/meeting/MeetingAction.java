package com.santrong.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.log.Log;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.Global;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31004;
import com.santrong.tcp.client.LocalTcp31004.RecStreamInfo;
import com.santrong.tcp.client.LocalTcp31005;
import com.santrong.tcp.client.LocalTcp31006;
import com.santrong.tcp.client.LocalTcp31008;
import com.santrong.tcp.client.LocalTcp31016;
import com.santrong.tcp.client.TcpService;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/meeting")
public class MeetingAction extends BaseAction{
	
	TcpService client = TcpService.getInstance();	
	
	/**
	 * 会议管理主页面
	 * @return
	 */
	@RequestMapping("/home")
	public String home(){
		MeetingDao dao = new MeetingDao();
		DatasourceDao dsDao = new DatasourceDao();
		MeetingItem meeting = dao.selectFirst();
		List<DatasourceItem> dsList = dsDao.selectByMeetingId(meeting.getId());
		if(dsList != null) {
			//获取状态
			LocalTcp31016 tcp = new LocalTcp31016();
			List<String> addrList = new ArrayList<String>();
			addrList.add("");
			client.request(tcp);
			
			//这里为了能正常显示界面，不处理请求失败，当成连接不上处理
			if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
				//对比转换状态
				for(int i=0;i<dsList.size();i++) {
					for(int j=0;j<tcp.getSrcStateList().size();j++){
						if(dsList.get(i).getAddr().equals(tcp.getSrcStateList().get(j).getAddr())) {
							dsList.get(i).setIsConnected(tcp.getSrcStateList().get(j).getState());
							break;
						}
					}
				}
			}
			//TODO 数量不等的时候应该怎么办
			
			meeting.setDsList(dsList);
		}
		
		RoomStatusEntry status = StatusMgr.getRoomStatus(MeetingItem.ConfIdPreview + meeting.getChannel());
		if(status != null) {
			meeting.setIsLive(status.getIsLive());
			meeting.setIsRecord(status.getIsRecord());
			meeting.setIsConnected(1);
		}
		request.setAttribute("meeting", meeting);
		return "meeting/home";
	}
	
	
	/**
	 * 保存
	 * @param meeting
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(MeetingItem meeting) {
		try{
			return persistence(meeting);
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
	}
	
	
	/**
	 * 开启会议
	 * @param meeting
	 * @return
	 */
	@RequestMapping("/openLive")
	@ResponseBody
	public String openLive(MeetingItem meeting) {
		try{
			//持久化配置
			String rs = persistence(meeting);
			if(rs != SUCCESS) {
				return rs;
			}
			

			String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
			RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
			
			if(roomStatus.getIsLive() == 1) {
				return "error_meeting_already_begin";//会议已经被开启，不可重复操作
			}
			
			if(roomStatus.getIsRecord() == 1 && meeting.getUseRecord() == 1) {
				return "error_record_already_begin";//已经有课件正在录制中，请先停止录制
			}
			
			LocalTcp31004 tcp = new LocalTcp31004();
			tcp.setConfId(confId);
			tcp.setIsLive(1);//固定值
			tcp.setRecordType(MeetingItem.Record_Type_CMPS);
			tcp.setLayout(meeting.getRecordMode());
			tcp.setbScale(MeetingItem.Bscale_Extend);
			
			// TODO ds获取
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
			
			//强行把VGA塞进去
			RecStreamInfo vga = tcp.new RecStreamInfo();
			vga.setHasAud(0);
			vga.setStrmAddr("0");
			vga.setStrmPort(0);
			vga.setStrmUser("0");
			vga.setStrmPw("0");
			vga.setStrmType(DatasourceItem.Datasoruce_Type_VGA);
			vga.setStrmBandwidth(0);
			vga.setStrmFmt(0);
			vga.setStrmFRate(0);
			datasourceList.add(vga);
			
			// 第一路附加声音属性
			datasourceList.get(0).setHasAud(1);// 标识为带声音的
			datasourceList.get(0).setAudSmpRate(48);// 声音采样率
			datasourceList.get(0).setAudCh(2);// 声道
			datasourceList.get(0).setAudBitrate(64);// 声音码率
				
			tcp.setRecStreamInfoList(datasourceList);
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
			
			if(tcp.getDoUni() != 1) {//要求直播，但是结果显示直播没成功
				return FAIL;
			}
			
			// 同步本地内存状态
			roomStatus.setIsLive(tcp.getDoUni());
			StatusMgr.setRoomStatus(confId, roomStatus);
			
			// 如果要求录制
			if(meeting.getUseRecord() == 1) {
				
				// 发送录制指令
				rs = doRecord(meeting);
				if(rs != SUCCESS) {
					return rs;
				}
				
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * 关闭会议
	 * @param meeting
	 * @return
	 */
	@RequestMapping("/closeLive")
	@ResponseBody
	public String closeLive(MeetingItem meeting) {
		try{
			String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
			RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
			
			if(roomStatus.getIsLive() == 0) {
				return "error_meeting_already_close";//会议已经被关闭，不可重复操作
			}
			
			LocalTcp31005 tcp = new LocalTcp31005();
			tcp.setConfId(confId);
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
			
			// 同步本地内存
			roomStatus.setIsLive(0);
			roomStatus.setIsRecord(0);//结束会议必然附带结束录制（如果存在录制的话）
			StatusMgr.setRoomStatus(confId, roomStatus);
			
			// 要求录制
			if(meeting.getUseRecord() == 1) {
				// 录制失败
				if(tcp.getRcdResult() == 0) {
					return FAIL;
				}
				FileDao fileDao = new FileDao();
				
				FileItem file = fileDao.selectByFileName(tcp.getFileUrl());
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
					return FAIL;
				}
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * 开始录制
	 * @param meeting
	 * @return
	 */
	@RequestMapping("/startRecord")
	@ResponseBody
	public String startRecord(MeetingItem meeting) {
		try{
			// 持久化
			String rs = persistence(meeting);
			if(rs != SUCCESS) {
				return rs;
			}
		
			// 发送录制指令
			rs = doRecord(meeting);
			if(rs != SUCCESS) {
				return rs;
			}
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * 结束录制
	 * @param meeting
	 * @return
	 */
	@RequestMapping("/stopRecord")
	@ResponseBody
	public String stopRecord(MeetingItem meeting) {
		try{
			String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
			RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
			
			if(roomStatus.getIsRecord() == 0) {
				return "error_record_already_close";//录制已经停止，不可重复操作
			}
			
			LocalTcp31006 tcp = new LocalTcp31006();
			tcp.setConfId(confId);
			tcp.setOperType(0);
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
		
			// 更新内存状态
			roomStatus.setIsRecord(0);
			StatusMgr.setRoomStatus(confId, roomStatus);
			
			// 更新课件状态
			FileDao fileDao = new FileDao();
			FileItem file = fileDao.selectByFileName(tcp.getFileUrl());
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
				return FAIL;
			}
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		return SUCCESS;
	}	
	
	/*
	 * 保存数据
	 */
	private String persistence(MeetingItem meeting) throws Exception {
		
		LocalTcp31008 tcp = new LocalTcp31008();
		tcp.setFreeSize(10240);// 默认剩余10G的空间就不给录制了
		tcp.setMaxTime(meeting.getMaxTime());
		client.request(tcp);
		
		if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
			return FAIL;// 设置系统录制资源阀值失败
		}
		
		MeetingDao dao = new MeetingDao();
		meeting.setUts(new Date());
		if(dao.update(meeting) <= 0) {
			return FAIL;// 存储数据库失败
		}
		return SUCCESS;
	}
	
	/*
	 * 发送录制指令
	 */
	private String doRecord(MeetingItem meeting) throws Exception {
		
		String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
		
		if(roomStatus.getIsRecord() == 1) {
			return "error_record_already_begin";//已经有课件正在录制中，请先停止录制
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//时间作为标致，精确到秒
		String rcdName = Global.vedioDir + "/" + confId + "/" + sdf.format(new Date());//全路径		
		
		LocalTcp31006 tcp = new LocalTcp31006();
		tcp.setConfId(confId);
		tcp.setOperType(1);
		tcp.setCourseName(meeting.getCourseName());
		tcp.setCourseAbs(meeting.getRemark());
		tcp.setTeacher(meeting.getTeacher());
		tcp.setRcdName(rcdName);
		client.request(tcp);
		
		if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
			return FAIL;
		}
		
		// 更新内存状态
		roomStatus.setIsRecord(1);
		StatusMgr.setRoomStatus(confId, roomStatus);
		
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
			return FAIL;
		}
		return SUCCESS;
	}
	
}
