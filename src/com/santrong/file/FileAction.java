package com.santrong.file;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.file.entry.FileQuery;
import com.santrong.file.entry.PlayInfo;
import com.santrong.log.Log;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.Global;
import com.santrong.tcp.client.LocalTcp31010;
import com.santrong.tcp.client.TcpClientService;

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
	@ResponseBody
	public String filePlay(String id) {
		try{
			
			FileDao dao = new FileDao();
			FileItem file = dao.selectById(id);
			if(file == null) {
				return "error_file_not_exists";
			}
			
			UserItem user = this.currentUser();
			if(file.getLevel() == FileItem.File_Level_Close && user == null) {
				return "error_access_deny";
			}
			
			String confId = MeetingItem.ConfIdPreview + file.getChannel();
			String filePath = Global.vedioDir + "/" + confId + "/" + file.getFileName();//全路径		
			
			PlayInfo info = new PlayInfo();
			info.setId(id);
			info.setType(PlayInfo.Type_Vod);
			info.setAddr(InetAddress.getLocalHost().getHostAddress());
			info.setConfId(confId);
			info.setLiveType(0);
			info.setFilePath(filePath);
			
			// 更新播放次数
			file.setPlayCount(file.getPlayCount() + 1);
			dao.update(file);
			
			Gson gson = new Gson();
			return gson.toJson(info);
			
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		return FAIL;
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
			TcpClientService client = TcpClientService.getInstance();
			LocalTcp31010 tcp31010 = new LocalTcp31010();
	
			List<FileItem> fileList = fileDao.selectByIds(idArr);
			if(fileList == null || fileList.size() == 0) {
				return FAIL;
			}
			
			// 先删除实体文件
			for(FileItem file : fileList) {
				String confId = MeetingItem.ConfIdPreview + file.getChannel();
				String rcdName = Global.vedioDir + "/" + confId + "/" + file.getFileName();//全路径
				tcp31010.setConfId(confId);
				tcp31010.setCourseName(rcdName);
				client.request(tcp31010);
				
				if(tcp31010.getRespHeader().getReturnCode() == 1 || tcp31010.getResultCode() == 1) {
					return FAIL;
				}
			}
			
			// 再删除数据库
			if(fileDao.deleteByIds(idArr) <= 0) {
				return FAIL;
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		
		return SUCCESS;
	}
	
	/*
	 * 课件设为公开
	 */
	@RequestMapping(value="/fileOpen", method=RequestMethod.POST)
	@ResponseBody
	public String fileOpen(String ids) {
		if(StringUtils.isNullOrEmpty(ids)) {
			return ERROR_PARAM;
		}
		
		try{
			FileDao fileDao = new FileDao();
			String[] idArr = ids.split(",");
			
			if(fileDao.openByIds(idArr) <= 0) {
				return FAIL;
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		return SUCCESS;
	}
	
	/*
	 * 课件设为关闭
	 */
	@RequestMapping(value="/fileClose", method=RequestMethod.POST)
	@ResponseBody
	public String fileClose(String ids) {
		if(StringUtils.isNullOrEmpty(ids)) {
			return ERROR_PARAM;
		}
		
		try{
			FileDao fileDao = new FileDao();
			String[] idArr = ids.split(",");
			
			if(fileDao.closeByIds(idArr) <= 0) {
				return FAIL;
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
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
