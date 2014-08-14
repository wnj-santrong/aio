package com.santrong.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.meeting.entry.DatasourceItem;

/**
 * @author weinianjie
 * @date 2014年7月23日
 * @time 上午11:31:11
 */
public interface DatasourceMapper {
	
	@Insert("insert into web_datasource (id,addr,port,username,password,meetingId,dsType,priority,cts,uts)values(#{id},#{addr},#{port},#{username},#{password},#{meetingId},#{dsType},#{priority},#{cts},#{uts})")
	int insert(DatasourceItem item);
	
	@Update("update web_datasource set addr=#{addr},port=#{port},username=#{username},password=#{password},meetingId=#{meetingId},dsType=#{dsType},priority=#{priority},cts=#{cts},uts=#{uts} where id=#{id}")
	int update(DatasourceItem item);
	
	@Delete("delete from web_datasource where id=#{id}")
	int delete(String id);
	
	@Select("select * from web_datasource where id=#{id}")
	DatasourceItem selectById(String id);
	
	@Select("select * from web_datasource where meetingId=#{mid} order by priority asc")
	List<DatasourceItem> selectByMeetingId(String mid);
	
	@Select("select * from web_datasource order by priority asc")
	List<DatasourceItem> selectAll();
	
	@Select("select count(*) from web_datasource where meetingId=#{mid} order by priority asc")
	int selectCountByMeetingId(String mid);
}
