package com.santrong.file.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseDao;
import com.santrong.file.entry.FileItem;
import com.santrong.file.entry.FileQuery;
import com.santrong.file.mapper.FileMapper;
import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.util.BeanUtils;
import com.santrong.util.ValidateTools;
import com.santrong.util.criteria.Statement;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 上午10:38:33
 */
public class FileDao extends BaseDao{
	
	public int insert(FileItem file) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.insert(file);
		}
		return 0;		
	}
	
	public FileItem selectById(String id) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.selectById(id);
		}
		return null;		
	}
	
	public List<FileItem> selectByIds(String[] ids) {
		StringBuilder sb = new StringBuilder();
		for(String id:ids) {
			if(ValidateTools.isGUID(id)) {
				sb.append(",'").append(id).append("'");
			}
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(0);
			FileMapper mapper = this.getMapper(FileMapper.class);
			if(mapper != null) {			
				return mapper.selectByIds(sb.toString());
			}
		}

		return null;
	}
	
	public FileItem selectRecording(int channel) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.selectRecording(channel);
		}
		return null;		
	}
	
	public FileItem selectByFileName(String fileName) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.selectByFileName(fileName);
		}
		return null;
	}
	
	public int deleteById(String id) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.deleteById(id);
		}
		return 0;
	}
	
	public int update(FileItem file) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.update(file);
		}
		return 0;
	}
	
	/*
	 * 分页获取课件列表
	 */
	public List<FileItem> selectByPage(FileQuery query) {
		
		List<FileItem> list = new ArrayList<FileItem>();
		
		try{
			Statement criteria = new Statement("web_file", "f");
			// 条件
			if(!StringUtils.isNullOrEmpty(query.getKeyword())) {
				criteria.where(or(
						like("f.showName", "?"),
						like("f.fileName", "?"),
						like("f.courseName", "?"),
						like("f.teacher", "?"),
						like("f.remark", "?")));
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
			}
			if(query.getLevel() != -1) {
				criteria.where(eq("f.level", "?"));
				criteria.setIntParam(query.getLevel());
			}
			// 排序
			if(!StringUtils.isNullOrEmpty(query.getOrderBy())) {
				if("desc".equalsIgnoreCase(query.getOrderRule())) {
					criteria.desc("f." + query.getOrderBy());
				}else {
					criteria.asc("f." + query.getOrderBy());
				}
			}
			
			// 分页
			criteria.limit(query.getBeginIndex(), query.getPageSize());
			
			Log.debug("===============criteria:" + criteria.toString());
			
			Connection conn = ThreadUtils.currentConnection();
			PreparedStatement stm = criteria.getRealStatement(conn);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				FileItem item = new FileItem();
				BeanUtils.Rs2Bean(rs, item);
				list.add(item);
			}
			
		}catch(Exception e){
			Log.printStackTrace(e);
		}
		
		return list;
	}
	
	/*
	 * 分页获取课件列表总数
	 */
	public int selectByPageCount(FileQuery query) {
		
		int count = 0;
		
		try{
			Statement criteria = new Statement("web_file", "f");
			criteria.setFields("count(*) cn");
			// 条件
			if(!StringUtils.isNullOrEmpty(query.getKeyword())) {
				criteria.where(or(
						like("f.showName", "%" + query.getKeyword() + "%"),
						like("f.fileName", "%" + query.getKeyword() + "%"),
						like("f.courseName", "%" + query.getKeyword() + "%"),
						like("f.teacher", "%" + query.getKeyword() + "%"),
						like("f.remark", "%" + query.getKeyword() + "%")));
			}
			if(query.getLevel() != -1) {
				criteria.where(eq("f.level", "?"));
				criteria.setIntParam(query.getLevel());
			}
			
			Log.debug("===============criteria:" + criteria.toString());
			
			Connection conn = ThreadUtils.currentConnection();
			PreparedStatement stm = criteria.getRealStatement(conn);
			ResultSet rs = stm.executeQuery();
			rs.next();
			count = rs.getInt("cn");
			
		}catch(Exception e){
			Log.printStackTrace(e);
		}
		
		return count;
	}	
	
}
