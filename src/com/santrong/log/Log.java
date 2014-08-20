package com.santrong.log;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.santrong.log.dao.OptLogDao;
import com.santrong.log.entry.OptLogItem;
import com.santrong.opt.ThreadUtils;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;
import com.santrong.util.CommonTools;

/**
 * @Author weinianjie
 * @Date 2014-7-5
 * @Time 下午1:16:28
 */
public class Log {
	
	private static final Logger logger = Logger.getLogger("");
	private static final Logger logger_request = Logger.getLogger("request");
	private static final Logger logger_mark = Logger.getLogger("mark");
	
	public static void debug(Object obj) {
		logger.debug(obj);
	}
	
	public static void info(Object obj) {
		logger.info(obj);
	}
	
	public static void warn(Object obj) {
		logger.warn(obj);
	}
	
	public static void error(Object obj) {
		logger.error(obj);
	}
	
	
	public static void logOpt(String title, String content, String username, String ip) {
		OptLogDao dao = new OptLogDao();
		OptLogItem item = new OptLogItem();
		item.setId(CommonTools.getGUID());
		item.setUsername(username);
		item.setTitle(title);
		item.setIp(ip);
		item.setCts(new Date());
		item.setUts(new Date());
		dao.insert(item);
	}
	
	
	public static void logOpt(String title, String content, HttpServletRequest request) {
		OptLogDao dao = new OptLogDao();
		OptLogItem item = new OptLogItem();
		UserItem user = (UserItem)ThreadUtils.currentHttpSession().getAttribute(Global.SessionKey_LoginUser);
		if(user != null) {
			item.setUsername(user.getUsername());
		}else{
			item.setUsername("anonymous");
		}
		item.setId(CommonTools.getGUID());
		item.setTitle(title);
		item.setContent(content);
		item.setIp(CommonTools.getRequestAddrIp(request, "127.0.0.1"));
		item.setCts(new Date());
		item.setUts(new Date());
		dao.insert(item);
	}
	
	public static void logRequest(HttpServletRequest request) {
		// 数据库方式
//		RequestLogDao dao = new RequestLogDao();
//		RequestLogItem item = new RequestLogItem();
//		item.setId(CommonTools.getGUID());
//		item.setUri(request.getRequestURI());
//		item.setParam(request.getQueryString());
//		item.setMethod(request.getMethod());
//		item.setIp(CommonTools.getRequestAddrIp(request, "127.0.0.1"));
//		item.setCts(new Date());
//		item.setUts(new Date());
//		dao.insert(item);
		// 日志文件
		logger_request.info(request.getRequestURI() + " --- "  + request.getQueryString() + " --- " + request.getMethod() + " --- " + CommonTools.getRequestAddrIp(request, "127.0.0.1"));
	}	
	
	public static void mark(Object obj) {
		logger_mark.info(obj);
	}
	
	
	public static void printStackTrace(Throwable e){
		if(e==null){return;}
		StackTraceElement[] stackTraceElement = e.getStackTrace();
		if(stackTraceElement==null){return;}
		logger.error(e.toString());
		for(int i=0 ; i < stackTraceElement.length ;i++){
			logger.error(stackTraceElement[i].toString());
	    }
	}
}
