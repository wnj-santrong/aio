package com.santrong.play;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;
import com.santrong.file.entry.FileQuery;
import com.santrong.meeting.dao.MeetingDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.play.dao.TagDao;
import com.santrong.play.entry.LiveEntry;
import com.santrong.play.entry.TagItem;
import com.santrong.system.status.RoomStatusEntry;
import com.santrong.system.status.StatusMgr;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/play")
public class PlayAction extends BaseAction{
	
	private String doHome(String keyword, Integer pageNum, String source) {
		if(pageNum == null) {
			pageNum = 0;
		}
		
		// 获取直播
		MeetingDao meetingDao = new MeetingDao();
		List<MeetingItem> meetingList = meetingDao.selectAll();
		List<LiveEntry> liveList = new ArrayList<LiveEntry>();
		for(MeetingItem m : meetingList) {
			RoomStatusEntry st = StatusMgr.getRoomStatus(MeetingItem.ConfIdPreview + m.getChannel());
			if(st != null && st.getIsConnect() == 1 && st.getIsLive() == 1) {
				LiveEntry entry = new LiveEntry();
				entry.setId(m.getId());
				entry.setCourseName(m.getCourseName());
				liveList.add(entry);
			}
		}
		
		// 获取课件
		FileDao fileDao = new FileDao();
		FileQuery query = new FileQuery();
		query.setKeyword(keyword);
		query.setPageNum(pageNum);
		query.setPrevInsert(liveList.size());
		if(this.currentUser() == null) {// 未登录
			query.setPageSize(15);
			query.setLevel(FileItem.File_Level_Open);
		}else {// 已登录
			query.setPageSize(12);
		}
		query.setCount(fileDao.selectByPageCount(query));
		List<FileItem> fileList = fileDao.selectByPage(query);
		
		// 获取标签
		TagDao tagDao = new TagDao();
		List<TagItem> tagList = tagDao.selectAll();
		
		request.setAttribute("query", query);
		request.setAttribute("liveList", liveList);
		request.setAttribute("fileList", fileList);
		request.setAttribute("tagList", tagList);
		request.setAttribute("source", source);
		return "play/home";
	}
	
	/*
	 * 免登陆
	 */
	@RequestMapping("/homeAnonymous")
	public String homeAnonymous(String keyword, Integer pageNum){
		return this.doHome(keyword, pageNum, "homeAnonymous");
	}
	
	/*
	 * 登录
	 */
	@RequestMapping("/home")
	public String home(String keyword, Integer pageNum){
		return this.doHome(keyword, pageNum, "home");
	}
}
