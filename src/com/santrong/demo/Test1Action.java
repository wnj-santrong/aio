package com.santrong.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.demo.dao.DemoDao;
import com.santrong.demo.entry.DemoForm;
import com.santrong.tcp.client.LocalTcpClientManager;

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
		LocalTcpClientManager client = LocalTcpClientManager.getInstance();
		client.Login();//31001
//		client.GetConfInfo();//31007
//		client.GetResource();//31009
//		client.TiltCtrl();//310012
//		client.DirectCtrl();//310013
//		client.GetSourceState();//310016
		
		return "index";
	}
	
	
}
