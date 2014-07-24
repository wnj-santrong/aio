package com.santrong.base;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;

/**
 * @Author weinianjie
 * @Date 2014-7-4
 * @Time 下午4:38:21
 */
public abstract class BaseAction {
	protected final String SUCCESS = "success";
	protected final String FAIL = "fail";
	protected final String ERROR_PARAM = "error_param";
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	/**
	 * 前置执行方法
	 * @param request
	 * @param response
	 */
	@ModelAttribute
	public final void init(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		ThreadUtils.setHttpSession(this.request.getSession());
	}		
	
	public final UserItem currentUser() {
		UserItem user = (UserItem)ThreadUtils.currentHttpSession().getAttribute(Global.loginUser_key);
		return user;
	}
	
	/**
	 * 自动附加控制器路径到文件名前面
	 * @param filename
	 * @return
	 */
	protected String findJsp(String filename) {
		Annotation[] anns = this.getClass().getAnnotations();
		for(Annotation a : anns) {
			Log.info("xxx:" + a.annotationType().getName());
		}
		return filename;
	}
}
