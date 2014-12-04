package com.santrong.plt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.http.client.AioHttpClient30001;
import com.santrong.http.client.HttpClientService;
import com.santrong.log.Log;
import com.santrong.util.SantrongUtils;

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
		
		boolean conSuccess = false;
		// 检查链接是否成功
		if(SantrongUtils.isNotNull(config.getUsername()) && SantrongUtils.isNotNull(config.getPassword())) {
			HttpClientService client = HttpClientService.getInstance();
			AioHttpClient30001 http30001 = new AioHttpClient30001();
			http30001.setUsername(config.getUsername());
			http30001.setPassword(config.getPassword());
			client.request(http30001);
			conSuccess = http30001.getRespHeader().getResultCode()==1? true:false;			
		}
		
		this.getRequest().setAttribute("username", config.getUsername());
		this.getRequest().setAttribute("password", config.getPassword());
		this.getRequest().setAttribute("conSuccess", conSuccess);
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
			config.setPassword(SantrongUtils.getMD5(password));// 直接md5加密
			if(config.write()) {
				return SUCCESS;			
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		return FAIL;
	}	
}
