package com.santrong.meeting;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.meeting.dao.DatasourceDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月23日
 * @time 下午2:55:44
 */
@Controller
@RequestMapping("/datasource")
public class DatasourceAction extends BaseAction {
	
	
	/**
	 * 数据源GET
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/dsGet", method=RequestMethod.GET)
	public String dsGet(String id, String meetingId) {
		if(StringUtils.isNullOrEmpty(meetingId)){
			return ERROR_PARAM;
		}
		
		DatasourceItem ds;
		if(!StringUtils.isNullOrEmpty(id)){
			DatasourceDao dsDao = new DatasourceDao();
			ds = dsDao.selectById(id);
		}else{
			ds = new DatasourceItem();
			ds.setMeetingId(meetingId);
		}

		request.setAttribute("ds", ds);
		
		return "meeting/datasource";
	}
	
	/**
	 * 数据源POST
	 * @param file
	 * @return
	 */
	@RequestMapping(value="/dsPost", method=RequestMethod.POST)
	@ResponseBody
	public String dsPost(DatasourceItem ds) {
		DatasourceDao dsDao = new DatasourceDao();
		
		ds.setUts(new Date());
		ds.setDsType(DatasourceItem.Datasoruce_Type_Camera);
		if(StringUtils.isNullOrEmpty(ds.getId())) {
			ds.setId(CommonTools.getGUID());
			ds.setCts(new Date());
			if(dsDao.insert(ds) < 1) {
				return FAIL;
			}
		}else{
			if(dsDao.update(ds) < 1) {
				return FAIL;
			}
		}

		
		return SUCCESS;
	}
	
	/**
	 * 删除数据源
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/dsDel", method=RequestMethod.POST)
	@ResponseBody
	public String dsDel(String id) {
		if(StringUtils.isNullOrEmpty(id)) {
			return FAIL;
		}
		
		DatasourceDao dsDao = new DatasourceDao();
		if(dsDao.delete(id) < 1) {
			return FAIL;
		}
		return SUCCESS;
	}
	
}
