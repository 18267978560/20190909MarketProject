package com.legou.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.common.redis.JedisClient;
import com.legou.common.utils.JsonUtils;
import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbUser;
import com.legou.sso.service.TokenService;


@Controller
public class TokenController {	

	@Autowired
	private TokenService tokenService;

	
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		//callback 这个是js调用时传过来的参数，内容就是回调的方法名
		System.out.println("callback : " + callback);
		System.out.println("token : " + token);
		//通过token从缓存中获得值
		LegouResult result = tokenService.getTbUser(token);
		System.out.println("result : " + result);
		System.out.println("sso-web-TokenController");
		if(StringUtils.isNotBlank(callback)) { 
			//把结果封装成一个js语句响应 
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			System.out.println(mappingJacksonValue);
			System.out.println("返回jsonp");
			return mappingJacksonValue; 
		}
		//两种方法
//		String jsonStr = JsonUtils.objectToJson(result);
//		return callback + "("+jsonStr+");";
		return result;
	}


}
