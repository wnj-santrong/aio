package com.santrong.plt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.santrong.base.BaseDao;
import com.santrong.log.Log;
import com.santrong.opt.ThreadUtils;
import com.santrong.plt.entry.FilePushItem;
import com.santrong.plt.entry.FilePushQuery;
import com.santrong.plt.entry.FilePushView;
import com.santrong.plt.mapper.FilePushMapper;
import com.santrong.util.BeanUtils;
import com.santrong.util.criteria.Statement;

/**
 * @author weinianjie
 * @date 2014年12月5日
 * @time 上午11:11:08
 */
public class FilePushDao extends BaseDao {
	
	public int insert(FilePushItem item) {
		FilePushMapper mapper = this.getMapper(FilePushMapper.class);
		if(mapper != null) {
			return mapper.insert(item);
		}
		return 0;
	}
	
	public int updateStatus(String id, int status) {
		FilePushMapper mapper = this.getMapper(FilePushMapper.class);
		if(mapper != null) {
			return mapper.updateStatus(id, status);
		}
		return 0;
	}	
	
	public FilePushItem selectById(String id) {
		FilePushMapper mapper = this.getMapper(FilePushMapper.class);
		if(mapper != null) {
			return mapper.selectById(id);
		}
		return null;
	}
	
	public FilePushView selectLastOne() {
		FilePushMapper mapper = this.getMapper(FilePushMapper.class);
		if(mapper != null) {
			return mapper.selectLastOne();
		}
		return null;
	}
	
	public int delete(String id) {
		FilePushMapper mapper = this.getMapper(FilePushMapper.class);
		if(mapper != null) {
			return mapper.delete(id);
		}
		return 0;
	}
	
	public boolean existWaiting(String fileId, String username) {
		FilePushMapper mapper = this.getMapper(FilePushMapper.class);
		if(mapper != null) {
			return mapper.existWaiting(fileId, username) > 0;
		}
		return false;
	}
	

	/*
	 * 分页获取推送列表
	 */
	public List<FilePushView> selectByPage(FilePushQuery query) {
		
		List<FilePushView> list = new ArrayList<FilePushView>();
		
		try{
			Statement criteria = new Statement("web_file_push", "p");
			criteria.setFields("p.id,p.fileId,p.username,p.status,p.remoteId,f.fileName,f.courseName,f.teacher,f.duration,f.fileSize,f.cts");
			criteria.ljoin("web_file", "f", "p.fileId", "f.id");
			// 条件
			if(query.isHistory()) {
				criteria.where(or(eq("p.status", FilePushItem.File_Push_Status_Done),
						eq("p.status", FilePushItem.File_Push_Status_Error)));
			}else{
				criteria.where(or(eq("p.status", FilePushItem.File_Push_Status_Wating),
						eq("p.status", FilePushItem.File_Push_Status_Pushing)));
			}
			// 排序
			if(!StringUtils.isNullOrEmpty(query.getOrderBy())) {
				if("desc".equalsIgnoreCase(query.getOrderRule())) {
					criteria.desc("p." + query.getOrderBy());
				}else {
					criteria.asc("p." + query.getOrderBy());
				}
			}
			
			// 分页
			criteria.limit(query.getLimitBegin(), query.getLimitEnd());
			
			Connection conn = ThreadUtils.currentConnection();
			PreparedStatement stm = criteria.getRealStatement(conn);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				FilePushView item = new FilePushView();
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
	public int selectByPageCount(FilePushQuery query) {
		
		int count = 0;
		
		try{
			Statement criteria = new Statement("web_file_push", "p");
			criteria.setFields("count(*) cn");
			criteria.ljoin("web_file", "f", "p.fileId", "f.id");
			// 条件
			if(query.isHistory()) {
				criteria.where(or(eq("p.status", FilePushItem.File_Push_Status_Done),
						eq("p.status", FilePushItem.File_Push_Status_Error)));
			}else{
				criteria.where(or(eq("p.status", FilePushItem.File_Push_Status_Wating),
						eq("p.status", FilePushItem.File_Push_Status_Pushing)));
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
