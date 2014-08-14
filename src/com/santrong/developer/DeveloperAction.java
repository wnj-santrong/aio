package com.santrong.developer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.santrong.base.BaseAction;
import com.santrong.developer.entry.Dev_RoomInfo;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31016;
import com.santrong.tcp.client.TcpClientService;

/**
 * @author weinianjie
 * @date 2014年7月31日
 * @time 下午7:57:53
 */
@Controller
@RequestMapping("/dev")
public class DeveloperAction extends BaseAction{
	
	@RequestMapping("/index")
	public String index() {
		return "developer/devmain";
	}
	
	/**
	 * 获取会议状态
	 * @return
	 */
	@RequestMapping("/roomStatus")
	public String roomStatus() {
		Hashtable<String, RoomStatusEntry> table = StatusMgr.getHashtable_Room();
		
		List<Dev_RoomInfo> list = new ArrayList<Dev_RoomInfo>();
		if(table != null) {
			Set<String> keys = table.keySet();
			for(String key:keys) {
				Dev_RoomInfo item = new Dev_RoomInfo();
				item.setConfId(key);
				item.setIsConnect(table.get(key).getIsConnect());
				item.setIsLive(table.get(key).getIsLive());
				item.setIsRecord(table.get(key).getIsRecord());
				item.setLiveSource(table.get(key).getLiveSource());
				list.add(item);
			}
		}
		
		request.setAttribute("list", list);
		return "developer/roomStatus";
	}
	
	@RequestMapping("/dsStatus")
	public String dsStatus() {
		TcpClientService client = TcpClientService.getInstance();
		DatasourceDao dsDao = new DatasourceDao();
		List<DatasourceItem> dsList = dsDao.selectAll();
		request.setAttribute("dsList", dsList);
		
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
		
		return "developer/dsStatus";
	}
	
	@RequestMapping("/test")
	@ResponseBody
	public String test() {
		fk();
		MeetingDao dao  = new MeetingDao();
		MeetingItem m = dao.selectFirst();
		return "123";
	}
	private void fk() {
		MeetingDao dao  = new MeetingDao();
		MeetingItem m = dao.selectFirst();
	}
}
