package com.santrong.plt.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.plt.entry.FilePushItem;
import com.santrong.plt.entry.FilePushView;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 上午11:02:23
 */
public interface FilePushMapper {
	@Insert("insert into web_file_push (id,fileId,username,status,remoteId,cts)values(#{id},#{fileId},#{username},#{status},#{remoteId},#{cts})")
	int insert(FilePushItem item);
	
	@Update("update web_file_push set status=#{status} where id=#{id}")
	int updateStatus(@Param("id")String id, @Param("status")int status);	
	
	@Select("select * from web_file_push where id=#{id}")
	FilePushItem selectById(String id);	
	
	@Select("select p.id,p.fileId,p.username,p.status,p.remoteId,f.fileName,f.courseName,f.teacher,f.duration,f.fileSize,f.cts "
			+ "from web_file_push p left join web_file f on p.fileId=f.id where p.status=0 or p.status=1 order by cts asc limit 1")
	FilePushView selectLastOne();
	
	@Delete("delete from web_file_push where id=#{id}")
	int delete(String id);
	
	@Select("select count(*) from web_file_push where fileId=#{fileId} and username=#{username} and status=0")
	int existWaiting(@Param("fileId")String fileId, @Param("username")String username);
}
