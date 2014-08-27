package com.santrong.meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.santrong.system.tip.TipItem;
import com.santrong.system.tip.TipService;
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
		MeetingDao meetingDao = new MeetingDao();
		DatasourceDao dsDao = new DatasourceDao();
		MeetingItem meeting = meetingDao.selectFirst();
		List<DatasourceItem> dsList = dsDao.selectByMeetingId(meeting.getId());
		RoomStatusEntry status = StatusMgr.getRoomStatus(MeetingItem.ConfIdPreview + meeting.getChannel());
		
		if(dsList != null) { 
			if(status != null && status.getIsConnect() == 1) {
				//获取数据源状态
				LocalTcp31016 tcp = new LocalTcp31016();
				client.request(tcp);
				
				//这里为了能正常显示界面，不处理请求失败，当成连接不上处理
				if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
					// 校验数据源个数---是否需要全部校验个数和IP完全对应？
					if(dsList.size() == tcp.getSrcStateList().size()) {
						//对比转换状态
						for(int i=0;i<dsList.size();i++) {
							for(int j=0;j<tcp.getSrcStateList().size();j++){
								if(dsList.get(i).getAddr().equals(tcp.getSrcStateList().get(j).getAddr())) {
									dsList.get(i).setIsConnect(tcp.getSrcStateList().get(j).getState());
									break;
								}
							}
						}
					}else {// 如果因为未知的异常导致个数不相等，以web为标准，更新控制层数据库
						// 删除
						if(tcp.getSrcStateList().size() > 0) {
							LocalTcp31015 tcp31015 = new LocalTcp31015();
							List<String> srcList = new ArrayList<String>();
							for(int i=0;i<tcp.getSrcStateList().size();i++) {
								srcList.add(tcp.getSrcStateList().get(i).getAddr());
							}
							tcp31015.setSrcAddrList(srcList);
							client.request(tcp31015);
							if(tcp31015.getRespHeader().getReturnCode() == 1 || tcp31015.getResultCode() == 1) {
								// 标记错误，不干扰使用
								String dsxxstr = "";
								for(String s : srcList) {
									dsxxstr += ("," + s);
								}
								dsxxstr.substring(1);
								Log.mark("--meetingPage find the datasource size is not equals, remove datasource in control fail:" + dsxxstr);
							}
						}
						// 新增
						if(dsList.size() > 0) {
							LocalTcp31014 tcp31014 = new LocalTcp31014();
							for(DatasourceItem ds : dsList) {
								tcp31014.setSrcAddr(ds.getAddr());
								tcp31014.setSrcPort(ds.getPort());
								tcp31014.setSrcUser(ds.getUsername());
								tcp31014.setSrcPw(ds.getPassword());
								tcp31014.setSrcType(ds.getDsType());
								client.request(tcp31014);
								
								if(tcp31014.getRespHeader().getReturnCode() == 1 || tcp31014.getResultCode() == 1) {
									// 标记错误，不干扰使用
									Log.mark("--meetingPage find the datasource size is not equals, add datasource in control fail:" + ds.getAddr());
								}
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
		getRequest().setAttribute("meeting", meeting);
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
			String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
			RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
			
			if(roomStatus.getIsLive() == 1) {
				return "error_meeting_already_begin";//会议已经被开启，不可重复操作
			}
			
			if(roomStatus.getIsRecord() == 1 && meeting.getUseRecord() == 1) {
				return "error_record_already_begin";//已经有课件正在录制中，请先停止录制
			}
			
			//持久化配置
			String rs = persistence(meeting);
			if(rs != SUCCESS) {
				return rs;
			}
			
			LocalTcp31004 tcp = new LocalTcp31004();
			tcp.setConfId(confId);
			tcp.setIsLive(1);//固定值
			tcp.setRecordType(meeting.getRecordType());
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
			
			// 到这里开会就算成功了
			Log.logOpt("meeting-open", "", getRequest());
			
			// 如果要求录制，有可能磁盘不足，在子方法中判断，结果不影响开会成功，但是会给出提示
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
			int isRecord = roomStatus.getIsRecord();
			
			if(roomStatus.getIsConnect() == 0) {
				return "error_meeting_connecte_error";//连接不上服务器
			}
			
			if(roomStatus.getIsLive() == 0) {
				return "error_meeting_other_close";//会议没有开启
			}
			
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
				
				String[] arr = tcp.getFileUrl().split("/");
				FileDao fileDao = new FileDao();
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
			
			Log.logOpt("meeting-close", "", getRequest());
			
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
			// 发送录制指令
			String rs = doRecord(meeting);
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
			
			if(roomStatus.getIsConnect() == 0) {
				return "error_meeting_connecte_error";//连接不上服务器
			}
			
			if(roomStatus.getIsLive() == 0) {
				return "error_meeting_other_close";//会议没有开启
			}			
			
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
			String[] arr = tcp.getFileUrl().split("/");
			FileDao fileDao = new FileDao();
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
			
			Log.logOpt("meeting-record", "stop", getRequest());
			
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
		HttpServletRequest request = getRequest();
		// 获取数据源，顺序一定是对的
		String[] ids = request.getParameterValues("dsId");
		String[] addrs = request.getParameterValues("addr");
		String[] ports = request.getParameterValues("port");
		String[] usernames = request.getParameterValues("username");
		String[] passwords = request.getParameterValues("password");
		String[] prioritys = request.getParameterValues("priority");
		for(String ad : addrs) {
			if(StringUtils.isNullOrEmpty(ad)) {
				return super.ERROR_PARAM;
			}
		}
		
		String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
		if(roomStatus.getIsLive() == 1) {
			return "error_meeting_is_begin_not_save";//已经开会不能持久化
		}		
		
		MeetingDao meetingDao = new MeetingDao();
		MeetingItem dbMeeting = meetingDao.selectById(meeting.getId());
		
		// 设置系统录制资源阀值
		if(dbMeeting.getMaxTime() != meeting.getMaxTime()) {
			LocalTcp31008 tcp = new LocalTcp31008();
			tcp.setFreeSize(Global.DiskErrorSize);
			tcp.setMaxTime(meeting.getMaxTime());
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
		}
		
		// 持久化数据源
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
		DatasourceDao dsDao = new DatasourceDao();
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
			List<String> srcList = new ArrayList<String>();			
			for(DatasourceItem ds : dsDbList) {
				srcList.add(ds.getAddr());
			}
			for(DatasourceItem ds : updateList) {// 没有修改数据源的接口，只能先删除再修改了
				srcList.add(ds.getAddr());
			}
			tcp.setSrcAddrList(srcList);
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
			if(meetingDao.update(meeting) <= 0) {
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
		
		// 磁盘容量判断
		TipService tipService = new TipService();
		TipItem tip = tipService.getTip(TipService.Disk_Lack);
		if(tip != null && tip.getOther() == 1) {//0是磁盘少需要注意，1是磁盘不足拒绝录制
			return "error_disk_lack";
		}
		
		String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(confId);
		
		if(roomStatus.getIsConnect() == 0) {
			return "error_meeting_connecte_error";//连接不上服务器
		}
		
		if(roomStatus.getIsLive() == 0) {
			return "error_meeting_other_close";//会议没有开启
		}
		
		if(roomStatus.getIsRecord() == 1) {
			return "error_record_already_begin";//已经有课件正在录制中，请先停止录制
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//时间作为标致，精确到秒
		String fileName = sdf.format(new Date());
		String rcdName = DirDefine.VedioDir + "/" + confId + "/" + fileName;//全路径		
		
		LocalTcp31006 tcp = new LocalTcp31006();
		tcp.setConfId(confId);
		tcp.setOperType(1);
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
			return FAIL;
		}
		
		// 更新内存状态
		roomStatus.setIsRecord(1);
		StatusMgr.setRoomStatus(confId, roomStatus);
		
		// 新增文件
		FileItem file = new FileItem();
		BeanUtils.copyProperties(meeting, file);
		file.setId(CommonTools.getGUID());
		file.setFileName(fileName);
		if(StringUtils.isNullOrEmpty(meeting.getCourseName())) {
			file.setCourseName(fileName);
		}else{
			file.setCourseName(meeting.getCourseName());
		}
		file.setFileSize(0);
		file.setDuration("");
		file.setStatus(FileItem.File_Status_Recording);
		file.setLevel(0);
		file.setChannel(meeting.getChannel());
		file.setDownloadCount(0);
		file.setPlayCount(0);
		file.setCts(new Date());
		file.setUts(new Date());
		
		FileDao fileDao = new FileDao();
		if(fileDao.insert(file) <= 0) {
			return FAIL;
		}
		
		Log.logOpt("meeting-record", "start", getRequest());
		
		return SUCCESS;
	}
	
}
