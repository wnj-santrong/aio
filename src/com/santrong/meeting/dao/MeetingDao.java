package com.santrong.meeting.dao;

import java.util.List;

import com.santrong.base.BaseDao;
import com.santrong.meeting.entry.MeetingItem;
import com.santrong.meeting.mapper.MeetingMapper;

/**
 * @author weinianjie
 * @date 2014年7月21日
 * @time 上午10:40:55
 */
public class MeetingDao extends BaseDao{
	
	public MeetingItem selectFirst() {
		MeetingMapper mapper = this.getMapper(MeetingMapper.class);
		if(mapper != null) {
			return mapper.selectFirst();
		}
		return null;
	}
	
	public MeetingItem selectById(String id) {
		MeetingMapper mapper = this.getMapper(MeetingMapper.class);
		if(mapper != null) {
			return mapper.selectById(id);
		}
		return null;
	}
	
	public List<MeetingItem> selectAll() {
		MeetingMapper mapper = this.getMapper(MeetingMapper.class);
		if(mapper != null) {
			return mapper.selectAll();
		}
		return null;
	}
	
	public int update(MeetingItem meeting) {
		MeetingMapper mapper = this.getMapper(MeetingMapper.class);
		if(mapper != null) {
			return mapper.update(meeting);
		}
		return 0;
	}

}
