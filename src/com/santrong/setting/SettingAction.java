package com.santrong.setting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.santrong.base.BaseAction;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/setting")
public class SettingAction extends BaseAction{
	
	@RequestMapping("/home")
	public String home(){
		return "setting/home";
	}
	
	
	/*
	 * 管理员修改密码
	 */
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String sysAdminGet() {
		return "system/admin";
	}
	
	/*
	 * 管理员修改密码
	 */
	@RequestMapping(value="/admin", method=RequestMethod.POST)
	public String sysAdminPost(String newname, String newpwd, String oldpwd) {
		newpwd = CommonTools.getMD5(newpwd);
		oldpwd = CommonTools.getMD5(oldpwd);
		UserDao userDao = new UserDao();
		UserItem user = userDao.selectByUserName("admin");
		if(user.getPassword().equals(oldpwd)){
			user.setUsername(newname);
			user.setPassword(newpwd);
			userDao.update(user);
		}else{
			
		}
		return "system/admin";
	}	
}
