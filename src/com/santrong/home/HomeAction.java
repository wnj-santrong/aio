package com.santrong.home;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.home.dao.MenuDao;
import com.santrong.home.entry.MenuItem;
import com.santrong.log.Log;
import com.santrong.setting.dao.UserDao;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;
import com.santrong.system.SysUpdateStatusEntry;
import com.santrong.system.SystemUpdateService;
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
			
			// 如果配置不显示链接到云，则菜单要移除
			if(!Global.OpenPlatform && navigator != null) {
				for(int i = 0;i < navigator.size();i++) {
					if("conPlatform".equals(navigator.get(i).getMenuName())) {
						navigator.remove(i);
						break;
					}
				}
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
	
	@RequestMapping(value="/updateProssor", method=RequestMethod.GET)
	public String updateProssor() {
		return "inc/updateprossor";
	}
	
	int getRandom(int max) {
		return new Random().nextInt(max);
	}
	
	@RequestMapping(value="/updateStatus")
	@ResponseBody
	public String updateStatus() {
		Gson gson = new Gson();
		SysUpdateStatusEntry entry = new SysUpdateStatusEntry();
		
		entry.setUpdateSource(SystemUpdateService.updateSource);
        entry.setUploading(SystemUpdateService.uploading);
        entry.setUploadPercent(SystemUpdateService.uploadPercent);
        entry.setUploadResult(SystemUpdateService.uploadResult);
        entry.setUpdating(SystemUpdateService.updating);
        entry.setUpdatePercent(SystemUpdateService.updatePercent);
        entry.setUpdateResult(SystemUpdateService.updateResult);
        
//		int a = getRandom(100);
//		int b = getRandom(100);
//		int c = getRandom(100) > 50? 0 : 1;
//		boolean d = getRandom(100) > 50? true : false;
//		boolean f = getRandom(100) > 50? true : false;
//		
//		entry.setUpdateSource(c);
//        entry.setUploading(d);
//        entry.setUploadPercent(a);
//        entry.setUpdating(f);
//        entry.setUpdatePercent(b);
        
        return gson.toJson(entry);
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
