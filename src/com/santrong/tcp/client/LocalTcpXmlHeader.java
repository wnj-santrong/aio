package com.santrong.tcp.client;

/**
 * @Author weinianjie
 * @Date 2014-7-12
 * @Time 下午2:30:54
 */
public class LocalTcpXmlHeader {
	private String msgCode;
	private int returnCode = 4;//<!-- 0：成功; 1：密码不正确; 2：消息类型不正确; 3：消息非法; 4：处理失败 -->
	private String sessionId;
	
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
}
