package com.santrong.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.meeting.entry.MeetingItem;

/**
 * @author weinianjie
 * @date 2014年7月21日
 * @time 上午10:39:53
 */
public interface MeetingMapper {
	
	@Select("select * from web_meeting limit 1")
	MeetingItem selectFirst();
	
	@Select("select * from web_meeting where id=#{id}")
	MeetingItem selectById(String id);
	
	@Select("select * from web_meeting")
	List<MeetingItem> selectAll();
	
	@Update("update web_meeting set showName=#{showName}, courseName=#{courseName}, teacher=#{teacher}, remark=#{remark}, bitRate=#{bitRate}, resolution=#{resolution}, maxTime=#{maxTime}, useRecord=#{useRecord}, recordMode=#{recordMode}, uts=#{uts} where id=#{id}")
	int update(MeetingItem meeting);
	
}
