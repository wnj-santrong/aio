package com.santrong.listener;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.santrong.ftp.FtpConfig;
import com.santrong.log.Log;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.schedule.FtpUploadJob;
import com.santrong.schedule.LogClearJob;
import com.santrong.schedule.ScheduleManager;
import com.santrong.schedule.StatusMonitJob;
import com.santrong.schedule.StorageMonitJob;
import com.santrong.schedule.SystemUpdateJob;
import com.santrong.system.DirDefine;
import com.santrong.system.UpdateConfig;
import com.santrong.tcp.client.LocalTcp31008;
import com.santrong.tcp.client.TcpClientService;

/**
 * @Author weinianjie
 * @Date 2014-7-6
 * @Time 下午6:37:09
 */
public class StartUpListener implements ServletContextListener {
	
	ScheduleManager scheManager = new ScheduleManager();
	
	// 启动执行
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// 把proxool配置载入环境
		try{
			Properties dbProps = new Properties();
			dbProps.load(StartUpListener.class.getResourceAsStream("/datasource.properties"));
			PropertyConfigurator.configure(dbProps);
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		
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
		
		// 启动日志清理线程
		scheManager.startCron(new LogClearJob());
		
		// 启动磁盘空间监控线程
		scheManager.startCron(new StorageMonitJob());
		
		
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
	
	// 销毁执行
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			
			// 手工销毁quartz框架防止tomcat关不住
			Log.info("------:destroy quartz framework----");
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			
//			@SuppressWarnings("unchecked")
//			Set<TriggerKey> triggers = scheduler.getTriggerKeys(GroupMatcher.groupEquals(JobImpl.BasicTriggerGroup));
//			@SuppressWarnings("unchecked")
//			Set<JobKey> jobs = scheduler.getJobKeys(GroupMatcher.groupEquals(JobImpl.BasicGroup));
//			
//			for(Iterator<TriggerKey> t=triggers.iterator(); t.hasNext();) {
//				scheduler.pauseTrigger(t.next());//停止触发器  
//				scheduler.unscheduleJob(t.next());//移除触发器 
//			}
//			for(Iterator<JobKey> j=jobs.iterator(); j.hasNext();) {
//				scheduler.deleteJob(j.next());//删除任务
//			}
 
            scheduler.shutdown(true);
            Thread.sleep(100);// Sleep for a bit so that we don't get any errors
        } catch (Exception e){
            Log.printStackTrace(e);
        }
	}

}
