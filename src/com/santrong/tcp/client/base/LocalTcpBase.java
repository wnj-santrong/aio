package com.santrong.tcp.client.base;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.LocalTcpXmlHeader;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午12:50:42
 */
public abstract class LocalTcpBase implements TcpImpl{
	
	private LocalTcpXmlHeader respHeader = new LocalTcpXmlHeader();

	public LocalTcpXmlHeader getRespHeader() {
		return respHeader;
	}
	
	public int getPort(){
		return TcpDefine.Basic_Client_Port;
	}
	
	public String getHost() {
		return TcpDefine.Basic_Client_Addr;
	}
}
