package com.santrong.schedule;

import java.net.InetAddress;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.Global;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31001;
import com.santrong.tcp.client.LocalTcp31007;
import com.santrong.tcp.client.LocalTcp31009;
import com.santrong.tcp.client.LocalTcp31007.ConfInfo;
import com.santrong.tcp.client.TcpClientService;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午2:14:28
 */
public class StatusMonitJob extends JobImpl {
	
	public static long lastHeatBeatTime;// 只有一路，先定义一个监听时间
	private static boolean moreOnce;// 是否至少运行过一次，如果control层启动的时候已经发送http过来修改了lastHeatTime，可能导致会议室状态没有被初始化，初始化工作至少运行一次
	
	@Override
	public String getJobName() {
		return "StatusMonit";
	}

	@Override
	public String getTriggerName() {
		return "StatusMonitTrigger";
	}

	@Override
	public String getCronExp() {
		int seconds = Global.HeartInterval / 1000;
		if(seconds < 3) {
			seconds = 3;
		}
		return "*/" + seconds + " * * * * ?";
	}	
	
	@Override
	public Date getDateTime() {
		return null;
	}	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try{
			
			long currentTime = System.currentTimeMillis();
			if(!moreOnce || (currentTime - lastHeatBeatTime) > Global.HeartTimeout) {
				// 确保失败或者失联后一旦重连至少能执行一次内存修正。
				moreOnce = false;				
				
				String key = MeetingItem.ConfIdPreview + 1;// 该版本只有一路，先写死
				RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(key);
				if(roomStatus == null) {
					roomStatus = new RoomStatusEntry();
				}
				// 连接状态置为未连接
				roomStatus.setIsConnect(0);
				StatusMgr.setRoomStatus(key, roomStatus);
				
				
				// --------------------------同步教室状态begin------------------------------------------
				TcpClientService client = TcpClientService.getInstance();
				LocalTcp31001 tcp = new LocalTcp31001();
				tcp.setAddr("http://" + InetAddress.getLocalHost().getHostAddress() + "/http/basic.action");
				tcp.setPort(80);
				tcp.setHeartbeat(Global.HeartInterval);
				
				Log.info("---Connect Controller...");
				client.request(tcp);
				Log.info("---Controller Return ReturnCode=" + tcp.getRespHeader().getReturnCode() + " : ResultCode=" + tcp.getResultCode() + " : IsReboot=" + tcp.getIsReboot());
				
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
				// --------------------------同步教室状态end------------------------------------------				
				
				// --------------------------同步直播点播数begin------------------------------------------
				LocalTcp31009 tcp31009 = new LocalTcp31009();
				client.request(tcp31009);
				if(tcp31009.getResultCode() == 0 && tcp31009.getRespHeader().getReturnCode() == 0) {
					StatusMgr.VodUsrCount = tcp31009.getVodCur();
					StatusMgr.UniUsrCount = tcp31009.getUniCur();
					StatusMgr.uniVodMax = tcp31009.getUniVodMax();
				}
				// --------------------------同步直播点播数end------------------------------------------
				
				// 新增重置判断，如果没连接上，都当成初始化失败，需要不断发送
				if(roomStatus.getIsConnect() == 1) {
					moreOnce = true;//到这里才算至少初始化一次了
				}
				
			}
			
		}catch (Exception e) {
			Log.info("---Connect Controller Fail!");
			Log.printStackTrace(e);
		}
		
	}
}
