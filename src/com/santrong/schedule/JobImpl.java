package com.santrong.schedule;

import java.util.Date;

import org.quartz.Job;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 下午2:31:11
 */
public interface JobImpl extends Job {
	
	String getJobName();
	
	String getTriggerName();
	
	String getGroupName(); 
	
	String getCronExp();
	
	Date getDateTime();
}
