package com.santrong.plt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.log.Log;

/**
 * @author weinianjie
 * @date 2014年12月3日
 * @time 下午2:46:51
 */
@Controller
@RequestMapping("/plt")
public class PltAction extends BaseAction {
	
	/**
	 * 连接到云主页面
	 * @return
	 */
	@RequestMapping("/home")
	public String home(){
		PltConfig config = new PltConfig();
		
		//TODO 检查连接到云是否成功
		
		this.getRequest().setAttribute("username", config.getUsername());
		this.getRequest().setAttribute("password", config.getPassword());
		return "plt/home";
	}
	
	/**
	 * 保存
	 * @param meeting
	 * @return
	 */
	@RequestMapping("/save")
	@ResponseBody
	public String save(String username, String password) {
		// 数据校验
		if(StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password)) {
			return "error_param";
		}
		
		try{
			// 持久化
			PltConfig config = new PltConfig();
			config.setUsername(username);
			config.setPassword(password);
			if(config.write()) {
				return SUCCESS;			
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		return FAIL;
	}	
}
