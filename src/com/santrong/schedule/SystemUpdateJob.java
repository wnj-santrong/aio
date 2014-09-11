package com.santrong.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.setting.SettingAction;
import com.santrong.system.SystemUpdateService;
import com.santrong.system.UpdateConfig;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午3:06:20
 */
public class SystemUpdateJob extends JobImpl {
	
	@Override
	public String getJobName() {
		return "SystemUpdate";
	}

	@Override
	public String getTriggerName() {
		return "SystemUpdateTrigger";
	}

	@Override
	public String getCronExp() {
		UpdateConfig config = new UpdateConfig();
		String updateTime = config.getUpdateTime();
		String[] arr = updateTime.split(":");
		int minute = Integer.parseInt(arr[1]);
		int hour = Integer.parseInt(arr[0]);
		return "0 " + minute + " " + hour + " * * ?";
	}
	
	@Override
	public Date getDateTime() {
		return null;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		// 判断是否在上课
		if(SettingAction.isClassOpen()) {
			return;
		}			
		
		// 系统正在上传升级、下载升级或者升级中
		if(SystemUpdateService.uploading || SystemUpdateService.updating) {
			return;
		}
		
		try{
			SystemUpdateService service = new SystemUpdateService();
	        service.update();
	        
	        // 打日志
	        Log.logOpt("system-update", "online-cron begin", "system", "127.0.0.1");
	        
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
			// 在quartz里面线程永远不会结束，需要手动关闭数据库连接
			ThreadUtils.closeAll();
		}
	}
}
