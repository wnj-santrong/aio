package com.santrong.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;
import com.santrong.demo.dao.DemoDao;
import com.santrong.demo.entry.DemoForm;
import com.santrong.tcp.client.TcpService;

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
		TcpService client = TcpService.getInstance();
		
		// 31001
//		LocalTcp31001 tcp31001 = new LocalTcp31001();
//		tcp31001.setAddr("192.168.10.102");
//		tcp31001.setHeartbeat(5);
//		tcp31001.setPort(80);
//		client.request(tcp31001);
		
//		LocalTcp31002 tcp31002 = new LocalTcp31002();
//		client.request(tcp31002);			
		
		
		// 31004
//		LocalTcp31004 tcp31004 = new LocalTcp31004();
//		
//		List<LocalTcp31004.RecStreamInfo> list = new ArrayList<LocalTcp31004.RecStreamInfo>();
//		LocalTcp31004.RecStreamInfo info = tcp31004.new RecStreamInfo();
//		info.setStrmAddr("192.168.8.8");
//		info.setStrmPort(80);
//		info.setStrmUser("weinianjie");
//		info.setStrmFRate(1);
//		list.add(info);
//		
//		tcp31004.setRecStreamInfoList(list);
//		tcp31004.setConfId("12345");
//		tcp31004.setCourseName("语文课");
//		tcp31004.setCourseAbs("这里是课程概要");
//		tcp31004.setTeacher("王老师");
//		tcp31004.setIsLive(1);
//		tcp31004.setIsLive(1);
//		tcp31004.setLayout(1);
//		tcp31004.setRecordType(1);
//		
//		client.request(tcp31004);
		
		// 31005
//		LocalTcp31005 tcp31005 = new LocalTcp31005();
//		tcp31005.setConfId("1234");
//		client.request(tcp31005);	
		
		// 31006
//		LocalTcp31006 tcp31006 = new LocalTcp31006();
//		tcp31006.setConfId("1234");
//		tcp31006.setOperType(1);
//		client.request(tcp31006);	
		
		// 31007
//		LocalTcp31007 tcp31007 = new LocalTcp31007();
//		client.request(tcp31007);
		
		// 31008
//		LocalTcp31008 tcp31008 = new LocalTcp31008();
//		tcp31008.setFreeSize(234);
//		tcp31008.setMaxTime(2222);
//		client.request(tcp31008);		
		
		// 31009
//		LocalTcp31009 tcp31009 = new LocalTcp31009();
//		client.request(tcp31009);
		
		// 31010
//		LocalTcp31010 tcp31010 = new LocalTcp31010();
//		tcp31010.setConfId("1111");
//		tcp31010.setCourseName("数学课");
//		client.request(tcp31010);
		
		// 31011
//		LocalTcp31011 tcp31011 = new LocalTcp31011();
//		tcp31011.setLogLevel(1);
//		client.request(tcp31011);
		
		// 310012
//		LocalTcp31012 tcp31012 = new LocalTcp31012();
//		tcp31012.setConfId("111111");
//		tcp31012.setCamAddr(0);
//		client.request(tcp31012);

		// 310013
//		LocalTcp31013 tcp31013 = new LocalTcp31013();
//		tcp31013.setConfId("11111111");
//		tcp31013.setStrmId(1);
//		client.request(tcp31013);
		
		// 31014
//		LocalTcp31014 tcp31014 = new LocalTcp31014();
//		tcp31014.setSrcAddr("127.0.0.1");
//		tcp31014.setSrcPort(80);
//		tcp31014.setSrcPw("800");
//		client.request(tcp31014);
		
		// 31015
//		LocalTcp31015 tcp31015 = new LocalTcp31015();
//		tcp31015.setSrcAddr("127.0.0.1");
//		client.request(tcp31015);		
		
		// 31016
//		LocalTcp31016 tcp31016 = new LocalTcp31016();
//		tcp31016.setSrcAddr("127.0.0.1");
//		client.request(tcp31016);
		
		return "index";
	}
	
	
}
