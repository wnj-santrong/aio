package com.santrong.file.mapper;

import java.util.List;

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
			+ "(id,fileName,courseName,teacher,remark,fileSize,tarSize,duration,status,level,channel,bitRate,resolution,playCount,downloadCount,cts,uts)"
			+ "values"
			+ "(#{id},#{fileName},#{courseName},#{teacher},#{remark},#{fileSize},#{tarSize},#{duration},#{status},#{level},#{channel},#{bitRate},#{resolution},#{playCount},#{downloadCount},#{cts},#{uts})")
	int insert(FileItem file);
	
	
    @Select("select * from web_file where id=#{id}")
    FileItem selectById(@Param("id") String id);
    
    
    @Select("select * from web_file where id in (${ids})")
    List<FileItem> selectByIds(@Param("ids") String ids);
    
    @Select("select * from web_file where status = 1 or status = 2")
    List<FileItem> selectToFtp();
    
    
    @Select("select * from web_file where status=0 and channel=#{channel} limit 1")
    FileItem selectRecording(int channel);
    
    
    @Select("select * from web_file where fileName=#{fileName}")
    FileItem selectByFileName(String fileName);
    
    
    @Delete("delete from web_file where id=#{id}")
    int deleteById(@Param("id") String id);    
    
    
    @Delete("delete from web_file where id in (${ids})")
    int deleteByIds(@Param("ids") String ids);
    
    
    @Delete("update web_file set level = 0 where id in (${ids})")
    int openByIds(@Param("ids") String ids);
    
    
    @Delete("update web_file set level = 1 where id in (${ids})")
    int closeByIds(@Param("ids") String ids);
    
    
    @Update("update web_file set fileName=#{fileName},courseName=#{courseName},teacher=#{teacher},remark=#{remark},fileSize=#{fileSize},tarSize=#{tarSize},duration=#{duration},status=#{status},level=#{level},channel=#{channel},bitRate=#{bitRate},resolution=#{resolution},playCount=#{playCount},downloadCount=#{downloadCount},uts=#{uts} where id=#{id}")
    int update(FileItem file);
}
