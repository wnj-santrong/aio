package com.santrong.plt.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.santrong.plt.entry.FilePushItem;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 上午11:02:23
 */
public interface FilePushMapper {
	@Insert("insert into web_file_push (id,fileId,username,status,cts)values(#{id},#{fileId},#{username},#{status},#{cts})")
	int insert(FilePushItem item);
	
	@Select("select * from web_file_push where id=#{id}")
	FilePushItem selectById(String id);	
	
	@Delete("delete from web_file_push where id=#{id}")
	int delete(String id);
	
	@Select("select count(*) from web_file_push where fileId=#{fileId} and username=#{username} and status=0")
	int existWaiting(@Param("fileId")String fileId, @Param("username")String username);
}
