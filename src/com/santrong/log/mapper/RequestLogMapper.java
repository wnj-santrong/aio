package com.santrong.log.mapper;

import org.apache.ibatis.annotations.Insert;

import com.santrong.log.entry.RequestLogItem;

/**
 * @author weinianjie
 * @date 2014年8月6日
 * @time 下午12:57:37
 */
public interface RequestLogMapper {

	@Insert("insert into web_request_log (id, uri, param, method, ip, cts, uts)values(#{id}, #{uri}, #{param}, #{method}, #{ip}, #{cts}, #{uts})")
	int insert(RequestLogItem log);
}
