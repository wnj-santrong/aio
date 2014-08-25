package com.santrong.schedule;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.log.Log;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.opt.ThreadUtils;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午2:14:28
 */
public class FileStatusCheckJob extends JobImpl {
	
	@Override
	public String getJobName() {
		return "FileStatusCheck";
	}

	@Override
	public String getTriggerName() {
		return "FileStatusCheckTrigger";
	}

	@Override
	public String getCronExp() {
		return "0 20 0 * * ?";// 每天00:20:00执行
	}	
	
	@Override
	public Date getDateTime() {
		return null;
	}	

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			FileDao dao = new FileDao();
			MeetingDao mdao = new MeetingDao();
			MeetingItem meeting = mdao.selectFirst();
			List<FileItem> list = dao.selectByStatus(FileItem.File_Status_Recording);// 获取所有正在录制的课件
			Date now = new Date();
			for(FileItem file:list) {
				// 判断时间是否超时
				Date cts = file.getCts();
				if(cts != null) {
					long separat = now.getTime() - cts.getTime();
					if(separat/1000/60 >= meeting.getMaxTime()) {
						file.setStatus(FileItem.File_Status_ERROR);
						file.setUts(now);
						if(dao.update(file) > 0) {
							Log.mark("---fix file status success with file id " + file.getId());
						}else {
							Log.mark("---fix file status fail with file id " + file.getId());
						}
					}
				}
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}finally{
			// 在quartz里面线程永远不会结束，需要手动关闭数据库连接
			ThreadUtils.closeAll();
		}		
		
	}
}
