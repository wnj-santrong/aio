package com.santrong.home;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.santrong.base.BaseAction;
import com.santrong.home.dao.MenuDao;
import com.santrong.home.entry.MenuItem;

/**
 * @Author weinianjie
 * @Date 2014-7-10
 * @Time 下午10:05:54
 */
@Controller
public class HomeAction extends BaseAction{
	
	@RequestMapping("/index")
	public String index(){
		
		MenuDao menuDao = new MenuDao();
		List<MenuItem> navigator = null;
		
		navigator = menuDao.selectByParentId("0");
		
		if(navigator == null) {
			navigator = new ArrayList<MenuItem>();
		}
		
		request.setAttribute("navigator", navigator);
		
		return "index";
	}
	
	@RequestMapping("/subMenu")
	@ResponseBody
	public String subMenu(String parentId) {
		
		MenuDao menuDao = new MenuDao();
		List<MenuItem> subMenu = menuDao.selectByParentId(parentId);
		
		Gson gson = new Gson();
		
		return gson.toJson(subMenu);
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
