package com.santrong.file.mapper;

import org.apache.ibatis.annotations.Delete;
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
    
    @Select("select * from web_file where id=#{id}")
    FileItem selectById(@Param("id") String id);
    
    @Delete("delete from web_file where id=#{id}")
    int deleteById(@Param("id") String id);    
    
    @Update("update web_file set teacher=#{teacher}, courseName=#{courseName}, remark=#{remark}, uts=#{uts} where id=#{id}")
    int update(FileItem file);
}
