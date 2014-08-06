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
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;

/**
 * @author weinianjie
 * @date 2014年7月31日
 * @time 下午7:57:53
 */
@Controller
@RequestMapping("/dev")
public class DeveloperAction extends BaseAction{
	
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
