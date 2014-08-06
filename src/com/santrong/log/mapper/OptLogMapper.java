package com.santrong.log.mapper;

import org.apache.ibatis.annotations.Insert;

import com.santrong.log.entry.OptLogItem;

/**
 * @author weinianjie
 * @date 2014年8月6日
 * @time 下午12:58:03
 */
public interface OptLogMapper {
	
	@Insert("insert into web_opt_log (id, username, title, content, ip, cts, uts)values(#{id}, #{username}, #{title}, #{content}, #{ip}, #{cts}, #{uts})")
	int insert(OptLogItem log);
}
