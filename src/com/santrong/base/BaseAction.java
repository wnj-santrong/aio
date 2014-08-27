package com.santrong.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	
	public final UserItem currentUser() {
		UserItem user = (UserItem)ThreadUtils.currentHttpRequest().getSession().getAttribute(Global.SessionKey_LoginUser);
		return user;
	}
	
	public final HttpServletRequest getRequest() {
		return ThreadUtils.currentHttpRequest();
	}
	
	public final HttpServletResponse getResponse() {
		return ThreadUtils.currentHttpResponse();
	}
}
