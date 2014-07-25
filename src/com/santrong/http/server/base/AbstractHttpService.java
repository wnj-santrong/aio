package com.santrong.http.server.base;

import com.santrong.util.XmlReader;

/**
 * @Author weinianjie
 * @Date 2014-7-7
 * @Time 下午10:29:13
 */
public interface AbstractHttpService {
	
	String excute(XmlReader xml);
	
}
