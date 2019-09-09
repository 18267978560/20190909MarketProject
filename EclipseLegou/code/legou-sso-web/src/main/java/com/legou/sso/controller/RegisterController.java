package com.legou.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbUser;
import com.legou.sso.service.RegisterService;

@Controller
public class RegisterController {

	@Autowired
	private RegisterService registerService;
	
	//实现注册方法：跳转到注册页面的方法
	@RequestMapping("/page/register")
	public String register() {
		
		return "register";
		
	}
	
	//实现注册方法：检查name和phone是否重复，如果重复的话，注册失败
	@RequestMapping("/user/check/{attribute}/{math}")
	@ResponseBody
	public LegouResult checkUser(@PathVariable String attribute,@PathVariable int math) {
		//调用实现类，判断name和phone是否重复
		LegouResult legouResult = registerService.checkUser(attribute,math);
		
		return legouResult;
	}

	//实现注册方法：注册用户
	@RequestMapping("/user/register")
	@ResponseBody
	public LegouResult registerUser(TbUser tbUser){
		//调用实现类，注册
		LegouResult legouResult = registerService.register(tbUser);
		
		return legouResult;
	}
}
