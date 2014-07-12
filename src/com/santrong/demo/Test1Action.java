package com.santrong.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.demo.dao.DemoDao;
import com.santrong.demo.entry.DemoForm;
import com.santrong.tcp.client.LocalTcp31004;
import com.santrong.tcp.client.LocalTcpClientService;

@Controller
public class Test1Action extends BaseAction{
	

	
	
	@RequestMapping("fn1")
	public String Fn1() {
		try{
			
			DemoForm form = new DemoForm();
			form.setId("hibernate");
			form.setField1("5");
			
//			ThreadUtil.beginTranx();
			DemoDao dao = new DemoDao();
			dao.jdbc();
//			dao.template();
			dao.ibatis();
//			dao.ibatis2();
//			dao.hibernate();
//			dao.insert(form);
			
		}finally {
//			ThreadUtil.rollbackTranx();
		}
		return "index";
	}
	
	@RequestMapping("fn2")
	public String fn2() {
		LocalTcpClientService client = LocalTcpClientService.getInstance();
		LocalTcp31004 tcp31004 = new LocalTcp31004();
		
		LocalTcp31004.RecStreamInfo info = tcp31004.new RecStreamInfo();
		tcp31004.getRecStreamInfoList().add(info);
		
		client.request(tcp31004);
		
//		client.Login();//31001
//		client.GetConfInfo();//31007
//		client.GetResource();//31009
//		client.TiltCtrl();//310012
//		client.DirectCtrl();//310013
//		client.GetSourceState();//310016
		
		return "index";
	}
	
	
}
