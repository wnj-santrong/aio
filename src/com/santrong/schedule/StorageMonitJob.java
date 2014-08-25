package com.santrong.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.system.Global;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.system.tip.TipItem;
import com.santrong.system.tip.TipService;
import com.santrong.tcp.client.LocalTcp31009;
import com.santrong.tcp.client.TcpClientService;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午2:14:28
 */
public class StorageMonitJob extends JobImpl {
	
	private static boolean moreOnce;
	
	@Override
	public String getJobName() {
		return "StorageMonit";
	}

	@Override
	public String getTriggerName() {
		return "StorageMonitTrigger";
	}

	@Override
	public String getCronExp() {
		return "*/10 * * * * ?";// 每10秒执行一次
	}	
	
	@Override
	public Date getDateTime() {
		return null;
	}	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			// 能连接上control层返回
			String key = MeetingItem.ConfIdPreview + 1;// 该版本只有一路，先写死
			RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(key);
			if(roomStatus == null || roomStatus.getIsConnect() == 0) {
				return;
			}
			
			// 提醒堆栈里没有磁盘空间不足返回
			TipService tipService = new TipService();
			TipItem tip = tipService.getTip(TipService.Disk_Lack);
			if(moreOnce) {// 确保第一次启动跳过下面，以便重新检测磁盘空间
				if(tip == null || tip.getOther() != 1) {
					return;
				}
			}
			
			// 发送tcp查看磁盘空间
			TcpClientService client = TcpClientService.getInstance();
			LocalTcp31009 tcp31009 = new LocalTcp31009();
			client.request(tcp31009);
			long freeSize = tcp31009.getFreeSize();
			if(freeSize > Global.DiskErrorSizeCancel) {// 大于取消提醒的设定值
				tipService.removeTip(TipService.Disk_Lack);// 移除磁盘空间不足提示
			}else{
				if(tip == null) {
					tip = new TipItem();
					tip.setTitle("tip_disk_lack_title");
					tip.setContent("tip_disk_lack_content");
					tip.setOther(1);//0是少，1是不足
					tipService.setTip(TipService.Disk_Lack, tip);
				}
			}
			
			moreOnce = true;
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}		
		
	}
}
