package com.santrong.meeting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.base.BaseAction;

/**
 * @author weinianjie
 * @date 2014年7月15日
 * @time 下午2:54:11
 */
@Controller
@RequestMapping("/meeting")
public class MeetingAction extends BaseAction{
	
	@RequestMapping("/home")
	public String home(){
		return "meeting/home";
	}
}
