package com.santrong.tcp.client.base;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:38:43
 */
public abstract  class AbstractTcpClient {
	
	
	protected static final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public String replaceKey(String source, String key, String val) {
		return source.replaceAll("\\#\\{" + key + "\\}", val);
	}
	
}
