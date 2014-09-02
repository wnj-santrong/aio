package com.santrong.home;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.home.dao.MenuDao;
import com.santrong.home.entry.MenuItem;
import com.santrong.log.Log;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;
import com.santrong.system.tip.TipItem;
import com.santrong.system.tip.TipService;
import com.santrong.util.SantrongUtils;

/**
 * @Author weinianjie
 * @Date 2014-7-10
 * @Time 下午10:05:54
 */
@Controller
public class HomeAction extends BaseAction{
	
	@RequestMapping("/index")
	public String index(){
		
		if(this.currentUser() == null){//未登录
			
			return "index";
			
		}else{//已登录
			
			// 获取菜单
			List<MenuItem> navigator = null;
			MenuDao menuDao = new MenuDao();
			navigator = menuDao.selectByParentId("0");
			
			if(navigator == null) {
				navigator = new ArrayList<MenuItem>();
			}
			
			// 获取提醒
			TipService tipService = new TipService();
			List<TipItem> tipList = tipService.getList();
			
			getRequest().setAttribute("navigator", navigator);
			getRequest().setAttribute("tipList", tipList);
			
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
		
		if(!user.getPassword().equals(SantrongUtils.getMD5(password))) {
			return "error_login_password_wrong";
		}
		
		getRequest().getSession().setAttribute(Global.SessionKey_LoginUser, user);
		
		Log.logOpt("user-login", user.getUsername(), getRequest());
		
		return SUCCESS;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public String logout() {
		HttpServletRequest request = getRequest();

		UserItem user = (UserItem)request.getSession().getAttribute(Global.SessionKey_LoginUser);
		if(user == null) {
			return SUCCESS;
		}
		
		try{
			request.getSession().removeAttribute(Global.SessionKey_LoginUser);
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		Log.logOpt("user-out", user.getUsername(), request);
		
		request.getSession().invalidate();
		
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
