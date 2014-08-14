package com.santrong.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.setting.entry.UserItem;
import com.santrong.system.DirDefine;
import com.santrong.system.Global;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.LocalTcp31010;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.CommonTools;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/file")
public class FileAction extends BaseAction{
	
	private static final Integer READER_BUFFER_SIZE = 8192;
	
	
	@RequestMapping("/home")
	public String home(String keyword, Integer pageNum){
		if(pageNum == null) {
			pageNum = 0;
		}
		
		FileDao fileDao = new FileDao();
		
		FileQuery query = new FileQuery();
		query.setKeyword(keyword);
		query.setPageNum(pageNum);
		query.setShowRecording(true);// 显示录制中的课件
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
	 * 异步获取课件详细
	 */
	@RequestMapping(value="/fileDetail", method=RequestMethod.GET)
	public String fileDetail(String id) {
		if(StringUtils.isNullOrEmpty(id)){
			return ERROR_PARAM;
		}
		FileDao fileDao = new FileDao();
		FileItem file = fileDao.selectById(id);
		
		request.setAttribute("file", file);
		
		return "file/detail";
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
		
		Log.logOpt("file-modify", dbFile.getId(), request);
		
		return SUCCESS;
	}
	
	/*
	 * 播放课件
	 */
	@RequestMapping("/filePlay")
	@ResponseBody
	public String filePlay(String id, Integer type) {
		try{
			if(type == null) {
				type = 0;
			}
			
			PlayInfo info = new PlayInfo();
			
			// 0点播
			if(type == 0) {
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
				String filePath = DirDefine.VedioDir + "/" + confId + "/" + file.getFileName();//全路径		
				
				info.setId(id);
				info.setType(PlayInfo.Type_Vod);
				info.setAddr(InetAddress.getLocalHost().getHostAddress());
				info.setConfId(confId);
				info.setLiveType(0);
				info.setFilePath(filePath);
				
				// 更新播放次数
				file.setPlayCount(file.getPlayCount() + 1);
				dao.update(file);
				
				Log.logOpt("file-play", file.getId(), request);
			}
			
			// 直播
			if(type == 1) {
				MeetingDao dao = new MeetingDao();
				MeetingItem meeting = dao.selectById(id);
				if(meeting == null) {
					return FAIL;
				}
				
				String confId = MeetingItem.ConfIdPreview + meeting.getChannel();
				RoomStatusEntry status = StatusMgr.getRoomStatus(confId);
				if(status.getIsConnect() == 0 || status.getIsLive() == 0) {
					return FAIL;
				}
				
				info.setId(id);
				if(this.currentUser() != null) {
					info.setType(PlayInfo.Type_Live_Manager);
				}else{
					info.setType(PlayInfo.Type_Live);
				}
				info.setAddr(InetAddress.getLocalHost().getHostAddress());
				info.setConfId(confId);
				info.setLiveType(PlayInfo.LiveType_MD_CMPS);
				info.setFilePath("");
				
				Log.logOpt("meeting-play", meeting.getId(), request);
			}
			
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
				String rcdName = DirDefine.VedioDir + "/" + confId + "/" + file.getFileName();//全路径
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
			
			Log.logOpt("file-delete", ids, request);
			
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
			
			Log.logOpt("file-open", ids, request);
			
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
			
			Log.logOpt("file-close", ids, request);
			
		}catch(Exception e) {
			Log.printStackTrace(e);
			return FAIL;
		}
		return SUCCESS;
	}
	
	/*
	 * 下载课件
	 * 支持断点下载
	 */
	@RequestMapping(value="/fileDownload")
	public void fileDownload(String id) {
		InputStream in = null;
		OutputStream out = null;
		long readed = 0L;
		
		// 代理转发、路由器，是否使用session的key更准确些
		String downloadKey = CommonTools.getRequestAddrIp(request, "127.0.0.1") + "-" + id;
		try{
			// id不正常
			if(StringUtils.isNullOrEmpty(id)) {
				return;
			}
			// 超过服务器配置最大下载值
			if(BreakPointDownloadContent.downloading.size() >= Global.DownloadMaxCount) {
				return;
			}
			// 如果该ip已经正在下载该id的文件了
			if(BreakPointDownloadContent.downloading.contains(downloadKey)) {
				return;
			}
			// 标记用户下载
			BreakPointDownloadContent.downloading.add(downloadKey);
			
			
			FileDao fileDao = new FileDao();
			FileItem file = fileDao.selectById(id);

			String path = DirDefine.VedioDir + "/" + MeetingItem.ConfIdPreview + file.getChannel();
			
			// 获取tar压缩后的大小
			long fileLenth = file.getTarSize();
			if(fileLenth == 0) {
				// 以前没算过tar大小的就要计算
				fileLenth = getTarSize(path, file.getFileName());
				if(fileLenth != 0) {
					// 持久化到数据库下次下载的时候免算
					file.setTarSize(fileLenth);
					fileDao.update(file);
				}
			}
			if(fileLenth == 0) {
				return;
			}
			
			// 获取上次下载的位置
			if (request.getHeader("Range") != null) {
				response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
				readed = Long.parseLong(request.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
			}
			
			// 设置HTTP响应头
			StringBuffer contentRangeTemp = new StringBuffer("bytes ");
			contentRangeTemp.append(new Long(readed).toString()).append("-");
			contentRangeTemp.append(new Long(fileLenth - 1).toString()).append("/");
			contentRangeTemp.append(new Long(fileLenth).toString());
			String contentRange = contentRangeTemp.toString();
			String downloadName = !StringUtils.isNullOrEmpty(file.getCourseName())?  file.getCourseName() : file.getFileName();// 课程名称不为空优先使用课程名称作为下载的文件名
			response.setContentType("application/x-gzip");
			response.setHeader("Content-Disposition","attachment;filename=" + downloadName + ".tar");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Content-Range", contentRange);
			response.setHeader("Content-Length", fileLenth + "");
			
	        // 边压缩边下载
			String[] cmd = new String[] { "/bin/sh", "-c", " cd " + path + " && tar -c " + file.getFileName()};
			Process ps = Runtime.getRuntime().exec(cmd);
			in = ps.getInputStream();
			byte[] buf = new byte[READER_BUFFER_SIZE];
			out = response.getOutputStream();
			int i = 0;
			long oldByte = 0;
			//下载
			while ((i = in.read(buf)) != -1) {
				oldByte += i;
				if (readed != 0 && (oldByte - READER_BUFFER_SIZE) >= readed) {
					out.write(buf, 0, i);
					out.flush();
				} else {
					out.write(buf, 0, i);
					out.flush();
				}
			}
			ps.waitFor();
			
			// 能下载到最后，下载次数+1
			file.setDownloadCount(file.getDownloadCount() + 1);
			file.setUts(new Date());
			fileDao.update(file);
			
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
			BreakPointDownloadContent.downloading.remove(downloadKey);
			try {
				if(in!=null)
				in.close();
				if(out!=null)
				{
				out.flush();
				out.close();
				}
			} catch (IOException ee) {
				Log.printStackTrace(ee);
			}
		}

		Log.logOpt("file-download", id, request);
	}
	
	
	/**
	 * 计算文件或者目录tar压缩以后的大小
	 * @param path
	 * @param fileName，可以是文件也可以是目录
	 * @return
	 * @throws IOException
	 */
	private long getTarSize(String path, String fileName) throws IOException {
		long len = 0;
		String[] cmd = new String[] { "/bin/sh", "-c",
				" cd " + path + " && tar -c " + fileName + " | wc -c" };
		Process process = Runtime.getRuntime().exec(cmd);
		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = reader.readLine()) != null) {
				len = Long.parseLong(line);
			}
			process.waitFor();
		} catch (Exception e) {
			Log.printStackTrace(e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return len;
	}
}
