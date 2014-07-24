package com.santrong.play;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.file.entry.FileQuery;
import com.santrong.play.dao.TagDao;
import com.santrong.play.entry.TagItem;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/play")
public class PlayAction extends BaseAction{
	
	@RequestMapping("/home")
	public String home(String keyword, String tag, Integer pageNum){
		if(pageNum == null) {
			pageNum = 0;
		}
		
		FileDao fileDao = new FileDao();
		
		FileQuery query = new FileQuery();
		if(!StringUtils.isNullOrEmpty(tag)) {
			query.setKeyword(tag);
		}else{
			query.setKeyword(keyword);
		}
		query.setPageNum(pageNum);
		query.setPageSize(10);//本页面可以重置分页大小
		query.setCount(fileDao.selectByPageCount(query));
		List<FileItem> fileList = fileDao.selectByPage(query);
		
		TagDao tagDao = new TagDao();
		List<TagItem> tagList = tagDao.selectAll();
		
		request.setAttribute("keyword", keyword);
		request.setAttribute("tag", tag);
		request.setAttribute("query", query);
		request.setAttribute("fileList", fileList);
		request.setAttribute("tagList", tagList);
		return "play/home";
	}
}
