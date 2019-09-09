package com.legou.sso.service;

import com.legou.common.utils.LegouResult;

public interface LoginService {

	LegouResult login(String username, String password);
	
}
