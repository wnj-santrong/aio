package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;
import com.santrong.tcp.client.base.AbstractTcpService;


/**
 * @Author weinianjie
 * @Date 2014-7-8
 * @Time 上午10:44:52
 * @description 本机服务器（本机底层会有C++写的监听服务，JAVA发送TCP请求实现底层接口调用）
 * 向其他类型的服务器发送请求，请继承AbstractTcpClientManager构建新类
 */
public class LocalTcpClientService extends AbstractTcpService{
	
	private static LocalTcpClientService instance;
	
	private LocalTcpClientService(){
	}	
	
	public static LocalTcpClientService getInstance(){
		if(instance == null){
			instance = new LocalTcpClientService();
		}
		return instance;
	}
	
	public String replaceKey(String source, String key, String val) {
		return source.replaceAll("\\#\\{" + key + "\\}", val);
	}
	
	public void request(AbstractTcpClient service){
		
		// 发送TCP
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, service.toXml());
		
		// 解析返回XML
		service.resolveXml(msgRsp);
	}
}
