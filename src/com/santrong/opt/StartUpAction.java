package com.santrong.opt;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.santrong.log.Log;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31007;
import com.santrong.tcp.client.LocalTcp31007.ConfInfo;
import com.santrong.tcp.client.TcpService;

/**
 * 系统启动的时候直接调用
 */
@SuppressWarnings("serial")
public class StartUpAction extends HttpServlet {
	

	@Override
	public void init() throws ServletException {
		TcpService client = TcpService.getInstance();

		//TODO 假设启动的时候这里异常，如何自动修复
		// 装载教室状态
		MeetingDao meetingDao = new MeetingDao();
		List<MeetingItem> meetingList = meetingDao.selectAll();
		for(MeetingItem item : meetingList) {
			StatusMgr.setRoomStatus(MeetingItem.ConfIdPreview + item.getChannel(), new RoomStatusEntry());
		}
		
		LocalTcp31007 tcp31007 = new LocalTcp31007();
		client.request(tcp31007);
		if(tcp31007.getRespHeader().getReturnCode() == 0 || tcp31007.getResultCode() == 0) {
			for(ConfInfo item : tcp31007.getConfInfoList()) {
				RoomStatusEntry entry = StatusMgr.getRoomStatus(item.getConfId());
				if(entry != null) {
					entry.setIsLive(item.getLiveState());
					entry.setIsRecord(item.getRcdState());
					StatusMgr.setRoomStatus(item.getConfId(), entry);
				}
			}
		}else{
			Log.error("------Init Meeting Room Status Fail!");
		}

		
		// 启动TCP服务监听线程
//		new Thread(new TcpServer(), "---TcpServer").start();
	
	}
	
}
