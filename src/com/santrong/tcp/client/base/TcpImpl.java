package com.santrong.tcp.client.base;

import com.santrong.util.XmlReader;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午2:33:02
 */
public interface TcpImpl {
	String toXml();
	
	void resolveXml(XmlReader xml);
}
