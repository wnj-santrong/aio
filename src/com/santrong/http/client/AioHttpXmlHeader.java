package com.santrong.http.client;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午2:30:54
 */
public class AioHttpXmlHeader {
	private String msgCode;
	private int resultCode = 0;//<!-- 0：失败，1：成功 -->
	
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
