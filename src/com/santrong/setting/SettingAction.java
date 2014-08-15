package com.santrong.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.santrong.opt.ThreadUtils;
import com.santrong.schedule.FtpUploadJob;
import com.santrong.schedule.ScheduleManager;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.DirDefine;
import com.santrong.system.Global;
import com.santrong.system.network.NetworkInfo;
import com.santrong.system.network.SystemUtils;
import com.santrong.util.CommonTools;
import com.santrong.util.FileUtils;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/setting")
public class SettingAction extends BaseAction{
	
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
	public String userUpdate(String newname, String newpwd, String oldpwd) {
		if(StringUtils.isNullOrEmpty(newname) || StringUtils.isNullOrEmpty(newpwd) || StringUtils.isNullOrEmpty(oldpwd)) {
			return "error_param";
		}
		
		newpwd = CommonTools.getMD5(newpwd);
		oldpwd = CommonTools.getMD5(oldpwd);
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
				ThreadUtils.currentHttpSession().setAttribute(Global.LoginUser_key, newuser);
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
		String[] cmd = new String[] { "/bin/sh", "-c", " /opt/AIO/Service/webservice/shell/dbBackup.sh " };
		
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				Log.debug("----------- db back success");
				Log.logOpt("db-backup", "", request);
				return SUCCESS;
			} else {
				Log.debug("----------- db back fail");
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
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
		
		Log.logOpt("db-delete", filename, request);
		
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
		
		String[] cmd = new String[] { "/bin/sh", "-c", " /opt/AIO/Service/webservice/shell/dbRestore.sh " + filename };
		
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				Log.debug("----------- db restore success");
				Log.logOpt("db-restore", "", request);
				return SUCCESS;
			} else {
				Log.debug("----------- db restore fail");
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		
		Log.logOpt("db-restore", filename, request);
		
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
		
		NetworkInfo info = new NetworkInfo();
		info.setIndex(type);
		info.setIp(ip);
		info.setMask(mask);
		info.setGateway(gateway);
		
		if(!SystemUtils.setNetwork(info)) {
			return FAIL;
		}
		
		// TODO 重启网卡
		
		Log.logOpt("net-save", String.valueOf(type), request);
		
		return SUCCESS;
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
		if(StringUtils.isNullOrEmpty(host) || StringUtils.isNullOrEmpty(port) || StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(duration)) {
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
				
				Log.logOpt("ftp-save", useFtp + "|" + host + "|" + port + "|" + username + "|" + password, request);
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
	 * 系统升级
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public String update(String file) {
		if(StringUtils.isNullOrEmpty(file)) {
			return "error_param";
		}
		
		Log.logOpt("system-update", file, request);
		
		return SUCCESS;
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
		
}
