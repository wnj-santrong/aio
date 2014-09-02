package com.santrong.play;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.log.Log;
import com.santrong.play.dao.TagDao;
import com.santrong.play.entry.TagItem;
import com.santrong.util.SantrongUtils;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/tag")
public class TagAction extends BaseAction{
	
	@RequestMapping(value="/tagGet", method=RequestMethod.GET)
	public String tagGet(String id) {
		TagDao tagDao = new TagDao();
		TagItem tag;
		if(StringUtils.isNullOrEmpty(id)) {
			tag = new TagItem();
		}else {
			tag = tagDao.selectById(id);
		}
		
		getRequest().setAttribute("tag", tag);
		
		return "play/tag";
	}
	
	
	@RequestMapping(value="/tagPost", method=RequestMethod.POST)
	@ResponseBody
	public String tagPost(TagItem tag) {
		TagDao tagDao = new TagDao();
		tag.setUts(new Date());
		
		if(StringUtils.isNullOrEmpty(tag.getId())) {// 新增
			tag.setId(SantrongUtils.getGUID());
			tag.setCts(new Date());;
			if(tagDao.insert(tag) < 1) {
				return FAIL;
			}			
		}else {
			if(tagDao.update(tag) < 1) {// 修改
				return FAIL;
			}
		}


		
		Log.logOpt("tag-insert", tag.getTagName(), getRequest());
		
		return SUCCESS;
	}
	
	
	@RequestMapping(value="/tagDel", method=RequestMethod.POST)
	@ResponseBody
	public String tagDel(String id) {
		TagDao tagDao = new TagDao();
		if(tagDao.delete(id) < 1) {
			return FAIL;
		}
		
		Log.logOpt("tag-delete", id, getRequest());
		
		return SUCCESS;
	}
}
