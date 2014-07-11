package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClientManager;
import com.santrong.tcp.client.base.MsgHeader;


/**
 * @Author weinianjie
 * @Date 2014-7-8
 * @Time 上午10:44:52
 * @description 本机服务器（本机底层会有C++写的监听服务，JAVA发送TCP请求实现底层接口调用）
 * 向其他类型的服务器发送请求，请继承AbstractTcpClientManager构建新类
 */
public class LocalTcpClientManager extends AbstractTcpClientManager{
	
	private static LocalTcpClientManager instance;
	
	private LocalTcpClientManager(){
	}	
	
	public static LocalTcpClientManager getInstance(){
		if(instance == null){
			instance = new LocalTcpClientManager();
		}
		return instance;
	}
	
	public String replaceKey(String source, String key, String val) {
		return source.replaceAll("\\#\\{" + key + "\\}", val);
	}
	

	public void Login() {
		LocalTcpClient31001 client = new LocalTcpClient31001();
		String majorXml = client.getMajorXml();
		majorXml = this.replaceKey(majorXml, "Addr", "192.168.10.102");
		majorXml = this.replaceKey(majorXml, "Port", "3000");
		majorXml = this.replaceKey(majorXml, "Heartheat", "5");

		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
		
		//TODO 解析msgRsp
	}
	
	public void Logout() {
		LocalTcpClient31002 client = new LocalTcpClient31002();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void StartConfRecord() {
		LocalTcpClient31004 client = new LocalTcpClient31004();
		String majorXml = client.getMajorXml();
		majorXml = this.replaceKey(majorXml, "Heartheat", "5");
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void StopConfRecord() {
		LocalTcpClient31005 client = new LocalTcpClient31005();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void RecordCtl() {
		LocalTcpClient31006 client = new LocalTcpClient31006();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void GetConfInfo() {
		LocalTcpClient31007 client = new LocalTcpClient31007();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void SetThreshold() {
		LocalTcpClient31008 client = new LocalTcpClient31008();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void GetResource() {
		LocalTcpClient31009 client = new LocalTcpClient31009();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void DeleteCourse() {
		LocalTcpClient31010 client = new LocalTcpClient31010();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void SetLogLevel() {
		LocalTcpClient31011 client = new LocalTcpClient31011();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void TiltCtrl() {
		LocalTcpClient31012 client = new LocalTcpClient31012();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void DirectCtrl() {
		LocalTcpClient31013 client = new LocalTcpClient31013();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void AddSource() {
		LocalTcpClient31014 client = new LocalTcpClient31014();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void DelSource() {
		LocalTcpClient31015 client = new LocalTcpClient31015();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
	
	public void GetSourceState() {
		LocalTcpClient31016 client = new LocalTcpClient31016();
		String majorXml = client.getMajorXml();
		String msgRsp = tcpHanlder.sendMsgOnce("192.168.10.10", TcpDefine.Basic_Client_Port, majorXml);
	}	
}
