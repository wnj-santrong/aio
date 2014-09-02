package com.santrong.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.ftp.FtpConfig;
import com.santrong.ftp.FtpHandler;
import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.schedule.FtpUploadJob;
import com.santrong.schedule.ScheduleManager;
import com.santrong.schedule.SystemUpdateJob;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.DirDefine;
import com.santrong.system.Global;
import com.santrong.system.SystemUpdateService;
import com.santrong.system.UpdateConfig;
import com.santrong.system.network.NetworkInfo;
import com.santrong.system.network.SystemUtils;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.util.SantrongUtils;
import com.santrong.util.FileUtils;
import com.scand.fileupload.ProgressMonitorFileItemFactory;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/setting")
public class SettingAction extends BaseAction{
	
	public static boolean isDatabaseDoing;// 数据库是否正则备份或者恢复
	
	/*
	 * 主页面
	 */
	@RequestMapping("/home")
	public String home(){
		return "setting/home";
	}
	
	
	/*
	 * 管理员修改密码
	 */
	@RequestMapping(value="/user", method=RequestMethod.POST)
	@ResponseBody
	public String userUpdate(HttpServletRequest request, String newname, String newpwd, String oldpwd) {
		if(StringUtils.isNullOrEmpty(newname) || StringUtils.isNullOrEmpty(newpwd) || StringUtils.isNullOrEmpty(oldpwd)) {
			return "error_param";
		}
		
		newpwd = SantrongUtils.getMD5(newpwd);
		oldpwd = SantrongUtils.getMD5(oldpwd);
		try{
			UserDao userDao = new UserDao();
			UserItem user = userDao.selectByUserName(this.currentUser().getUsername());
			if(user.getPassword().equals(oldpwd)){
				user.setShowName(newname);
				user.setUsername(newname);
				user.setPassword(newpwd);
				if(userDao.update(user) <= 0) {
					return FAIL;
				}
			}else{
				return "error_oldpwd";
			}
			
			// 把新用户注入session
			UserItem newuser = userDao.selectByUserName(newname);
			if(newuser != null) {
				request.getSession().setAttribute(Global.SessionKey_LoginUser, newuser);
			}
			
			Log.logOpt("user-modify", newname, request);
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		return SUCCESS;
	}	
	
	
	/*
	 * 数据库备份列表
	 */
	@RequestMapping("/dbList")
	@ResponseBody
	public String database() {
		List<String> nameList = new ArrayList<String>();
		
		List<File> dbList = FileUtils.searchFile(DirDefine.DbBackupDir, "sql");
		if(dbList != null) {
			for(File f : dbList) {
				nameList.add(f.getName());
			}
		}
		
		Gson gson = new Gson();
		
		return gson.toJson(nameList);
	}
	
	
	/*
	 * 数据库备份操作
	 */
	@RequestMapping(value="/dbBackup", method=RequestMethod.POST)
	@ResponseBody
	public String dbBackup() {
		String[] cmd = new String[] { "/bin/sh", "-c", " " + DirDefine.ShellDir + "/dbBackup.sh " };
		
		try {
			
			// 判断是否在上课
			if(isClassOpen()) {
				return "warn_class_is_open";
			}			
			
			if(isDatabaseDoing) {
				return "error_setting_dbdoing";
			}
			isDatabaseDoing = true;
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				Log.debug("----------- db back success");
				Log.logOpt("db-backup", "", getRequest());
				return SUCCESS;
			} else {
				Log.debug("----------- db back fail");
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}finally{
			isDatabaseDoing = false;
		}
		
		return FAIL;
	}
	
	
	/*
	 * 数据库删除操作
	 */
	@RequestMapping(value="/dbDel", method=RequestMethod.POST)
	@ResponseBody
	public String dbDel(String filename) {
		if(StringUtils.isNullOrEmpty(filename)) {
			return "error_param";
		}
		
		if(!FileUtils.delFile(DirDefine.DbBackupDir + File.separator + filename)) {
			return FAIL;
		}
		
		Log.logOpt("db-delete", filename, getRequest());
		
		return SUCCESS;
	}
	
	
	/*
	 * 数据库恢复操作
	 */
	@RequestMapping(value="/dbRestore", method=RequestMethod.POST)
	@ResponseBody
	public String dbRestore(String filename) {
		if(StringUtils.isNullOrEmpty(filename)) {
			return "error_param";
		}
		
		// 判断是否在上课
		if(isClassOpen()) {
			return "warn_class_is_open";
		}		
		
		String[] cmd = new String[] { "/bin/sh", "-c", " " + DirDefine.ShellDir + "/dbRestore.sh " + filename };
		
		try {
			if(isDatabaseDoing) {
				return "error_setting_dbdoing";
			}
			isDatabaseDoing = true;
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				Log.debug("----------- db restore success");
				Log.logOpt("db-restore", filename, getRequest());
				return SUCCESS;
			} else {
				Log.debug("----------- db restore fail");
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}finally {
			isDatabaseDoing = false;
		}
		
		return FAIL;
	}	
	
	/*
	 * 网络设置获取
	 */
	@RequestMapping(value="/networkGet", method=RequestMethod.GET)
	@ResponseBody
	public String inputNetwork(int type) {// lan 0, wan 1
		NetworkInfo info = SystemUtils.getNetwork(type);
		Gson gson = new Gson();
		
		return gson.toJson(info);
	}	
	
	/*
	 * 网络设置写入
	 */
	@RequestMapping(value="/networkPost", method=RequestMethod.POST)
	@ResponseBody
	public String saveNetwork(int type, String ip, String mask, String gateway) {
		if(StringUtils.isNullOrEmpty(ip) || StringUtils.isNullOrEmpty(mask) || StringUtils.isNullOrEmpty(gateway)) {
			return "error_param";
		}
		
		// 判断是否在上课
		if(isClassOpen()) {
			return "warn_class_is_open";
		}
		
		NetworkInfo info = new NetworkInfo();
		info.setIndex(type);
		info.setIp(ip);
		info.setMask(mask);
		info.setGateway(gateway);
		
		if(!SystemUtils.setNetwork(info)) {
			return FAIL;
		}
		
		// 重启网卡
		String[] cmd = new String[] { "/bin/sh", "-c", " " + DirDefine.ShellDir + "/networkRestart.sh " };
		
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				Log.debug("----------- restart network success");
				Log.logOpt("net-save", String.valueOf(type), getRequest());
				return SUCCESS;
			} else {
				Log.debug("----------- restart network fail");
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}			
		
		
		
		return FAIL;
	}	
	
	/*
	 * ftp配置获取
	 */
	@RequestMapping(value="/ftp", method=RequestMethod.GET)
	@ResponseBody
	public String getFtp() {
		FtpConfig config = new FtpConfig();
		Gson gson = new Gson();
		
		return gson.toJson(config);
	}	
	
	/*
	 * ftp设置
	 */
	@RequestMapping(value="/ftp", method=RequestMethod.POST)
	@ResponseBody
	public String postFtp(Integer useFtp, String host, String port, String username, String password, String duration) {
		// 数据校验
		if(StringUtils.isNullOrEmpty(host) || StringUtils.isNullOrEmpty(port) || StringUtils.isNullOrEmpty(duration)) {
			return "error_param";
		}
		
		try{
			// 持久化
			FtpConfig config = new FtpConfig();
			if(useFtp != null && useFtp == 1) {
				config.setFtpEnable("1");
			}else{
				config.setFtpEnable("0");
			}
			String[] arr = duration.split("--");
			if(arr.length == 2) {
				config.setBeginTime(arr[0]);
				config.setEndTime(arr[1]);
			}
			config.setFtpIp(host);
			config.setFtpPort(port);
			config.setUsername(username);
			config.setPassword(password);
			if(config.write()) {
	
				// 强行停止ftp，无论是禁用了还是修改了配置，都需要停止再说
				FtpHandler handler = FtpHandler.getInstance();
				handler.forceStop();
				FtpUploadJob.uploading = false;
				
				ScheduleManager scheManager = new ScheduleManager();
				FtpUploadJob job = new FtpUploadJob();
				if("1".equals(config.getFtpEnable())) {
					// 如果原来没有运行调度，启动调度
					if(!scheManager.existsCron(job)) {
						if(!scheManager.startCron(job)) {
							return FAIL;
						}
					}
				}else {
					// 如果原来有运行调度，停止掉
					if(scheManager.existsCron(job)) {
						if(!scheManager.removeCron(job)) {
							return FAIL;
						}
					}
				}
				
				Log.logOpt("ftp-save", useFtp + "|" + host + "|" + port + "|" + username + "|" + password, getRequest());
				return SUCCESS;			
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		return FAIL;
	}
	
	/*
	 * license
	 */
	@RequestMapping(value="/license", method=RequestMethod.POST)
	@ResponseBody
	public String license(String file) {
		if(StringUtils.isNullOrEmpty(file)) {
			return "error_param";
		}
		
		return SUCCESS;
	}
	
	/*
	 * 	 本地升级
	 */
	@RequestMapping(value="/updateLocal", method=RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({ "deprecation", "unchecked" })
	public String updateLocal(HttpServletRequest request) {
		try {
			
			// 判断是否在上课
			if(isClassOpen()) {
				return "warn_class_is_open";
			}			
			
    		// 系统正在升级
    		if(SystemUpdateJob.updating) {
    			return "error_system_updating";
    		}
    		SystemUpdateJob.updating = true;
    		
    		// 判断请求是否包含文件
    		boolean isMultipart = FileUpload.isMultipartContent(request);
            if (!isMultipart) {
                return "error_update_no_file";
            }
        	
        	// 获取所有文件和输入
        	FileItem remoteFile = null;
        	List<FileItem> files = null;
            FileItemFactory factory = new ProgressMonitorFileItemFactory(request, SantrongUtils.getGUID());
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(Global.UploadFileSizeLimit * 1024 * 1024);// 设置允许的最大值
            files = upload.parseRequest(request);// 注意，该工具类会将http头中file类型的表单字段和文本类型的表单字段都封装成FileItem，不过文本类型的字段封装时候内容和名称都设置为空值
	        if (files == null) {
	        	return "error_update_no_file";
	        }
	        
	        // 辨别出升级文件
	        for(FileItem file : files) {
	    		if (!file.isFormField()) {// 如果是不是普通文本类型封装成的FileItem
	    			String fileName = file.getName();
	    			if(!StringUtils.isNullOrEmpty(fileName) && fileName.endsWith("tar.gz")) {
	    		        fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
			            remoteFile = file;
			            break;
	    			}
	    		}
	        }
        
	        // 取不到升级文件
	        if(remoteFile == null){
	        	return "error_update_no_file";
	        }
	        
        	// 远程文件存储到本地
        	File uploadFile = new File(DirDefine.updateFileDir, "update.tar.gz");
        	remoteFile.write(uploadFile);
        	
        	// 发送升级指令
        	SystemUpdateService service = new SystemUpdateService();
        	if(!service.cmdUpdate()) {
        		return FAIL;
        	}
        	
        	Log.logOpt("system-update", "local", request);
        	return "notice_update_success";
        	
        }catch(SizeLimitExceededException e) {
			Log.printStackTrace(e);
			return "error_update_error_file_large";
			
        }catch(Exception e) {
			Log.printStackTrace(e);
        }finally{
        	SystemUpdateJob.updating = false;
        }
		
		return FAIL;
	}
	
	
	/**
	 * 在线升级Get
	 * @return
	 */
	@RequestMapping(value="/updateOnlineGet", method=RequestMethod.GET)
	@ResponseBody
	public String updateOnlineGet() {
		UpdateConfig config = new UpdateConfig();
		Gson gson = new Gson();
		return gson.toJson(config);
	}
	
	/**
	 * 在线升级Post
	 * @return
	 */
	@RequestMapping(value="/updateOnlinePost", method=RequestMethod.POST)
	@ResponseBody
	public String updateOnlinePost(Integer autoUpdate, String hours, String minutes) {
		
		try{
			UpdateConfig config = new UpdateConfig();
			
			// 参数校验
			
			if (StringUtils.isNullOrEmpty(hours) || StringUtils.isNullOrEmpty(minutes) || !Pattern.matches("\\d\\d:\\d\\d", hours + ":" + minutes)) {
				return super.ERROR_PARAM;
			}
			
			if(autoUpdate != null && autoUpdate == 1) {
				config.setAutoUpdate("1");
			}else {
				config.setAutoUpdate("0");
			}
			config.setUpdateTime(hours + ":" + minutes);
			
			
			if(config.write()) {
				
				ScheduleManager scheManager = new ScheduleManager();
				SystemUpdateJob job = new SystemUpdateJob();
				
				// 如果原来有运行调度，停止掉
				if(scheManager.existsCron(job)) {
					if(!scheManager.removeCron(job)) {
						return FAIL;
					}
				}
				
				if("1".equals(config.getAutoUpdate())) {
					// 启动新调度
					if(!scheManager.startCron(job)) {
						return FAIL;
					}
				}
				
				return SUCCESS;
			}
		
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		
		return FAIL;
	}
	
	
	/**
	 * 立刻在线升级
	 * @return
	 */
	@RequestMapping(value="/updateOnlineNow", method=RequestMethod.POST)
	@ResponseBody
	public String updateOnlineNow(HttpServletRequest request) {
		SystemUpdateJob.updating = true;
		
		try{
			
			// 判断是否在上课
			if(isClassOpen()) {
				return "warn_class_is_open";
			}			
			
			SystemUpdateService service = new SystemUpdateService();
	        String rt = service.update();
	        
	        // 打日志
	        if("notice_update_success".equals(rt)){
	        	Log.logOpt("system-update", "online-now", request);
	        }
	        
	        return rt;
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally {
        	SystemUpdateJob.updating = false;
		}
		
		return FAIL;
	}
	
	@RequestMapping(value="/reboot", method=RequestMethod.POST)
	@ResponseBody
	public String reboot(HttpServletRequest request) {
		
		try {
			
			// 判断是否在上课
			if(isClassOpen()) {
				return "warn_class_is_open";
			}
			
			// 重启服务器
			String[] cmd = new String[] { "/bin/sh", "-c", " " + DirDefine.ShellDir + "/systemReboot.sh " };
			
			Runtime.getRuntime().exec(cmd);//只发送指令，不再监控结果
			
			Log.logOpt("system-reboot", "reboot", request);
			
			return "notice_reboot_success";
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		
		return FAIL;
	}
	
	
	/*
	 * 语言设置
	 */
	@RequestMapping(value="/language", method=RequestMethod.POST)
	@ResponseBody
	public String language(String lang) {
		if(StringUtils.isNullOrEmpty(lang)) {
			return "error_param";
		}
		
		return SUCCESS;
	}	
	
	// 判断是否在上课
	public static boolean isClassOpen() {
		String key = MeetingItem.ConfIdPreview + 1;// 该版本只有一路，先写死
		RoomStatusEntry roomStatus = StatusMgr.getRoomStatus(key);
		if(roomStatus == null || roomStatus.getIsLive() == 0) {
			return false;
		}
		return true;
	}
		
}
