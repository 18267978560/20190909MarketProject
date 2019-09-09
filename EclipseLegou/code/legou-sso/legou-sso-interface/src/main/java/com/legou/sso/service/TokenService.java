package com.legou.sso.service;

import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbUser;

public interface TokenService {

	LegouResult getTbUser(String token);
	
}
