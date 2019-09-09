package com.legou.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.utils.CookieUtils;
import com.legou.common.utils.LegouResult;
import com.legou.sso.service.LoginService;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showLogin() {
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public LegouResult login(String username ,String password,HttpServletRequest request,HttpServletResponse response) {
		
		LegouResult legouResult = loginService.login(username,password);
		
		//判断登陆是否成功
		if(legouResult.getStatus() == 200) {
			String token = legouResult.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
			
		}
		return legouResult;
	}
}
