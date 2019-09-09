package com.legou.sso.service;

import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbUser;

public interface RegisterService {


	LegouResult checkUser(String attribute, int math);

	LegouResult register(TbUser tbUser);

	
}
