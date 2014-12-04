package com.santrong.http.client.base;

import com.santrong.http.client.AioHttpXmlHeader;
import com.santrong.util.XmlReader;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午2:33:02
 */
public interface HttpImpl {
	String toXml();
	
	void resolveXml(XmlReader xml);
	
	String getUrl();
	
	AioHttpXmlHeader getRespHeader();
}
