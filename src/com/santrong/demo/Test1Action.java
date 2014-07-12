package com.santrong.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.demo.dao.DemoDao;
import com.santrong.demo.entry.DemoForm;
import com.santrong.tcp.client.LocalTcp31004;
import com.santrong.tcp.client.LocalTcp31007;
import com.santrong.tcp.client.LocalTcp31009;
import com.santrong.tcp.client.LocalTcp31012;
import com.santrong.tcp.client.LocalTcp31013;
import com.santrong.tcp.client.LocalTcp31016;
import com.santrong.tcp.client.LocalTcpService;

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
		LocalTcpService client = LocalTcpService.getInstance();
		
		// 31001
		
		// 31004
		LocalTcp31004 tcp31004 = new LocalTcp31004();
		
		List<LocalTcp31004.RecStreamInfo> list = new ArrayList<LocalTcp31004.RecStreamInfo>();
		LocalTcp31004.RecStreamInfo info = tcp31004.new RecStreamInfo();
		list.add(info);
		
		tcp31004.setRecStreamInfoList(list);
		
		client.request(tcp31004);
		

		
		// 31007
		LocalTcp31007 tcp31007 = new LocalTcp31007();
		client.request(tcp31007);
//		tcp31007.getConfInfoList().get(0).getRecStreamInfoList().get(0).getStrmAddr();
		
		// 31009
		LocalTcp31009 tcp31009 = new LocalTcp31009();
		client.request(tcp31009);
		
		// 310012
		LocalTcp31012 tcp31012 = new LocalTcp31012();
		client.request(tcp31012);
		
		// 310013
		LocalTcp31013 tcp31013 = new LocalTcp31013();
		client.request(tcp31013);
		
		
		// 310016
		LocalTcp31016 tcp31016 = new LocalTcp31016();
		client.request(tcp31016);
		
		return "index";
	}
	
	
}
