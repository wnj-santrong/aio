package com.santrong.meeting;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.log.Log;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.opt.ThreadUtils;
import com.santrong.system.Global;
import com.santrong.tcp.client.LocalTcp31014;
import com.santrong.tcp.client.LocalTcp31015;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月23日
 * @time 下午2:55:44
 */
@Controller
@RequestMapping("/datasource")
public class DatasourceAction extends BaseAction {
	
	TcpClientService client = TcpClientService.getInstance();
	
	/**
	 * 数据源GET
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/dsGet", method=RequestMethod.GET)
	public String dsGet(String id, String meetingId) {
		if(StringUtils.isNullOrEmpty(meetingId)){
			return ERROR_PARAM;
		}
		
		DatasourceItem ds;
		if(!StringUtils.isNullOrEmpty(id)){
			DatasourceDao dsDao = new DatasourceDao();
			ds = dsDao.selectById(id);
		}else{
			ds = new DatasourceItem();
			ds.setMeetingId(meetingId);
		}

		request.setAttribute("ds", ds);
		
		return "meeting/datasource";
	}
	
	/**
	 * 数据源POST
	 * @param file
	 * @return
	 */
	@RequestMapping(value="/dsPost", method=RequestMethod.POST)
	@ResponseBody
	public String dsPost(DatasourceItem ds) {
		DatasourceDao dsDao = new DatasourceDao();
		
		ds.setUts(new Date());
		ds.setDsType(DatasourceItem.Datasoruce_Type_Camera);
		
		// 校验数量，多人操作可能引发的问题
		int dsCount = 0;
		if(StringUtils.isNullOrEmpty(ds.getId())) {
			dsCount = dsDao.selectCountByMeetingId(ds.getMeetingId());
			if(dsCount == Global.VedioCount - 1) {// 扣除VGA
				return "error_datasource_already_max";
			}
		}
		
		// 校验数据源重复
		boolean needAdd = true;
		boolean needDel = false;
		String delAddr = "";
		List<DatasourceItem> existList = dsDao.selectByMeetingId(ds.getMeetingId());
		for(DatasourceItem item : existList) {
			if(!StringUtils.isNullOrEmpty(ds.getId()) || item.getMeetingId() == ds.getMeetingId()) {// 修改的情况下
				if(!item.getAddr().equals(ds.getAddr())) {//如果地址不同了
					needDel = true;
					delAddr = item.getAddr();
					continue;
				}else {// 如果地址没变化
					needAdd = false;
					break;
				}
			}
			if(item.getAddr().equals(ds.getAddr())) {
				return "error_datasource_already_exists";
			}
		}
		
		if(needDel) {
			LocalTcp31015 tcp = new LocalTcp31015();
			tcp.setSrcAddr(delAddr);
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
		}
		
		if(needAdd) {
			LocalTcp31014 tcp = new LocalTcp31014();
			tcp.setSrcAddr(ds.getAddr());
			tcp.setSrcPort(ds.getPort());
			tcp.setSrcUser(ds.getUsername());
			tcp.setSrcPw(ds.getPassword());
			tcp.setSrcType(ds.getDsType());
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
				return FAIL;
			}
		}
		
		// 数据库操作
		ThreadUtils.beginTranx();
		try{
			if(StringUtils.isNullOrEmpty(ds.getId())) {
				ds.setId(CommonTools.getGUID());
				ds.setCts(new Date());
				
				MeetingDao meetingDao = new MeetingDao();
				MeetingItem meeting = meetingDao.selectById(ds.getMeetingId());
				meeting.setUts(new Date());
				//视频路数改变，布局模式也要调整
				int recordMode = 0;
				if(dsCount == 0) {//0+1+1（原有+即将新增+VGA）
					recordMode = 2;
				}else if(dsCount == 1) {//1+1+1
					recordMode = 10;
				}
				meeting.setRecordMode(recordMode);
				
				if(dsDao.insert(ds) < 1 || meetingDao.update(meeting) < 1) {
					ThreadUtils.rollbackTranx();
					return FAIL;
				}
			}else{
				if(dsDao.update(ds) < 1) {
					ThreadUtils.rollbackTranx();
					return FAIL;
				}
			}
			ThreadUtils.commitTranx();
		}catch(Exception e) {
			ThreadUtils.rollbackTranx();
			Log.printStackTrace(e);
			return FAIL;
		}

		
		return SUCCESS;
	}
	
	/**
	 * 删除数据源
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/dsDel", method=RequestMethod.POST)
	@ResponseBody
	public String dsDel(String id) {
		if(StringUtils.isNullOrEmpty(id)) {
			return FAIL;
		}
		
		DatasourceDao dsDao = new DatasourceDao();
		
		DatasourceItem item = dsDao.selectById(id);
		if(item == null) {
			return FAIL;
		}
		
		// 发送tcp
		LocalTcp31015 tcp = new LocalTcp31015();
		tcp.setSrcAddr(item.getAddr());
		client.request(tcp);
		
		if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
			return FAIL;
		}
		
		ThreadUtils.beginTranx();
		try{
			MeetingDao meetingDao = new MeetingDao();
			int dsCount = dsDao.selectCountByMeetingId(item.getMeetingId());
			MeetingItem meeting = meetingDao.selectById(item.getMeetingId());
			meeting.setUts(new Date());
			//视频路数改变，布局模式也要调整
			int recordMode = 0;
			if(dsCount == 1) {//1-1+1
				recordMode = 1;
			}else if(dsCount == 2) {//2-1+1
				recordMode = 2;
			}
			meeting.setRecordMode(recordMode);
			if(dsDao.delete(id) < 1 || meetingDao.update(meeting) < 1) {
				return FAIL;
			}
			ThreadUtils.commitTranx();
		}catch(Exception e) {
			Log.printStackTrace(e);
			ThreadUtils.rollbackTranx();
			return FAIL;
		}
		return SUCCESS;
	}
	
}
