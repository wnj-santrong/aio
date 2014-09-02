package com.santrong.developer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.developer.entry.Dev_JobInfo;
import com.santrong.developer.entry.Dev_JobInfo.TriggerInfo;
import com.santrong.developer.entry.Dev_RoomInfo;
import com.santrong.log.Log;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.schedule.JobImpl;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31016;
import com.santrong.tcp.client.TcpClientService;

/**
 * @author weinianjie
 * @date 2014年7月31日
 * @time 下午7:57:53
 */
@Controller
@RequestMapping("/dev")
public class DeveloperAction extends BaseAction{
	
	@RequestMapping("/index")
	public String index() {
		return "developer/devmain";
	}
	
	/**
	 * 获取会议状态
	 * @return
	 */
	@RequestMapping("/roomStatus")
	public String roomStatus() {
		Hashtable<String, RoomStatusEntry> table = StatusMgr.getHashtable_Room();
		
		List<Dev_RoomInfo> list = new ArrayList<Dev_RoomInfo>();
		if(table != null) {
			Set<String> keys = table.keySet();
			for(String key:keys) {
				Dev_RoomInfo item = new Dev_RoomInfo();
				item.setConfId(key);
				item.setIsConnect(table.get(key).getIsConnect());
				item.setIsLive(table.get(key).getIsLive());
				item.setIsRecord(table.get(key).getIsRecord());
				item.setLiveSource(table.get(key).getLiveSource());
				list.add(item);
			}
		}
		
		getRequest().setAttribute("list", list);
		return "developer/roomStatus";
	}
	
	@RequestMapping("/dsStatus")
	public String dsStatus(HttpServletRequest request) {
		TcpClientService client = TcpClientService.getInstance();
		DatasourceDao dsDao = new DatasourceDao();
		List<DatasourceItem> dsList = dsDao.selectAll();
		request.setAttribute("dsList", dsList);
		
		//获取数据源状态
		LocalTcp31016 tcp = new LocalTcp31016();
		client.request(tcp);
		request.setAttribute("ctrlList", tcp.getSrcStateList());
		
		//这里为了能正常显示界面，不处理请求失败，当成连接不上处理
		if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
			for(int i=0;i<dsList.size();i++) {
				for(int j=0;j<tcp.getSrcStateList().size();j++){
					if(dsList.get(i).getAddr().equals(tcp.getSrcStateList().get(j).getAddr())) {
						dsList.get(i).setIsConnect(tcp.getSrcStateList().get(j).getState());
						break;
					}
				}
			}
		}		
		
		return "developer/dsStatus";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/job")
	public String job() {
		List<Dev_JobInfo> jobList = new ArrayList<Dev_JobInfo>();
		try{
			// 获取任务
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			Set<JobKey> jobs = scheduler.getJobKeys(GroupMatcher.groupEquals(JobImpl.BasicGroup));
			for(Iterator<JobKey> iter = jobs.iterator();iter.hasNext();) {
				JobKey jkey = iter.next();
				Dev_JobInfo item = new Dev_JobInfo();
				item.setJobName(jkey.getName());
				item.setJobGroupName(jkey.getGroup());
				
				// 获取任务里面的触发器
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jkey);
				for(int i=0;i<triggers.size();i++) {
					if(triggers.get(i) instanceof CronTrigger) {
						TriggerInfo tinfo = item.new TriggerInfo();
						CronTrigger trigger = (CronTrigger)triggers.get(i);
						tinfo.setTriggerName(trigger.getKey().getName());
						tinfo.setTriggerGroupName(trigger.getKey().getGroup());
						tinfo.setRuntime(trigger.getCronExpression());
						item.getTriggerList().add(tinfo);
					}
				}
				jobList.add(item);
			}
			
			// 判断运行状态
			for(JobExecutionContext jec :scheduler.getCurrentlyExecutingJobs()) {
				for(Dev_JobInfo jinfo : jobList) {
					if(jec.getJobDetail().getKey().getName().equals(jinfo.getJobName())) {
						jinfo.setStatus(1);
						break;
					}
				}
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		getRequest().setAttribute("jobList", jobList);
		return "developer/job";
	}
	
	@RequestMapping("/playCount")
	public String playCount(HttpServletRequest request) {
		try {  
			
            request.setAttribute("UniUsrCount", StatusMgr.UniUsrCount);
            request.setAttribute("VodUsrCount", StatusMgr.VodUsrCount);
            request.setAttribute("uniVodMax", StatusMgr.uniVodMax);
        } catch (Exception e) {  
            Log.printStackTrace(e); 
        }
		return "developer/playCount";
	}	
	
	@RequestMapping("/proxool")
	public String proxool(HttpServletRequest request) {
		try {  
            SnapshotIF snapshot = ProxoolFacade.getSnapshot("santrong", true);  
            int curActiveCount = snapshot.getActiveConnectionCount();// 获得活动连接数  
            int availableCount = snapshot.getAvailableConnectionCount();// 获得可得到的连接数  
            int maxCount = snapshot.getMaximumConnectionCount();// 获得总连接数  
            request.setAttribute("curActiveCount", curActiveCount);
            request.setAttribute("availableCount", availableCount);
            request.setAttribute("maxCount", maxCount);
        } catch (Exception e) {  
            Log.printStackTrace(e); 
        }
		return "developer/proxool";
	}
	
	
	@RequestMapping("/html")
	public String html() {
		return "dev/html";
	}
}
