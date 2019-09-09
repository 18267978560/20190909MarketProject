package com.legou.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.legou.common.redis.JedisClient;
import com.legou.common.utils.JsonUtils;
import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbUser;
import com.legou.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService{

	@Autowired
	JedisClient jedisClient;
	
	//传入token，获取
	@Override
	public LegouResult getTbUser(String token) {
		//从缓存中通过key获得value
		String json = jedisClient.get("SESSION"+token);
		//返回一个tbUser
		if(StringUtils.isBlank(json)) {
			return LegouResult.build(400, "用户缓存已过期");
		}
		//设置过期时间
		jedisClient.expire("SESSION"+token, 1800);
		//获得TbUser类型的信息，并返回
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		return LegouResult.ok(tbUser);
	}

}
