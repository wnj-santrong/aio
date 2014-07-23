package com.santrong.meeting.dao;

import java.util.List;

import com.santrong.base.BaseDao;
import com.santrong.meeting.entry.DatasourceItem;
import com.santrong.meeting.mapper.DatasourceMapper;

/**
 * @author weinianjie
 * @date 2014年7月23日
 * @time 上午11:38:54
 */
public class DatasourceDao extends BaseDao {

	public int insert(DatasourceItem ds) {
		DatasourceMapper mapper = this.getMapper(DatasourceMapper.class);
		if(mapper != null) {
			return mapper.insert(ds);
		}
		return 0;
	}
	
	public int update(DatasourceItem ds) {
		DatasourceMapper mapper = this.getMapper(DatasourceMapper.class);
		if(mapper != null) {
			return mapper.update(ds);
		}
		return 0;
	}
	
	public int delete(String id) {
		DatasourceMapper mapper = this.getMapper(DatasourceMapper.class);
		if(mapper != null) {
			return mapper.delete(id);
		}
		return 0;
	}
	
	public DatasourceItem selectById(String id) {
		DatasourceMapper mapper = this.getMapper(DatasourceMapper.class);
		if(mapper != null) {
			return mapper.selectById(id);
		}
		return null;
	}
	
	public List<DatasourceItem> selectByMeetingId(String mid) {
		DatasourceMapper mapper = this.getMapper(DatasourceMapper.class);
		if(mapper != null) {
			return mapper.selectByMeetingId(mid);
		}
		return null;
	}
	
	public int selectCountByMeetingId(String mid) {
		DatasourceMapper mapper = this.getMapper(DatasourceMapper.class);
		if(mapper != null) {
			return mapper.selectCountByMeetingId(mid);
		}
		return 0;
	}
}
