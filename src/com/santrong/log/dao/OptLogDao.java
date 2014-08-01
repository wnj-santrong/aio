package com.santrong.log.dao;

import com.santrong.base.BaseDao;
import com.santrong.log.Log;
import com.santrong.log.entry.OptLogItem;

/**
 * @author weinianjie
 * @date 2014年8月1日
 * @time 下午3:47:27
 */
public class OptLogDao extends BaseDao{
	
	public int insert(OptLogItem log) {
		try{
			return this.getJdbcTemplate().update("insert into web_opt_log (id, username, title, content, ip, cts, uts)values(?,?,?,?,?,?,?)", 
					new Object[]{log.getId(), log.getUsername(), log.getTitle(), log.getContent(), log.getIp(), log.getCts(), log.getUts()});
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		return 0;
	}
}
