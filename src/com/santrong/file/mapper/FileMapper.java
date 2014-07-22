package com.santrong.file.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.file.entry.FileItem;

/**
 * @Author weinianjie
 * @Date 2014-7-4
 * @Time 下午10:33:09
 */
public interface FileMapper {
    
	@Insert("insert into web_file "
			+ "(id,showName,fileName,courseName,teacher,remark,fileSize,duration,status,level,channel,bitRate,resolution,playCount,downloadCount,cts,uts)"
			+ "values"
			+ "(#{id},#{showName},#{fileName},#{courseName},#{teacher},#{remark},#{fileSize},#{duration},#{status},#{level},#{channel},#{bitRate},#{resolution},#{playCount},#{downloadCount},#{cts},#{uts})")
	int insert(FileItem file);
	
    @Select("select * from web_file where id=#{id}")
    FileItem selectById(@Param("id") String id);
    
    @Select("select * from web_file where status=0 and channel=#{channel} limit 1")
    FileItem selectRecording(int channel);
    
    @Select("select * from web_file where fileName=#{fileName}")
    FileItem selectByFileName(String fileName);
    
    @Delete("delete from web_file where id=#{id}")
    int deleteById(@Param("id") String id);    
    
    @Update("update web_file set showName=#{showName},fileName=#{fileName},courseName=#{courseName},teacher=#{teacher},remark=#{remark},fileSize=#{fileSize},duration=#{duration},status=#{status},level=#{level},channel=#{channel},bitRate=#{bitRate},resolution=#{resolution},playCount=#{playCount},downloadCount=#{downloadCount},uts=#{uts} where id=#{id}")
    int update(FileItem file);
}
