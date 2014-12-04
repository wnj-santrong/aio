package com.santrong.http.client;

import com.santrong.http.client.base.AbstractHttpService;
import com.santrong.http.client.base.HttpImpl;
import com.santrong.util.XmlReader;


/**
 * @Author weinianjie
 * @Date 2014-7-8
 * @Time 上午10:44:52
 */
public class HttpClientService extends AbstractHttpService{
	
	private static HttpClientService instance;
	
	private HttpClientService(){
	}	
	
	public static HttpClientService getInstance(){
		if(instance == null){
			instance = new HttpClientService();
		}
		return instance;
	}
	
	public void request(HttpImpl service) {
		
		// 发送POST
		String msgRsp = httpHanlder.sendPost(service.getUrl(), service.toXml().replaceAll(">((?i)null)<", "><"));
		
		// 解析返回XML
		if(msgRsp != null) {
			XmlReader xml = new XmlReader();
			xml.parse(msgRsp);
			service.getRespHeader().setMsgCode(xml.find("/MsgHead/MsgCode").getText());
			service.getRespHeader().setResultCode(Integer.parseInt(xml.find("/MsgHead/ResultCode").getText()));
			service.resolveXml(xml);
		}else{
			service.getRespHeader().setResultCode(0);
		}
	}
}
