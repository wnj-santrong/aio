package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.LocalTcpBase;
import com.santrong.tcp.client.base.AbstractTcpService;
import com.santrong.util.XmlReader;


/**
 * @Author weinianjie
 * @Date 2014-7-8
 * @Time 上午10:44:52
 * @description 本机服务器（本机底层会有C++写的监听服务，JAVA发送TCP请求实现底层接口调用）
 * 向其他类型的服务器发送请求，请继承AbstractTcpClientManager构建新类
 */
public class TcpService extends AbstractTcpService{
	
	private static TcpService instance;
	
	private TcpService(){
	}	
	
	public static TcpService getInstance(){
		if(instance == null){
			instance = new TcpService();
		}
		return instance;
	}
	
	public void request(LocalTcpBase service){
		
		// 发送TCP
		String msgRsp = tcpHanlder.sendMsgOnce(service.getHost(), service.getPort(), service.toXml());
		
		// 解析返回XML
		XmlReader xml = new XmlReader();
		xml.parse(msgRsp);
		service.getRespHeader().setMsgCode(xml.find("/MsgHead/MsgCode").getText());
		service.getRespHeader().setReturnCode(Integer.parseInt(xml.find("/MsgHead/ReturnCode").getText()));
		service.getRespHeader().setSessionId(xml.find("/MsgHead/SessionId").getText());
		service.resolveXml(xml);
	}
}
