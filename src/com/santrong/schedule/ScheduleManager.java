package com.santrong.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.santrong.log.Log;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午2:24:23
 */
public class ScheduleManager {
	
	SchedulerFactory factory = new StdSchedulerFactory();
	
	public void startCron(JobImpl job) {
		if(job != null) {
			try{
				
				Log.info("************startup job : " + job.getJobName() + " by cron [" + job.getCronExp() + "]************");
				
				Scheduler scheduler = factory.getScheduler();
				JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(job.getJobName(), job.getGroupName()).build();
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getTriggerName(), job.getGroupName()).withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExp())).build();//00:00:00
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.start();
				
			}catch(Exception e) {
				Log.printStackTrace(e);
			}
		}
	}
	
	public void startOnce(JobImpl job) {
		
	}
	
}
