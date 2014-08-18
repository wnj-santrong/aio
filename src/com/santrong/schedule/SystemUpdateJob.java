package com.santrong.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.santrong.log.Log;
import com.santrong.system.SystemUpdateService;
import com.santrong.system.UpdateConfig;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午3:06:20
 */
public class SystemUpdateJob implements JobImpl {
	
	public static boolean updating;
	
	@Override
	public String getJobName() {
		return "SystemUpdate";
	}
	
	@Override
	public String getGroupName() {
		return "BasicGroup";
	}

	@Override
	public String getTriggerName() {
		return "SystemUpdateTrigger";
	}

	@Override
	public String getTriggerGroupName() {
		return "BasicTriggerGroup";
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
		updating = true;
		try{
			SystemUpdateService service = new SystemUpdateService();
	        String rt = service.update();
	        
	        // 打日志
	        if("notice_update_success".equals(rt)){
	        	Log.logOpt("system-update", "online-cron", "system", "127.0.0.1");
	        }
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
			updating = false;
		}
	}
}
