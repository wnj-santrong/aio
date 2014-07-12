package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcp;
import com.santrong.tcp.client.base.AbstractTcpService;
import com.santrong.util.XmlReader;


/**
 * @Author weinianjie
 * @Date 2014-7-8
 * @Time 上午10:44:52
 * @description 本机服务器（本机底层会有C++写的监听服务，JAVA发送TCP请求实现底层接口调用）
 * 向其他类型的服务器发送请求，请继承AbstractTcpClientManager构建新类
 */
public class LocalTcpService extends AbstractTcpService{
	
	private static LocalTcpService instance;
	
	private LocalTcpService(){
	}	
	
	public static LocalTcpService getInstance(){
		if(instance == null){
			instance = new LocalTcpService();
		}
		return instance;
	}
	
	public void request(AbstractTcp service){
		
		// 发送TCP
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, service.toXml());
		
		// 解析返回XML
		XmlReader xml = new XmlReader();
		xml.parse(msgRsp);
		service.getRepHeader().setMsgCode(xml.find("/MsgHead/MsgCode").getText());
		service.getRepHeader().setReturnCode(Integer.parseInt(xml.find("/MsgHead/ReturnCode").getText()));
		service.getRepHeader().setSessionId(xml.find("/MsgHead/SessionId").getText());
		service.resolveXml(xml);
	}
}
