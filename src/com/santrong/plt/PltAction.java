package com.santrong.plt;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.http.client.AioHttpClient30001;
import com.santrong.http.client.HttpClientService;
import com.santrong.log.Log;
import com.santrong.plt.dao.FilePushDao;
import com.santrong.plt.entry.FilePushItem;
import com.santrong.plt.entry.FilePushQuery;
import com.santrong.plt.entry.FilePushView;
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
	 * 主页面
	 * @return
	 */
	@RequestMapping("/home")
	public String home(){
		return "plt/home";
	}
	
	/**
	 * 连接配置
	 * @return
	 */
	@RequestMapping("/config")
	public String config(){
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
		return "plt/config";		
	}	
	
	/**
	 * 推送列表
	 * @return
	 */
	@RequestMapping("/pushList")
	public String pushList(Integer pageNum){
		HttpServletRequest request = getRequest();
		if(pageNum == null) {
			pageNum = 0;
		}
		
		FilePushDao dao = new FilePushDao();
		FilePushQuery query = new FilePushQuery();
		query.setHistory(false);
		query.setOrderBy("cts");
		query.setOrderRule("asc");
		query.setPageNum(pageNum);
		query.setCount(dao.selectByPageCount(query));
		List<FilePushView> pushList = dao.selectByPage(query);
		
		request.setAttribute("query", query);
		request.setAttribute("pushList", pushList);
		
		return "plt/pushList";
	}
	
	/**
	 * 推送历史
	 * @return
	 */
	@RequestMapping("/pushHistory")
	public String pushHistory(Integer pageNum){
		
		HttpServletRequest request = getRequest();
		if(pageNum == null) {
			pageNum = 0;
		}
		
		FilePushDao dao = new FilePushDao();
		FilePushQuery query = new FilePushQuery();
		query.setHistory(true);
		query.setOrderBy("cts");
		query.setOrderRule("asc");
		query.setPageNum(pageNum);
		query.setCount(dao.selectByPageCount(query));
		List<FilePushView> pushList = dao.selectByPage(query);
		
		request.setAttribute("query", query);
		request.setAttribute("pushList", pushList);
		
		return "plt/pushHistory";
	}	
	
	/**
	 * 移除推送
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/removePush",method=RequestMethod.POST)
	@ResponseBody
	public String removePush(String id) {
		if(StringUtils.isNullOrEmpty(id)) {
			return ERROR_PARAM;
		}
		
		try{
			FilePushDao dao = new FilePushDao();
			
			// 已经开始推送的不能取消
			FilePushItem item = dao.selectById(id);
			if(item.getStatus() != FilePushItem.File_Push_Status_Wating) {
				return "error_push_has_begin";
			}
			
			if(dao.delete(id) <= 0) {
				return FAIL;
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 保存配置
	 * @param meeting
	 * @return
	 */
	@RequestMapping(value = "/save",method=RequestMethod.POST)
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
