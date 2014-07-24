package com.santrong.play;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.santrong.base.BaseAction;
import com.santrong.play.dao.TagDao;
import com.santrong.play.entry.TagItem;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/tag")
public class TagAction extends BaseAction{
	
	@RequestMapping(value="/tagGet", method=RequestMethod.GET)
	public String tagGet() {
		return "play/tag";
	}
	
	
	@RequestMapping(value="/tagPost", method=RequestMethod.POST)
	@ResponseBody
	public String tagPost(TagItem tag) {
		TagDao tagDao = new TagDao();
		tag.setId(CommonTools.getGUID());
		tag.setCts(new Date());;
		tag.setUts(new Date());
		if(tagDao.insert(tag) < 1) {
			return FAIL;
		}
		return SUCCESS;
	}
	
	
	@RequestMapping(value="/tagDel", method=RequestMethod.POST)
	@ResponseBody
	public String tagDel(String tagName) {
		TagDao tagDao = new TagDao();
		if(tagDao.deleteByTagName(tagName) < 1) {
			return FAIL;
		}
		return SUCCESS;
	}
}
