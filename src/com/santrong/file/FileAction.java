package com.santrong.file;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.file.entry.FileQuery;
import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.tcp.client.LocalTcp31010;
import com.santrong.tcp.client.TcpService;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/file")
public class FileAction extends BaseAction{
	
	@RequestMapping("/home")
	public String home(String keyword, Integer pageNum){
		if(pageNum == null) {
			pageNum = 0;
		}
		
		FileDao fileDao = new FileDao();
		
		FileQuery query = new FileQuery();
		query.setKeyword(keyword);
		query.setPageNum(pageNum);
		query.setCount(fileDao.selectByPageCount(query));
		List<FileItem> fileList = fileDao.selectByPage(query);
		
		request.setAttribute("query", query);
		request.setAttribute("fileList", fileList);
		return "file/home";
	}
	
	/*
	 * 修改前获取课件信息
	 */
	@RequestMapping(value="/fileEdit", method=RequestMethod.GET)
	public String fileInput(String id) {
		if(StringUtils.isNullOrEmpty(id)){
			return ERROR_PARAM;
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
		FileDao fileDao = new FileDao();
		FileItem dbFile = fileDao.selectById(file.getId());
		
		if(dbFile == null) {
			return ERROR_PARAM;
		}
		
		dbFile.setCourseName(file.getCourseName());
		dbFile.setTeacher(file.getTeacher());
		dbFile.setRemark(file.getRemark());
		dbFile.setUts(new Date());
		
		fileDao.update(dbFile);
		
		return SUCCESS;
	}
	
	/*
	 * 播放课件
	 */
	@RequestMapping("/filePlay")
	public String filePlay(String id) {
		FileDao dao = new FileDao();
		FileItem file = dao.selectById(id);
		if(file == null) {
			return "error_file_not_exists";
		}
		
		request.setAttribute("file", file);
		return "file/play";
	}
	
	/*
	 * 删除课件
	 */
	@RequestMapping(value="/fileDel", method=RequestMethod.POST)
	@ResponseBody
	public String fileDel(String ids) {
		if(StringUtils.isNullOrEmpty(ids)) {
			return ERROR_PARAM;
		}
		
		try{
			
			String[] idArr = ids.split(",");
			
			FileDao fileDao = new FileDao();
			TcpService client = TcpService.getInstance();
			LocalTcp31010 tcp31010 = new LocalTcp31010();
	
			// 先删除实体文件
			for(String id : idArr) {
				tcp31010.setConfId(id);
				client.request(tcp31010);
				
				if(tcp31010.getRespHeader().getReturnCode() == 1 || tcp31010.getResultCode() == 1) {
					return FAIL;
				}
			}
			
			// 再删除数据库
			ThreadUtils.beginTranx();
			for(String id : idArr) {
				if(fileDao.deleteById(id) <= 0) {
					ThreadUtils.rollbackTranx();
					return FAIL;
				}
			}
			ThreadUtils.commitTranx();
		}catch(Exception e) {
			Log.printStackTrace(e);
			ThreadUtils.rollbackTranx();
			return FAIL;
		}
		
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
