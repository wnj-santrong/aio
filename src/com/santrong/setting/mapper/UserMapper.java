package com.santrong.setting.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.setting.entry.UserItem;

/**
 * @Author weinianjie
 * @Date 2014-7-4
 * @Time 下午10:33:09
 */
public interface UserMapper {
	
    @Select("select * from web_user where username=#{username}")
    UserItem selectByUserName(@Param("username") String username);
    
    @Update("update web_user set showName=#{showName}, username=#{username}, password=#{password} where id=#{id}")
    int update(UserItem user);
    
}
