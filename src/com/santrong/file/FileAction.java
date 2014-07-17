package com.santrong.file;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/file")
public class FileAction extends BaseAction{
	
	@RequestMapping("/home")
	public String home(){
		FileDao fileDao = new FileDao();
		List<FileItem> fileList = fileDao.selectAll();
		request.setAttribute("fileList", fileList);
		return "file/home";
	}
	
	/*
	 * 修改前获取课件信息
	 */
	@RequestMapping(value="/fileEdit", method=RequestMethod.GET)
	public String fileInput(String id) {
		if(StringUtils.isNullOrEmpty(id)){
			return "";
		}
		
		FileDao fileDao = new FileDao();
		FileItem file = fileDao.selectById(id);
		request.setAttribute("file", file);
		
		return "file/edit";
	}
	
	/*
	 * 修改课件信息
	 */
	@RequestMapping(value="/fileEdit", method=RequestMethod.POST)
	@ResponseBody
	public String fileEdit(FileItem file) {

		
		return SUCCESS;
	}
	
	/*
	 * 修改课件信息
	 */
	@RequestMapping("/filePlay")
	public String filePlay(String id) {

		
		return "file/play";
	}
	
	/*
	 * 删除课件
	 */
	@RequestMapping(value="/fileDel", method=RequestMethod.POST)
	@ResponseBody
	public String fileDel(String ids) {

		
		return SUCCESS;
	}
	
	/*
	 * 下载课件
	 */
	@RequestMapping(value="/fileDownload", method=RequestMethod.POST)
	@ResponseBody
	public String fileDownload(String ids) {

		
		return SUCCESS;
	}	
}
