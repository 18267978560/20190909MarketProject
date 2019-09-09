package com.legou.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.legou.common.redis.JedisClient;
import com.legou.common.utils.JsonUtils;
import com.legou.common.utils.LegouResult;
import com.legou.mapper.TbUserMapper;
import com.legou.pojo.TbUser;
import com.legou.pojo.TbUserExample;
import com.legou.pojo.TbUserExample.Criteria;
import com.legou.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	//'单点登录'，存入缓存中，并设置过期时间
	@Override
	public LegouResult login(String username, String password) {

		//根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		//创建条件
		Criteria createCriteria = example.createCriteria();
		//添加条件
		createCriteria.andUsernameEqualTo(username);
		//查到数据库中符合类型的
		List<TbUser> list = tbUserMapper.selectByExample(example);
		//判断是否有值
		if(list == null || list.size() == 0) {
			System.out.println("第一个");
			return LegouResult.build(400, "用户名或密码错误");
		}
		//取出下标为0的那个(只有1个所以只可能是0)
		TbUser tbUser = list.get(0);
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
			//如果不正确的话就反回错误
			System.out.println("第二个");
			return LegouResult.build(400, "用户名或者密码错误");
		}
		//随机生成
		String token = UUID.randomUUID().toString();
		//将密码置空
		tbUser.setPassword(null);
		jedisClient.set("SESSION" + token, JsonUtils.objectToJson(tbUser));
		//设置过期时间
		jedisClient.expire("SESSION" + token, SESSION_EXPIRE);
		
		return LegouResult.ok(token);
	}
}
