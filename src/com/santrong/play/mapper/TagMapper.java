package com.santrong.play.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.play.entry.TagItem;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 上午11:02:23
 */
public interface TagMapper {
	@Insert("insert into web_tag (id,tagName,priority,cts,uts)values(#{id},#{tagName},#{priority},#{cts},#{uts})")
	int insert(TagItem item);
	
	@Update("update web_tag set tagName=#{tagName},priority=#{priority},cts=#{cts},uts=#{uts} where id=#{id}")
	int update(TagItem item);
	
	@Delete("delete from web_tag where id=#{id}")
	int delete(String id);
	
	@Delete("delete from web_tag where tagName=#{tagName}")
	int deleteByTagName(String tagName);
	
	@Select("select * from web_tag where id=#{id}")
	TagItem selectById(String id);
	
	@Select("select * from web_tag order by priority asc,uts desc")
	List<TagItem> selectAll();
}
