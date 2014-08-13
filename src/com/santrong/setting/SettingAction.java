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
		String[] cmd = new String[] { "cmd.exe  /c mysqldump -uroot -p1996815 -B aio > " + DirDefine.DbBackupDir + File.separator + "111.sql"};
		
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				//SUCCESS
				Log.debug("----------- db back success");
			} else {
				Log.debug("----------- db back fail");
			}
			
			Log.logOpt("db-backup", "", request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return SUCCESS;
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
		
		String[] cmd = new String[] { "/bin/sh", "-c", " /opt/MysqlBackup/mysqlRestore " + filename };
		
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				//SUCCESS
				
			} else {
				
			}
			
		} catch (Exception e) {
			
		}
		
		Log.logOpt("db-restore", filename, request);
		
		return SUCCESS;
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
	public String postFtp(int useFtp, String host, String port, String username, String password, String duration1, String duration2) {
		// 数据校验
		if(StringUtils.isNullOrEmpty(host) || StringUtils.isNullOrEmpty(port) || StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(duration1) || StringUtils.isNullOrEmpty(duration2)) {
			return "error_param";
		}
		
		try{
			// 持久化
			FtpConfig config = new FtpConfig();
			config.setFtpEnable(String.valueOf(useFtp));
			config.setFtpIp(host);
			config.setFtpPort(port);
			config.setUsername(username);
			config.setPassword(password);
			config.setBeginTime(duration1);
			config.setEndTime(duration2);
			if(config.write()) {
	
				// 强行停止，以便job线程重新扫描配置
				FtpHandler handler = FtpHandler.getInstance();
				handler.forceStop();
				FtpUploadJob.uploading = false;
				
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
