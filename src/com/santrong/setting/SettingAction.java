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
import com.santrong.log.Log;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;
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
			UserItem user = userDao.selectByUserName("admin");
			if(user.getPassword().equals(oldpwd)){
				user.setShowName(newname);
				user.setUsername(newname);
				user.setPassword(newpwd);
				userDao.update(user);
			}else{
				return "error_oldpwd";
			}
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
		
		List<File> dbList = FileUtils.searchFile(Global.dbBackupDir, "sql");
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
		String[] cmd = new String[] { "cmd.exe  /c mysqldump -uroot -p1996815 -B aio > " + Global.dbBackupDir + File.separator + "111.sql"};
		
		try {
			Process ps = Runtime.getRuntime().exec(cmd);
			
			if (ps.waitFor() == 0) {
				//SUCCESS
				Log.debug("----------- db back success");
			} else {
				Log.debug("----------- db back fail");
			}
			
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
		
		if(!FileUtils.delFile(Global.dbBackupDir + File.separator + filename)) {
			return FAIL;
		}
		
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
		
		return SUCCESS;
	}	
	
	/*
	 * 网络设置
	 */
	@RequestMapping(value="/network", method=RequestMethod.POST)
	@ResponseBody
	public String network(int type, String ip, String mask, String gateway) {
		if(StringUtils.isNullOrEmpty(ip) || StringUtils.isNullOrEmpty(mask) || StringUtils.isNullOrEmpty(gateway)) {
			return "error_param";
		}
		
		return SUCCESS;
	}	
	
	/*
	 * 网络设置
	 */
	@RequestMapping(value="/ftp", method=RequestMethod.POST)
	@ResponseBody
	public String ftp(int useFtp, String host, String port, String username, String password, String duration1, String duration2) {
		if(StringUtils.isNullOrEmpty(host) || StringUtils.isNullOrEmpty(port) || StringUtils.isNullOrEmpty(password)) {
			return "error_param";
		}
		
		return SUCCESS;
	}
	
	/*
	 * 网络设置
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
	 * 网络设置
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public String update(String file) {
		if(StringUtils.isNullOrEmpty(file)) {
			return "error_param";
		}
		
		return SUCCESS;
	}
	
	/*
	 * 网络设置
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
