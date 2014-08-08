package com.santrong.system.status;

import java.net.InetAddress;

import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.Global;
import com.santrong.tcp.client.LocalTcp31001;
import com.santrong.tcp.client.LocalTcp31007;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.tcp.client.LocalTcp31007.ConfInfo;


public class StatusMonitor implements Runnable {
	
	public static boolean isRun = false;
	
	public static long lastHeatBeatTime = 0l;// 只有一路，所有先定义一个监听时间
	
	@Override
	public void run() {
		if(!isRun) {
			
			isRun = true;
			
			try{
				Log.info("************Startup StatusMonitor Thread************");
			}catch(Exception e) {}
			
			// 状态监控一旦启动，永不停止，发生异常重连
			
//			调试模式之运行一次，降低控制台输出
//			int a = 0;
//			while(a++ < 1) {
				
			while(true) {
				try{
					
					long currentTime = System.currentTimeMillis();
					if((currentTime - lastHeatBeatTime) > Global.HeartTimeout) {
						
						String key = MeetingItem.ConfIdPreview + 1;// 该版本只有一路，先写死
						RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(key);
						if(roomStatus == null) {
							roomStatus = new RoomStatusEntry();
						}
						// 连接状态置为未连接
						roomStatus.setIsConnect(0);
						StatusMgr.setRoomStatus(key, roomStatus);
						
						
						TcpClientService client = TcpClientService.getInstance();
						LocalTcp31001 tcp = new LocalTcp31001();
						tcp.setAddr("http://" + InetAddress.getLocalHost().getHostAddress() + "/http/basic.action");
						tcp.setPort(80);
						tcp.setHeartbeat(Global.HeartInterval);
						
						
						Log.info("---Connect Controller...");
						client.request(tcp);
						Log.info("---Controller Return ReturnCode=" + tcp.getRespHeader().getReturnCode() + ":ResultCode=" + tcp.getResultCode() + ":IsReboot=" + tcp.getIsReboot());
						
						// 连接状态置为已连接
						if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
							roomStatus.setIsConnect(1);
							StatusMgr.setRoomStatus(key, roomStatus);
						}
						
						// 初始化会议状态和录制状态
						if(roomStatus.getIsConnect() == 1) {
							LocalTcp31007 tcp31007 = new LocalTcp31007();
							client.request(tcp31007);
							if(tcp31007.getRespHeader().getReturnCode() == 0 && tcp31007.getResultCode() == 0) {
								for(ConfInfo item : tcp31007.getConfInfoList()) {
									RoomStatusEntry entry = StatusMgr.getRoomStatus(item.getConfId());
									if(entry != null) {
										entry.setIsLive(item.getLiveState());
										entry.setIsRecord(item.getRcdState());
										StatusMgr.setRoomStatus(item.getConfId(), entry);
									}
								}
							}
						}
						
					}
					
				}catch (Exception e) {
					Log.info("---Connect Controller Fail!");
					Log.printStackTrace(e);
				}
				
				// 休息
				try {
					Thread.sleep(Global.HeartInterval);
				} catch (InterruptedException e) {
					Log.printStackTrace(e);
				}
			}
		}
	}

}
