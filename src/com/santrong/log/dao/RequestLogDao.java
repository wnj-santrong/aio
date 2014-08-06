package com.santrong.log.dao;

import com.santrong.base.BaseDao;
import com.santrong.log.entry.RequestLogItem;
import com.santrong.log.mapper.RequestLogMapper;

/**
 * @author weinianjie
 * @date 2014年8月1日
 * @time 下午3:47:14
 */
public class RequestLogDao extends BaseDao{
	
	// 娘的，jdbcTemplate会自动关闭链接的
//	public int insert(RequestLogItem log) {
//		try{
//			return this.getJdbcTemplate().update("insert into web_request_log (id, uri, param, method, ip, cts, uts)values(?,?,?,?,?,?,?)", 
//					new Object[]{log.getId(), log.getUri(), log.getParam(), log.getMethod(), log.getIp(), log.getCts(), log.getUts()});
//		}catch(Exception e) {
//			Log.printStackTrace(e);
//		}
//		return 0;
//	}
	
	public int insert(RequestLogItem log) {
		RequestLogMapper mapper = this.getMapper(RequestLogMapper.class);
		if(mapper != null) {
			return mapper.insert(log);
		}
		return 0;
	}
}
