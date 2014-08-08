package com.santrong.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.log.Log;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.opt.ThreadUtils;
import com.santrong.system.DirDefine;
import com.santrong.system.Global;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31004;
import com.santrong.tcp.client.LocalTcp31004.RecStreamInfo;
import com.santrong.tcp.client.LocalTcp31005;
import com.santrong.tcp.client.LocalTcp31006;
import com.santrong.tcp.client.LocalTcp31008;
import com.santrong.tcp.client.LocalTcp31014;
import com.santrong.tcp.client.LocalTcp31015;
import com.santrong.tcp.client.LocalTcp31016;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/meeting")
public class MeetingAction extends BaseAction{
	
	TcpClientService client = TcpClientService.getInstance();	
	
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
		RoomStatusEntry status = StatusMgr.getRoomStatus(MeetingItem.ConfIdPreview + meeting.getChannel());
		
		if(dsList != null) { 
			if(status != null && status.getIsConnect() == 1) {
				//获取数据源状态
				LocalTcp31016 tcp = new LocalTcp31016();
				List<String> addrList = new ArrayList<String>();
				for(DatasourceItem ds : dsList) {
					addrList.add(ds.getAddr());
				}
				client.request(tcp);
				
				//这里为了能正常显示界面，不处理请求失败，当成连接不上处理
				if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
					//对比转换状态
					for(int i=0;i<dsList.size();i++) {
						for(int j=0;j<tcp.getSrcStateList().size();j++){
							if(dsList.get(i).getAddr().equals(tcp.getSrcStateList().get(j).getAddr())) {
								dsList.get(i).setIsConnect(tcp.getSrcStateList().get(j).getState());
								break;
							}
						}
					}
				}
			}

			meeting.setDsList(dsList);
		}
		
		if(status != null) {
			meeting.setIsLive(status.getIsLive());
			meeting.setIsRecord(status.getIsRecord());
			meeting.setIsConnect(status.getIsConnect());
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
			
			List<RecStreamInfo> datasourceList = new ArrayList<RecStreamInfo>();
			
			DatasourceDao dsDao = new DatasourceDao();
			List<DatasourceItem> dsList = dsDao.selectByMeetingId(meeting.getId());
			for(DatasourceItem item : dsList) {
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
			
			Log.logOpt("meeting-open", "", request);
			
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
			int isRecord = roomStatus.getIsRecord();
			
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
			if(meeting.getUseRecord() == 1 && isRecord == 1) {// 为了保险，最好还是同时判断关闭会议之前的录制状态，因为录制状态有可能被外设或者接口停止了
				// 录制失败
				if(tcp.getRcdResult() == 0) {
					return FAIL;
				}
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
					return FAIL;
				}
			}
			
			Log.logOpt("meeting-close", "", request);
			
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
			
			Log.logOpt("meeting-record", "start", request);
			
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
				return FAIL;
			}
			
			Log.logOpt("meeting-record", "stop", request);
			
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
		MeetingDao dao = new MeetingDao();
		DatasourceDao dsDao = new DatasourceDao();
		MeetingItem dbMeeting = dao.selectById(meeting.getId());
		
		// 设置系统录制资源阀值
		if(dbMeeting.getMaxTime() != meeting.getMaxTime()) {
			LocalTcp31008 tcp = new LocalTcp31008();
			tcp.setFreeSize(10240);// 默认剩余10G的空间就不给录制了
			tcp.setMaxTime(meeting.getMaxTime());
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
		}
		
		// 获取数据源，顺序一定是对的
		String[] ids = request.getParameterValues("dsId");
		String[] addrs = request.getParameterValues("addr");
		String[] ports = request.getParameterValues("port");
		String[] usernames = request.getParameterValues("username");
		String[] passwords = request.getParameterValues("password");
		String[] prioritys = request.getParameterValues("priority");		
		List<DatasourceItem> dsList = new ArrayList<>();
		for(int i=0;i<ids.length;i++) {
			DatasourceItem ds = new DatasourceItem();
			ds.setId(ids[i]);
			ds.setAddr(addrs[i]);
			ds.setPort(CommonTools.stringToInt(ports[i], Global.CameraPort));
			String username = StringUtils.isNullOrEmpty(usernames[i])? Global.CameraUsername : usernames[i];
			ds.setUsername(username);
			String password = StringUtils.isNullOrEmpty(passwords[i])? Global.CameraPassword : passwords[i];
			ds.setPassword(password);
			ds.setPriority(CommonTools.stringToInt(prioritys[i], 0));
			ds.setMeetingId(meeting.getId());
			ds.setDsType(DatasourceItem.Datasoruce_Type_Camera);
			ds.setUts(new Date());
			dsList.add(ds);
		}
		List<DatasourceItem> dsDbList = dsDao.selectByMeetingId(meeting.getId());
		
		List<DatasourceItem> insertList = new ArrayList<DatasourceItem>();
		List<DatasourceItem> updateList = new ArrayList<DatasourceItem>();
		
		// 算出新增、修改、删除
		for(int i=0;i<dsList.size();i++) {
			DatasourceItem item = dsList.get(i);
			if(StringUtils.isNullOrEmpty(item.getId())) {
				item.setId(CommonTools.getGUID());
				item.setCts(new Date());
				insertList.add(item);
			}else{
				for(int j=0;j<dsDbList.size();j++) {
					DatasourceItem dbItem = dsDbList.get(j);
					if(dbItem.getId().equals(item.getId())) {
						if(!item.getAddr().equals(dbItem.getAddr())
								|| item.getPort() != dbItem.getPort()
								|| !item.getUsername().equals(dbItem.getUsername())
								|| !item.getPassword().equals(dbItem.getPassword())
								|| item.getPriority() != dbItem.getPriority()
								) {
							updateList.add(item);
						}
						dsDbList.remove(j);j--;
						break;
					}
				}
			}
		}
		
		// 删除数据源，余下的就是要删除的
		if(dsDbList.size() > 0 && updateList.size() > 0) {
			LocalTcp31015 tcp = new LocalTcp31015();
			for(DatasourceItem ds : dsDbList) {
				tcp.setSrcAddr(ds.getAddr());
			}
			for(DatasourceItem ds : updateList) {// 没有修改数据源的接口，只能先删除再修改了
				tcp.setSrcAddr(ds.getAddr());
			}
			client.request(tcp);
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
		}
		
		// 新增数据源，没有修改数据源的接口，所以采用新增的方法，修改的数据源一定在新增的前面，在新增或者修改的List里面，顺序一定是对的
		if(updateList.size() > 0 || insertList.size() > 0) {
			LocalTcp31014 tcp2 = new LocalTcp31014();
			List<DatasourceItem> tmpList = new ArrayList<DatasourceItem>();
			tmpList.addAll(updateList);
			tmpList.addAll(insertList);
			for(DatasourceItem ds : tmpList) {
				tcp2.setSrcAddr(ds.getAddr());
				tcp2.setSrcPort(ds.getPort());
				tcp2.setSrcUser(ds.getUsername());
				tcp2.setSrcPw(ds.getPassword());
				tcp2.setSrcType(ds.getDsType());
				client.request(tcp2);
				
				if(tcp2.getRespHeader().getReturnCode() == 1 || tcp2.getResultCode() == 1) {
					return FAIL;
				}
			}
		}
		
		try{
			ThreadUtils.beginTranx();
			
			// 存储会议配置
			meeting.setUts(new Date());
			if(dao.update(meeting) <= 0) {
				ThreadUtils.rollbackTranx();
				return FAIL;// 存储数据库失败
			}
			
			//-----注意多人操作下的安全性(根据现场场景暂时忽略)-----
			// 存储数据源
			for(DatasourceItem ds : insertList) {
				if(dsDao.insert(ds)  <= 0) {
					ThreadUtils.rollbackTranx();
					return FAIL;
				}
			}
			// 修改数据源
			for(DatasourceItem ds : updateList) {
				if(dsDao.update(ds)  <= 0) {
					ThreadUtils.rollbackTranx();
					return FAIL;
				}
			}
			
			// 删除数据源
			for(DatasourceItem ds : dsDbList) {
				if(dsDao.delete(ds.getId()) <= 0) {
					ThreadUtils.rollbackTranx();
					return FAIL;
				}
			}
			
			ThreadUtils.commitTranx();
			Log.logOpt("meeting-save", "", request);
			
		}catch(Exception e) {
			ThreadUtils.rollbackTranx();
			Log.printStackTrace(e);
			return FAIL;
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
		String fileName = sdf.format(new Date());
		String rcdName = DirDefine.VedioDir + "/" + confId + "/" + fileName;//全路径		
		
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
		file.setFileName(fileName);
		file.setFileSize(0);
		file.setDuration("");
		file.setStatus(FileItem.File_Status_Recording);
		file.setLevel(0);
		file.setChannel(meeting.getChannel());
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
