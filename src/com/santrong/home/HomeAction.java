package com.santrong.home;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.home.dao.MenuDao;
import com.santrong.home.entry.MenuItem;
import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;
import com.santrong.util.CommonTools;

/**
 * @Author weinianjie
 * @Date 2014-7-10
 * @Time 下午10:05:54
 */
@Controller
public class HomeAction extends BaseAction{
	
	@RequestMapping("/index")
	public String index(){
		
		if(this.currentUser() == null){
			//未登录
			return "index";
			
		}else{
			//已登录
			MenuDao menuDao = new MenuDao();
			List<MenuItem> navigator = null;
			
			navigator = menuDao.selectByParentId("0");
			
			if(navigator == null) {
				navigator = new ArrayList<MenuItem>();
			}
			
			request.setAttribute("navigator", navigator);
			
			return "manage";
			
		}
		

	}
	
	@RequestMapping(value="/loginForm", method=RequestMethod.GET)
	public String loginForm() {
		
		return "inc/loginform";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginGet() {
		
		return "login";
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public String loginPOST(String username, String password) {
		if(StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password)) {
			return "error_login_nullInput";
		}
		
		UserDao userDao = new UserDao();
		UserItem user = userDao.selectByUserName(username);
		
		if(user == null) {
			return "error_login_user_not_exists";
		}
		
		if(!user.getPassword().equals(CommonTools.getMD5(password))) {
			return "error_login_password_wrong";
		}
		
		ThreadUtils.currentHttpSession().setAttribute(Global.LoginUser_key, user);
		
		Log.logOpt("user-login", user.getUsername(), request);
		
		return SUCCESS;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public String logout() {

		UserItem user = (UserItem)ThreadUtils.currentHttpSession().getAttribute(Global.LoginUser_key);
		if(user == null) {
			return SUCCESS;
		}
		
		try{
			ThreadUtils.currentHttpSession().removeAttribute(Global.LoginUser_key);
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		ThreadUtils.currentHttpSession().invalidate();
		
		Log.logOpt("user-out", user.getUsername(), request);
		
		return SUCCESS;
	}
	
	@RequestMapping("/404")
	public String page404() {
		return "404";
	}
	
	@RequestMapping("/500")
	public String page500() {
		return "404";
	}
}
