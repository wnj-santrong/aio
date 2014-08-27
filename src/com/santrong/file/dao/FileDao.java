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
	
	/*
	 * 组合ID数组，可以防止注入
	 */
	private String consistIds(String[] ids) {
		StringBuilder sb = new StringBuilder();
		for(String id:ids) {
			if(ValidateTools.isGUID(id)) {
				sb.append(",'").append(id).append("'");
			}
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(0);
			return sb.toString();
		}

		return null;
	}
	
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
		String _ids = consistIds(ids);
		if(_ids != null) {
			FileMapper mapper = this.getMapper(FileMapper.class);
			if(mapper != null) {			
				return mapper.selectByIds(_ids);
			}
		}
		return null;
	}
	
	public List<FileItem> selectToFtp() {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {			
			return mapper.selectToFtp();
		}
		return null;
	}	
	
	public List<FileItem> selectByStatus(int status) {
		FileMapper mapper = this.getMapper(FileMapper.class);
		if(mapper != null) {
			return mapper.selectByStatus(status);
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
	
	public int deleteByIds(String[] ids) {
		String _ids = consistIds(ids);
		if(_ids != null) {
			FileMapper mapper = this.getMapper(FileMapper.class);
			if(mapper != null) {			
				return mapper.deleteByIds(_ids);
			}
		}
		return 0;
	}
	
	public boolean hasFileRecording(String[] ids) {
		String _ids = consistIds(ids);
		if(_ids != null) {
			FileMapper mapper = this.getMapper(FileMapper.class);
			if(mapper != null) {			
				return mapper.hasFileRecording(_ids) > 0;
			}
		}
		return false;
	}
	
	public int openByIds(String[] ids) {
		String _ids = consistIds(ids);
		if(_ids != null) {
			FileMapper mapper = this.getMapper(FileMapper.class);
			if(mapper != null) {			
				return mapper.openByIds(_ids);
			}
		}
		return 0;
	}
	
	public int closeByIds(String[] ids) {
		String _ids = consistIds(ids);
		if(_ids != null) {
			FileMapper mapper = this.getMapper(FileMapper.class);
			if(mapper != null) {			
				return mapper.closeByIds(_ids);
			}
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
						like("f.fileName", "?"),
						like("f.courseName", "?"),
						like("f.teacher", "?"),
						like("f.remark", "?")));
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
			}
			if(query.getLevel() != -1) {
				criteria.where(eq("f.level", "?"));
				criteria.setIntParam(query.getLevel());
			}
			if(!query.isShowRecording()) {
				criteria.where(ne("f.status", FileItem.File_Status_Recording));// 录制中课件不显示
			}
			if(!query.isShowError()) {
				criteria.where(ne("f.status", FileItem.File_Status_ERROR));// 异常课件不显示
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
			criteria.limit(query.getLimitBegin(), query.getLimitEnd());
			
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
						like("f.fileName", "?"),
						like("f.courseName", "?"),
						like("f.teacher", "?"),
						like("f.remark", "?")));
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
				criteria.setStringParam("%" + query.getKeyword() + "%");
			}
			if(query.getLevel() != -1) {
				criteria.where(eq("f.level", "?"));
				criteria.setIntParam(query.getLevel());
			}
			if(!query.isShowRecording()) {
				criteria.where(ne("f.status", FileItem.File_Status_Recording));// 显示录制中课件
			}
			if(!query.isShowError()) {
				criteria.where(ne("f.status", FileItem.File_Status_ERROR));// 异常课件不显示
			}
			
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
