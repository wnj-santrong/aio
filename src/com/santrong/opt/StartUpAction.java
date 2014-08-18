package com.santrong.opt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.santrong.ftp.FtpConfig;
import com.santrong.log.Log;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.schedule.FtpUploadJob;
import com.santrong.schedule.ScheduleManager;
import com.santrong.schedule.StatusMonitJob;
import com.santrong.schedule.SystemUpdateJob;
import com.santrong.system.DirDefine;
import com.santrong.system.UpdateConfig;
import com.santrong.tcp.client.LocalTcp31008;
import com.santrong.tcp.client.TcpClientService;

/**
 * 系统启动的时候直接调用
 */
@SuppressWarnings("serial")
public class StartUpAction extends HttpServlet {
	

	@Override
	public void init() throws ServletException {
		ScheduleManager scheManager = new ScheduleManager();
		
		// 给control层发送一次配置值，防止状态不一致
		try {
			TcpClientService client = TcpClientService.getInstance();
			MeetingDao dao = new MeetingDao();
			MeetingItem dbMeeting = dao.selectFirst();
			if(dbMeeting != null) {
				LocalTcp31008 tcp = new LocalTcp31008();
				tcp.setFreeSize(10240);// 默认剩余10G的空间就不给录制了
				tcp.setMaxTime(dbMeeting.getMaxTime());
				client.request(tcp);
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		
		
		
		// 给shell目录添加权限
		try {
			String[] cmd = new String[] { "/bin/sh", "-c", " chmod 777 " + DirDefine.ShellDir + "/* " };
			Process ps = Runtime.getRuntime().exec(cmd);
			ps.waitFor();
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		
		
		
		// 启动会议室状态监听线程
		scheManager.startCron(new StatusMonitJob());
		
		
		// 启动ftp上传扫描线程
		FtpConfig ftpConfig = new FtpConfig();
		if("1".equals(ftpConfig.getFtpEnable())) {
			scheManager.startCron(new FtpUploadJob());
		}
		
		// 启动在线升级扫描线程
		UpdateConfig updateConfig = new UpdateConfig();
		if("1".equals(updateConfig.getAutoUpdate())) {
			scheManager.startCron(new SystemUpdateJob());
		}
		
		
		// 启动TCP服务监听线程
//		new Thread(new TcpServer(), "---TcpServer").start();
	}
	
}
