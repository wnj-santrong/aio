package com.santrong.info;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.info.entry.SystemInfoView;
import com.santrong.log.Log;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.Global;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31009;
import com.santrong.tcp.client.MainTcp39004;
import com.santrong.tcp.client.TcpClientService;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午3:05:53
 */
@Controller
@RequestMapping("/info")
public class InfoAction extends BaseAction{

	@RequestMapping("/home")
	public String home() {
		
		SystemInfoView info = new SystemInfoView();
		
		try{
			MeetingDao dao = new MeetingDao();
			MeetingItem meeting = dao.selectFirst();
			RoomStatusEntry status = StatusMgr.getRoomStatus(MeetingItem.ConfIdPreview + meeting.getChannel());
			
			if(status != null && status.getIsConnect() == 1) {
				TcpClientService client = TcpClientService.getInstance();
				LocalTcp31009 tcp31009 = new LocalTcp31009();
				client.request(tcp31009);
				
				MainTcp39004 tcp39004 = new MainTcp39004();
				client.request(tcp39004);
				
				BeanUtils.copyProperties(tcp31009, info);
				BeanUtils.copyProperties(tcp39004, info);
				
				info.setSystemVersion(tcp39004.getSystemVersion());
			}
			
			info.setWebVersion(Global.Version);
			
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		
		request.setAttribute("info", info);
		
		return "info/home";
	}
	
}
