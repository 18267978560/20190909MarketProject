package com.legou.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.legou.common.utils.LegouResult;
import com.legou.mapper.TbUserMapper;
import com.legou.pojo.TbUser;
import com.legou.pojo.TbUserExample;
import com.legou.pojo.TbUserExample.Criteria;
import com.legou.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	private TbUserMapper tbUserMapper;
	
	//注册用户：判断用户的name和phone是否重复的方法
	@Override
	public LegouResult checkUser(String attribute, int math) {
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria createCriteria = example.createCriteria();
		//通过传入的数字判断是需要比较name或是phone
		if(math == 1) {
			createCriteria.andUsernameEqualTo(attribute);
		}else if(math == 2){
			createCriteria.andPhoneEqualTo(attribute);
		}else {
			return LegouResult.build(400, "数据类型错误");
		}
		//搜索出符合条件的类型并存入集合中
		List<TbUser> tbUser = tbUserMapper.selectByExample(example);
		
		//如果tbUser不为空切>0那么就意味着name或者phone重复，返回false，无法创建
		if(tbUser != null &&  tbUser.size() > 0) {
			return LegouResult.ok(false);
		}
		//如果不为重复，则返回true
		return LegouResult.ok(true);
	}
	

	//注册用户：注册的方法
	@Override
	public LegouResult register(TbUser tbUser) {
		//判断是否有空值，双重判断
		if(StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword()) 
			|| StringUtils.isBlank(tbUser.getPhone())) {
			return LegouResult.build(400,"数据缺失");
		}
		//将username和phone放入checkUser的方法中判定是否重复，进行双重判断
		LegouResult username = checkUser(tbUser.getUsername(), 1);
		if(!(boolean) username.getData()) {
			return LegouResult.build(400, "该用户名已被占用");
		}
		LegouResult phone = checkUser(tbUser.getPhone(),2);
		if(!(boolean) phone.getData()) {
			return LegouResult.build(400, "该号码已被注册");
		}
		//按照数据库添加创建时间和修改的时间
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		//将密码加密
		String md5Digest = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		//将加密的密码添加到类中
		tbUser.setPassword(md5Digest);
		//将数据添加入数据库
		tbUserMapper.insert(tbUser);
		return LegouResult.ok();
	}


}
