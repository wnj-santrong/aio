package com.santrong.tcp.client.base;

import com.santrong.tcp.client.LocalTcpXmlHeader;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午12:50:42
 */
public abstract class AbstractTcp implements TcpImpl{
	
	private LocalTcpXmlHeader repHeader = new LocalTcpXmlHeader();
	
	public LocalTcpXmlHeader getRepHeader() {
		return repHeader;
	}

	public void setRepHeader(LocalTcpXmlHeader repHeader) {
		this.repHeader = repHeader;
	}}
