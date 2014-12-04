package com.santrong.http.client.base;

import com.santrong.http.client.AioHttpXmlHeader;
import com.santrong.system.Global;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午12:50:42
 */
public abstract class AioHttpBase implements HttpImpl{
	
	private AioHttpXmlHeader respHeader = new AioHttpXmlHeader();

	public AioHttpXmlHeader getRespHeader() {
		return respHeader;
	}	
	
	public String getUrl(){
		return "http://" + Global.PlatformDomain + "/http/basic";
	}
}
