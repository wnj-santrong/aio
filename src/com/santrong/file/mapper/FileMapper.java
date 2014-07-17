package com.santrong.file.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.santrong.file.entry.FileItem;

/**
 * @Author weinianjie
 * @Date 2014-7-4
 * @Time 下午10:33:09
 */
public interface FileMapper {
	
    @Select("select * from web_file order by cts desc limit 14")
    List<FileItem> selectAll();
    
}
