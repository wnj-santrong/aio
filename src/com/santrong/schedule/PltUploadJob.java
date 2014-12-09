package com.santrong.schedule;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.santrong.http.client.AioHttpClient30002;
import com.santrong.http.client.AioHttpClient30003;
import com.santrong.http.client.HttpClientService;
import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.plt.dao.FilePushDao;
import com.santrong.plt.entry.FilePushItem;
import com.santrong.plt.entry.FilePushView;
import com.santrong.system.Global;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午3:06:20
 */
public class PltUploadJob extends JobImpl {
	
	public static String pushId;// 推送表id，如果没在推送该值为null
	
	@Override
	public String getJobName() {
		return "PltUpload";
	}

	@Override
	public String getTriggerName() {
		return "PltUploadTrigger";
	}

	@Override
	public String getCronExp() {
		// 每30秒检测一次
		return "*/30 * * * * ?";
	}
	
	@Override
	public Date getDateTime() {
		return null;
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try{
		
			if(Global.OpenPlatform) {
				
				FilePushDao pushDao = new FilePushDao();
				HttpClientService client = HttpClientService.getInstance();
				FilePushView view = null;
				
				do{
					
					view = pushDao.selectLastOne();
					if(view != null) {// 有等待推送的课件
						try{
							pushId = view.getId();
							

							// 状态为待推送，如果已经是待推送，则续传即可
							boolean preResult = true;
							if(view.getStatus() == FilePushItem.File_Push_Status_Wating) {
								
								// 设置本地文件状态为推送中
								pushDao.updateStatus(pushId, FilePushItem.File_Push_Status_Pushing);
								
								// 远程库新增文件
								AioHttpClient30002 http30002 = new AioHttpClient30002();
								http30002.setUsername(view.getUsername());
								http30002.setTitle(view.getCourseName());
								http30002.setFileSize(view.getFileSize());
								http30002.setDuration(view.getDuration());
								http30002.setRemoteId(view.getRemoteId());
								client.request(http30002);
								if(http30002.getRespHeader().getResultCode() != 1) {
									pushDao.updateStatus(pushId, FilePushItem.File_Push_Status_Error);// 不成功，标识异常
									preResult = false;
								}
							}
							
							// 前面步骤没有失败
							if(preResult) {	
								//TODO 正式开始推送文件，支持续传
								
								// 文件推送完成后，远程文件标识为推送完成
								AioHttpClient30003 http30003 = new AioHttpClient30003();
								http30003.setRemoteId(view.getRemoteId());
								http30003.setStatus(FilePushItem.File_Push_Status_Done);
								client.request(http30003);
								
								if(http30003.getRespHeader().getResultCode() == 1) {
									pushDao.updateStatus(pushId, FilePushItem.File_Push_Status_Done);// 成功，标识完成
								}else {
									pushDao.updateStatus(pushId, FilePushItem.File_Push_Status_Error);// 不成功，标识异常
								}
							}
							
						}catch(Exception e) {
							Log.printStackTrace(e);
						}finally{
							pushId = null;
						}
					}
					
				}while(view != null);
				
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
			// 在quartz里面线程永远不会结束，需要手动关闭数据库连接
			ThreadUtils.closeAll();
		}
	}
}
