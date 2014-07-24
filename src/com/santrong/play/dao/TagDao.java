package com.santrong.play.dao;

import java.util.List;

import com.santrong.base.BaseDao;
import com.santrong.play.entry.TagItem;
import com.santrong.play.mapper.TagMapper;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 上午11:07:23
 */
public class TagDao extends BaseDao {
	
	public int insert(TagItem item) {
		TagMapper mapper = this.getMapper(TagMapper.class);
		if(mapper != null) {
			return mapper.insert(item);
		}
		return 0;
	}
	
	public int update(TagItem item) {
		TagMapper mapper = this.getMapper(TagMapper.class);
		if(mapper != null) {
			return mapper.insert(item);
		}
		return 0;
	}
	
	public int delete(String id) {
		TagMapper mapper = this.getMapper(TagMapper.class);
		if(mapper != null) {
			return mapper.delete(id);
		}
		return 0;
	}
	
	public int deleteByTagName(String tagName) {
		TagMapper mapper = this.getMapper(TagMapper.class);
		if(mapper != null) {
			return mapper.deleteByTagName(tagName);
		}
		return 0;
	}
	
	public TagItem selectById(String id) {
		TagMapper mapper = this.getMapper(TagMapper.class);
		if(mapper != null) {
			return mapper.selectById(id);
		}
		return null;
	}
	
	public List<TagItem> selectAll() {
		TagMapper mapper = this.getMapper(TagMapper.class);
		if(mapper != null) {
			return mapper.selectAll();
		}
		return null;
	}

}
