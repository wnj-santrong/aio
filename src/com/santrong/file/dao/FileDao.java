package com.santrong.file.dao;

import java.util.List;

import com.santrong.base.BaseDao;
import com.santrong.file.entry.FileItem;
import com.santrong.file.mapper.FileMapper;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 上午10:38:33
 */
public class FileDao extends BaseDao{
	
	public List<FileItem> selectAll() {
		
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.selectAll();
		}
		return null;
	}
	
	public FileItem selectById(String id) {
		
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.selectById(id);
		}
		return null;		
	}
}
